import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { InvoiceDetail, InvoiceService, InvoiceStatus } from '../invoice.service';

/**
 * One invoice, with its lines and the order it bills.
 *
 * Product lines and the tax adjustment come back in the same list, so they are split here:
 * the tax line is an InvoiceItem of type INVOICE_HEAD_ADJ, which is how the model carries a
 * header-level charge.
 */
@Component({
  selector: 'sys-invoice-view',
  templateUrl: './invoice-view.html',
  styleUrl: './invoice-view.scss',
  imports: [RouterLink, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class InvoiceView {
  readonly invoice = signal<InvoiceDetail | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);

  private readonly invoiceService = inject(InvoiceService);
  private readonly route = inject(ActivatedRoute);

  readonly productLines = computed(() => (this.invoice()?.items ?? []).filter(i => i.type !== 'INVOICE_HEAD_ADJ'));
  readonly adjustmentLines = computed(() => (this.invoice()?.items ?? []).filter(i => i.type === 'INVOICE_HEAD_ADJ'));

  readonly subtotal = computed(() => this.productLines().reduce((sum, l) => sum + (l.amount ?? 0), 0));
  readonly adjustmentTotal = computed(() => this.adjustmentLines().reduce((sum, l) => sum + (l.amount ?? 0), 0));

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (Number.isFinite(id) && id > 0) {
      this.load(id);
    } else {
      this.error.set('No invoice was requested.');
      this.loading.set(false);
    }
  }

  load(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.invoiceService.details(id).subscribe({
      next: detail => {
        this.invoice.set(detail);
        this.loading.set(false);
      },
      error: () => {
        this.invoice.set(null);
        this.error.set('Could not load that invoice.');
        this.loading.set(false);
      },
    });
  }

  setStatus(status: InvoiceStatus): void {
    const current = this.invoice();
    if (!current) {
      return;
    }
    this.notice.set(null);
    this.error.set(null);
    this.invoiceService.changeStatus(current.id, status).subscribe({
      next: body => {
        if (body.error) {
          this.error.set(String(body.error));
          return;
        }
        this.notice.set(`Invoice is now ${body.success?.status ?? 'updated'}.`);
        this.load(current.id);
      },
      error: () => this.error.set('Could not update the invoice.'),
    });
  }

  canApprove(): boolean {
    return this.invoice()?.statusName === 'INVOICE_CREATED';
  }

  canSettle(): boolean {
    const s = this.invoice()?.statusName;
    return s === 'INVOICE_CREATED' || s === 'INVOICE_APPROVED';
  }

  canCancel(): boolean {
    const s = this.invoice()?.statusName;
    return s !== 'INVOICE_CANCELLED' && s !== 'INVOICE_PAID';
  }

  statusClass(): string {
    switch (this.invoice()?.statusName) {
      case 'INVOICE_PAID':
        return 'bg-success-subtle text-success-emphasis';
      case 'INVOICE_APPROVED':
        return 'bg-primary-subtle text-primary-emphasis';
      case 'INVOICE_CANCELLED':
        return 'bg-danger-subtle text-danger-emphasis';
      default:
        return 'bg-light text-dark';
    }
  }

  print(): void {
    window.print();
  }
}
