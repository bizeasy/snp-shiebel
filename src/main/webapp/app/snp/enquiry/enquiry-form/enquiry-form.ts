import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { CustomerRow, CustomerService } from '../../sales-management/customer.service';
import { ProductRow, ProductService } from '../../product/product.service';
import { CurrencyUom, SalesOrderService } from '../../sales-order/sales-order.service';
import { EnquiryService } from '../enquiry.service';

/**
 * Raise an enquiry (RFQ), or view one that exists.
 *
 * snp21's version posted to /api/cust-requests/enquiry/create, a path its own server never
 * served, so nothing it collected was ever stored.
 */
@Component({
  selector: 'sys-enquiry-form',
  templateUrl: './enquiry-form.html',
  styleUrl: './enquiry-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class EnquiryForm {
  readonly customers = signal<CustomerRow[]>([]);
  readonly products = signal<ProductRow[]>([]);
  readonly currencies = signal<CurrencyUom[]>([]);
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly enquiryService = inject(EnquiryService);
  private readonly customerService = inject(CustomerService);
  private readonly productService = inject(ProductService);
  private readonly orderService = inject(SalesOrderService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly enquiryId = computed<number | null>(() => {
    const raw = this.params()['id'];
    const n = Number(raw);
    return raw && Number.isFinite(n) && n > 0 ? n : null;
  });

  readonly isView = computed(() => this.params()['view'] === 'true' && this.enquiryId() !== null);
  readonly title = computed(() => (this.isView() ? 'Enquiry' : 'New Enquiry'));

  readonly form = this.fb.nonNullable.group({
    partyId: [null as number | null, Validators.required],
    name: [''],
    currencyUomId: [null as number | null],
    receivedDate: [this.today()],
    responseRequiredDate: [''],
    description: [''],
    remarks: [''],
    lines: this.fb.array([this.newLine(1)]),
  });

  readonly viewData = signal<{ enquiryNo: string; status: string; lines: Record<string, unknown>[] } | null>(null);

  private loadedFor: number | null = null;

  constructor() {
    this.customerService.getAllCustomersList(0, 500).subscribe({ next: r => this.customers.set(r.rows) });
    this.productService.getAllProductsList(0, 500).subscribe({ next: r => this.products.set(r.rows) });
    this.orderService.getCurrencyUoms().subscribe({ next: r => this.currencies.set(r) });
    queueMicrotask(() => this.init());
  }

  get lines(): FormArray {
    return this.form.controls.lines as FormArray;
  }

  private newLine(sequenceNo: number) {
    return this.fb.nonNullable.group({
      sequenceNo: [sequenceNo],
      productId: [null as number | null, Validators.required],
      quantity: [null as number | null, [Validators.required, Validators.min(0.0001)]],
      description: [''],
      requiredByDate: [''],
      maxAmount: [null as number | null],
    });
  }

  private init(): void {
    const id = this.enquiryId();
    if (this.isView() && id !== null && this.loadedFor !== id) {
      this.loadedFor = id;
      this.loading.set(true);
      this.enquiryService.details(id).subscribe({
        next: d => {
          this.viewData.set({
            enquiryNo: d.enquiryNo ?? String(id),
            status: d.status ?? '',
            lines: (d.lines ?? []) as unknown as Record<string, unknown>[],
          });
          this.form.patchValue({
            partyId: d.customerId ?? null,
            name: d.enquiryNo ?? '',
            currencyUomId: d.currencyUomId ?? null,
            receivedDate: d.enquiryDate ? String(d.enquiryDate).slice(0, 10) : '',
            responseRequiredDate: d.responseRequiredDate ? String(d.responseRequiredDate).slice(0, 10) : '',
            description: d.description ?? '',
            remarks: d.remarks ?? '',
          });
          this.lines.clear();
          (d.lines ?? []).forEach((l, i) => {
            const g = this.newLine(l.sequenceNo ?? i + 1);
            g.patchValue({
              productId: l.productId ?? null,
              quantity: l.quantity ?? null,
              description: l.description ?? '',
              requiredByDate: l.requiredByDate ? String(l.requiredByDate).slice(0, 10) : '',
              maxAmount: l.maxAmount ?? null,
            });
            this.lines.push(g);
          });
          this.form.disable({ emitEvent: false });
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Could not load that enquiry.');
          this.loading.set(false);
        },
      });
    }
  }

  addLine(): void {
    this.lines.push(this.newLine(this.lines.length + 1));
  }

  removeLine(index: number): void {
    if (this.lines.length > 1) {
      this.lines.removeAt(index);
      this.renumber();
    }
  }

  duplicateLine(index: number): void {
    const source = this.lines.at(index).getRawValue() as Record<string, unknown>;
    const copy = this.newLine(0);
    copy.patchValue({ ...source }, { emitEvent: false });
    this.lines.insert(index + 1, copy);
    this.renumber();
  }

  moveLine(index: number, delta: number): void {
    const target = index + delta;
    if (target < 0 || target >= this.lines.length) {
      return;
    }
    const control = this.lines.at(index);
    this.lines.removeAt(index);
    this.lines.insert(target, control);
    this.renumber();
  }

  renumber(): void {
    this.lines.controls.forEach((c, i) => c.patchValue({ sequenceNo: i + 1 }, { emitEvent: false }));
  }

  invalid(name: 'partyId'): boolean {
    const c = this.form.controls[name];
    return c.invalid && (c.dirty || c.touched);
  }

  lineInvalid(index: number, field: 'productId' | 'quantity'): boolean {
    const c = this.lines.at(index).get(field);
    return !!c && c.invalid && (c.dirty || c.touched);
  }

  cancel(): void {
    void this.router.navigate(['/enquiry-entry/enquiry-list']);
  }

  submit(): void {
    if (this.isView() || this.saving()) {
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();

    this.enquiryService
      .create({
        partyId: v.partyId as number,
        name: v.name || null,
        description: v.description || null,
        remarks: v.remarks || null,
        receivedDate: this.toInstant(v.receivedDate),
        responseRequiredDate: this.toInstant(v.responseRequiredDate),
        currencyUomId: v.currencyUomId,
        lines: (v.lines as Record<string, unknown>[]).map(l => ({
          productId: l['productId'] as number | null,
          quantity: l['quantity'] as number | null,
          description: (l['description'] as string) || null,
          requiredByDate: this.toInstant(l['requiredByDate'] as string),
          maxAmount: l['maxAmount'] as number | null,
        })),
      })
      .subscribe({
        next: body => {
          this.saving.set(false);
          if (body.error) {
            this.error.set(String(body.error));
            return;
          }
          void this.router.navigate(['/enquiry-entry/enquiry-list']);
        },
        error: err => {
          this.saving.set(false);
          this.error.set(err?.error?.error ? String(err.error.error) : 'Could not save the enquiry.');
        },
      });
  }

  private today(): string {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
  }

  private toInstant(value: string | null | undefined): string | null {
    return value ? new Date(value + 'T00:00:00Z').toISOString() : null;
  }
}
