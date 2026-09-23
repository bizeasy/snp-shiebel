import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { OrderRow, SalesOrderService } from '../../sales-order/sales-order.service';

/**
 * Purchase order list — ported from snp21 snp/purchase-order/purchase-order-list.
 *
 * Shares SalesOrderService with the sales order list: both hit /api/orders with the
 * same envelope, differing only in order type.
 *
 * snp21 built poNumber/supplierName/supplierOrderDate/status query strings for
 * server-side filtering, but the list never passed a formData argument and the endpoint
 * only accepts page and size, so none of it ran. Search filters the loaded page here.
 */
@Component({
  selector: 'sys-purchase-order-list',
  templateUrl: './purchase-order-list.html',
  styleUrl: './purchase-order-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class PurchaseOrderList {
  readonly rows = signal<OrderRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100, 250];

  private readonly orderService = inject(SalesOrderService);

  readonly filteredRows = computed<OrderRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r =>
      [r.id, r.name, r.refNo, r.supplier, r.status, r.projectName].some(v => v != null && String(v).toLowerCase().includes(term)),
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
    this.orderService.getAllPurchaseList(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the purchase order list.');
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

  partyName(value: string | null): string {
    if (!value) {
      return '—';
    }
    const dash = value.indexOf('-');
    return dash > -1 ? value.slice(dash + 1) || '—' : value;
  }
}
