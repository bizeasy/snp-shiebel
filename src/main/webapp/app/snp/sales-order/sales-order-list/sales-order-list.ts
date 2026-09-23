import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { OrderRow, SalesOrderService } from '../sales-order.service';
import { OrderPdfService } from '../order-pdf.service';

/**
 * Sales order list — ported from snp21 snp/sales-order/sales-order-list.
 *
 * snp21 labelled the `id` column "Ref No" and `refNo` (which holds Order.externalId)
 * "SME SO Number", and showed `supplier` under a "Project" heading. The headings here
 * name what the column actually contains.
 */
@Component({
  selector: 'sys-sales-order-list',
  templateUrl: './sales-order-list.html',
  styleUrl: './sales-order-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class SalesOrderList {
  readonly rows = signal<OrderRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100, 250];

  private readonly salesOrderService = inject(SalesOrderService);
  private readonly orderPdf = inject(OrderPdfService);

  readonly filteredRows = computed<OrderRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r =>
      [r.id, r.name, r.refNo, r.party, r.supplier, r.status, r.projectName].some(v => v != null && String(v).toLowerCase().includes(term)),
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
    this.salesOrderService.getAllSalesList(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the sales order list.');
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

  readonly downloading = signal<number | null>(null);

  /**
   * Fetch the order and write a PDF.
   *
   * snp21 navigated to a separate view-invoice screen and rasterised it with html2canvas.
   * Here the document is generated from the order data, so no intermediate screen is needed
   * and the result is real text rather than a picture of a page.
   */
  downloadPdf(row: OrderRow): void {
    if (this.downloading() !== null) {
      return;
    }
    this.downloading.set(row.id);
    this.error.set(null);
    this.salesOrderService.getSalesOrderById(row.id).subscribe({
      next: detail => {
        // Async now (the logo is fetched once), so the spinner stays until the file is saved.
        this.orderPdf
          .download(detail, 'sales')
          .catch(() => this.error.set(`Could not create the PDF for order ${row.refNo ?? String(row.id)}.`))
          .finally(() => this.downloading.set(null));
      },
      error: () => {
        this.error.set('Could not load order ' + (row.refNo ?? row.id) + ' for download.');
        this.downloading.set(null);
      },
    });
  }

  /** "12-Acme Ltd" -> "Acme Ltd" for display. */
  partyName(value: string | null): string {
    if (!value) {
      return '—';
    }
    const dash = value.indexOf('-');
    return dash > -1 ? value.slice(dash + 1) || '—' : value;
  }
}
