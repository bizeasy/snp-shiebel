import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { TermsInput } from '../../shared/terms-input/terms-input';
import { BillableLine, BillableOrder, InvoiceService, InvoiceableOrder, TaxSlab } from '../invoice.service';

/** An order line with the quantity the user has chosen to bill on this invoice. */
interface EditableLine extends BillableLine {
  selected: boolean;
  billQuantity: number;
  price: number;
}

/**
 * Raise a sales invoice against a sales order.
 *
 * Nothing was ported. snp21 had a Create Sales Invoice screen, but its Create button called
 * createPurchaseOrder() and then navigated to the purchase order list, so it produced a
 * purchase order and no invoice; its Next sales invoice button collected an empty array and
 * console.logged it. This screen writes a real Invoice.
 *
 * Part-invoicing is the point of the design: each line shows what was ordered, what has
 * already been billed and what is left, and the quantity to bill defaults to the remainder
 * but can be reduced. The server re-checks every quantity, so trimming the request in the
 * browser cannot over-bill an order line.
 */
@Component({
  selector: 'sys-invoice-form',
  templateUrl: './invoice-form.html',
  styleUrl: './invoice-form.scss',
  imports: [FormsModule, RouterLink, FaIconComponent, DatePipe, DecimalPipe, TermsInput],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class InvoiceForm {
  readonly orders = signal<InvoiceableOrder[]>([]);
  readonly order = signal<BillableOrder | null>(null);
  readonly lines = signal<EditableLine[]>([]);
  readonly taxSlabs = signal<TaxSlab[]>([]);
  readonly invoiceNo = signal<string>('');

  readonly loadingOrders = signal(false);
  readonly loadingOrder = signal(false);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);

  readonly orderSearch = signal('');
  readonly selectedOrderId = signal<number | null>(null);

  readonly invoiceDate = signal(this.today());
  readonly dueDate = signal(this.plusDays(30));
  readonly taxSlabId = signal<number | null>(null);
  readonly paymentTerms = signal('');
  readonly deliveryTerms = signal('');
  readonly remarks = signal('');
  readonly invoiceMessage = signal('');

  private readonly invoiceService = inject(InvoiceService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly filteredOrders = computed(() => {
    const term = this.orderSearch().trim().toLowerCase();
    const open = this.orders().filter(o => !o.fullyInvoiced);
    if (!term) {
      return open;
    }
    return open.filter(o => [o.refNo, o.name, o.customer, o.id].some(v => v != null && String(v).toLowerCase().includes(term)));
  });

  readonly subtotal = computed(() =>
    this.lines()
      .filter(l => l.selected && l.billQuantity > 0)
      .reduce((sum, l) => sum + l.billQuantity * l.price, 0),
  );

  readonly taxRate = computed(() => {
    const id = this.taxSlabId();
    if (id == null) {
      return 0;
    }
    return this.taxSlabs().find(s => s.id === Number(id))?.rate ?? 0;
  });

  readonly taxAmount = computed(() => Math.round(this.subtotal() * this.taxRate()) / 100);
  readonly grandTotal = computed(() => this.subtotal() + this.taxAmount());
  readonly selectedCount = computed(() => this.lines().filter(l => l.selected && l.billQuantity > 0).length);
  readonly canSave = computed(() => !!this.order() && this.selectedCount() > 0 && !this.saving());

  constructor() {
    this.loadOrders();
    this.invoiceService.taxSlabs().subscribe({ next: slabs => this.taxSlabs.set(slabs) });
    this.invoiceService.nextInvoiceNo().subscribe({ next: no => this.invoiceNo.set(no) });

    // Deep link from the sales order list: ?orderId=N skips the picker and opens that order.
    const requested = Number(this.route.snapshot.queryParamMap.get('orderId'));
    if (Number.isFinite(requested) && requested > 0) {
      this.chooseOrder(requested);
    }
  }

  loadOrders(): void {
    this.loadingOrders.set(true);
    this.invoiceService.invoiceableOrders().subscribe({
      next: rows => {
        this.orders.set(rows);
        this.loadingOrders.set(false);
      },
      error: () => {
        this.orders.set([]);
        this.error.set('Could not load the sales orders.');
        this.loadingOrders.set(false);
      },
    });
  }

  chooseOrder(id: number): void {
    this.selectedOrderId.set(id);
    this.loadingOrder.set(true);
    this.error.set(null);
    this.invoiceService.billableOrder(id).subscribe({
      next: order => {
        this.order.set(order);
        this.lines.set(
          order.lines.map(l => ({
            ...l,
            selected: l.remainingQuantity > 0,
            billQuantity: l.remainingQuantity,
            price: l.unitPrice,
          })),
        );
        this.paymentTerms.set(order.paymentTerms ?? '');
        this.deliveryTerms.set(order.deliveryTerms ?? '');
        this.loadingOrder.set(false);
      },
      error: () => {
        this.order.set(null);
        this.lines.set([]);
        this.error.set('Could not load that order.');
        this.loadingOrder.set(false);
      },
    });
  }

  clearOrder(): void {
    this.order.set(null);
    this.lines.set([]);
    this.selectedOrderId.set(null);
  }

  toggleLine(orderItemId: number, selected: boolean): void {
    this.lines.update(all => all.map(l => (l.orderItemId === orderItemId ? { ...l, selected } : l)));
  }

  /** Clamped to the remainder, so the form cannot ask for more than is left. */
  setQuantity(orderItemId: number, value: string): void {
    const asked = Number(value);
    this.lines.update(all =>
      all.map(l => {
        if (l.orderItemId !== orderItemId) {
          return l;
        }
        const clamped = Number.isFinite(asked) ? Math.min(Math.max(asked, 0), l.remainingQuantity) : 0;
        return { ...l, billQuantity: clamped, selected: clamped > 0 ? l.selected : false };
      }),
    );
  }

  setPrice(orderItemId: number, value: string): void {
    const asked = Number(value);
    this.lines.update(all =>
      all.map(l => (l.orderItemId === orderItemId ? { ...l, price: Number.isFinite(asked) && asked >= 0 ? asked : 0 } : l)),
    );
  }

  billAll(): void {
    this.lines.update(all => all.map(l => ({ ...l, selected: l.remainingQuantity > 0, billQuantity: l.remainingQuantity })));
  }

  billNone(): void {
    this.lines.update(all => all.map(l => ({ ...l, selected: false, billQuantity: 0 })));
  }

  lineTotal(line: EditableLine): number {
    return line.selected ? line.billQuantity * line.price : 0;
  }

  save(): void {
    const order = this.order();
    if (!order || this.saving()) {
      return;
    }
    this.saving.set(true);
    this.error.set(null);

    this.invoiceService
      .create({
        orderId: order.orderId,
        invoiceDate: this.toInstant(this.invoiceDate()),
        dueDate: this.toInstant(this.dueDate()),
        taxSlabId: this.taxSlabId() == null ? null : Number(this.taxSlabId()),
        paymentTerms: this.paymentTerms() || null,
        deliveryTerms: this.deliveryTerms() || null,
        remarks: this.remarks() || null,
        invoiceMessage: this.invoiceMessage() || null,
        lines: this.lines()
          .filter(l => l.selected && l.billQuantity > 0)
          .map(l => ({ orderItemId: l.orderItemId, quantity: l.billQuantity, unitPrice: l.price })),
      })
      .subscribe({
        next: body => {
          this.saving.set(false);
          if (body.error) {
            this.error.set(String(body.error));
            return;
          }
          void this.router.navigate(['/sales-invoice', body.success?.id, 'view']);
        },
        error: err => {
          this.saving.set(false);
          this.error.set(err?.error?.error ? String(err.error.error) : 'Could not raise the invoice.');
        },
      });
  }

  private today(): string {
    return this.plusDays(0);
  }

  /**
   * Built from the local date parts, not toISOString(). East of UTC, toISOString() returns
   * yesterday for most of the evening, which would date invoices a day early.
   */
  private plusDays(days: number): string {
    const d = new Date();
    d.setDate(d.getDate() + days);
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${d.getFullYear()}-${month}-${day}`;
  }

  private toInstant(value: string): string | null {
    return value ? new Date(value + 'T00:00:00Z').toISOString() : null;
  }
}
