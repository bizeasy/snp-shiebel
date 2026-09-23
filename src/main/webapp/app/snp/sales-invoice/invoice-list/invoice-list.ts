import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { InvoiceRow, InvoiceService, InvoiceStatus } from '../invoice.service';

/**
 * Invoice list.
 *
 * New: snp21 had no invoice list, because it never wrote an invoice. Statuses are shown as
 * a word plus colour rather than colour alone, and the actions offered depend on where the
 * invoice is: a cancelled invoice cannot be settled, and a paid one cannot be re-approved.
 */
@Component({
  selector: 'sys-invoice-list',
  templateUrl: './invoice-list.html',
  styleUrl: './invoice-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class InvoiceList {
  readonly rows = signal<InvoiceRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100];

  private readonly invoiceService = inject(InvoiceService);

  readonly filteredRows = computed<InvoiceRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r =>
      [r.invoiceNo, r.customer, r.orderRefNo, r.orderName, r.status].some(v => v != null && String(v).toLowerCase().includes(term)),
    );
  });

  readonly totalPages = computed(() => Math.max(1, Math.ceil(this.totalRecords() / this.size())));
  readonly rangeStart = computed(() => (this.totalRecords() === 0 ? 0 : this.page() * this.size() + 1));
  readonly rangeEnd = computed(() => Math.min((this.page() + 1) * this.size(), this.totalRecords()));

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.invoiceService.list(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the invoice list.');
        this.loading.set(false);
      },
    });
  }

  goToPage(p: number): void {
    if (p < 0 || p >= this.totalPages() || p === this.page()) {
      return;
    }
    this.page.set(p);
    this.load();
  }

  changeSize(value: string): void {
    this.size.set(Number(value));
    this.page.set(0);
    this.load();
  }

  setStatus(row: InvoiceRow, status: InvoiceStatus): void {
    this.notice.set(null);
    this.error.set(null);
    this.invoiceService.changeStatus(row.id, status).subscribe({
      next: body => {
        if (body.error) {
          this.error.set(String(body.error));
          return;
        }
        this.notice.set(`${row.invoiceNo ?? 'Invoice'} is now ${body.success?.status ?? 'updated'}.`);
        this.load();
      },
      error: () => this.error.set('Could not update the invoice.'),
    });
  }

  canApprove(row: InvoiceRow): boolean {
    return row.statusName === 'INVOICE_CREATED';
  }

  canSettle(row: InvoiceRow): boolean {
    return row.statusName === 'INVOICE_CREATED' || row.statusName === 'INVOICE_APPROVED';
  }

  canCancel(row: InvoiceRow): boolean {
    return row.statusName !== 'INVOICE_CANCELLED' && row.statusName !== 'INVOICE_PAID';
  }

  /** Bootstrap contextual class for the status pill. The word is always shown too. */
  statusClass(row: InvoiceRow): string {
    switch (row.statusName) {
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
}
