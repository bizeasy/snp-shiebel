import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { OrderDetail, SalesOrderService } from '../../sales-order/sales-order.service';

/**
 * Purchase order detail — ported from snp21 snp/purchase-order/purchase-order-view.
 *
 * Reuses the shared order detail endpoint; a purchase order carries a supplier where a
 * sales order carries a customer, and both blocks render only when populated.
 */
@Component({
  selector: 'sys-purchase-order-view',
  templateUrl: './purchase-order-view.html',
  styleUrl: './purchase-order-view.scss',
  imports: [RouterLink, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class PurchaseOrderView {
  readonly order = signal<OrderDetail | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  private readonly route = inject(ActivatedRoute);
  private readonly orderService = inject(SalesOrderService);
  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly orderId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly items = computed(() => this.order()?.productDetailesList ?? []);
  readonly computedTotal = computed(() => this.items().reduce((sum, i) => sum + (i.totalPrice ?? 0), 0));
  readonly totalsDiffer = computed(() => {
    const stated = this.order()?.grandTotal;
    return stated != null && Math.abs(stated - this.computedTotal()) > 0.005;
  });

  readonly header = computed(() => {
    const o = this.order();
    return [
      { label: 'Order Id', value: o?.id },
      { label: 'Name', value: o?.name },
      { label: 'Project', value: o?.projectName },
      { label: 'Reference No.', value: o?.refNo },
      { label: 'Customer Order Ref', value: o?.customerOrderRef },
      { label: 'Factory Offer Ref', value: o?.factoryRef },
      { label: 'Order Type', value: o?.orderType },
      { label: 'Status', value: o?.status },
      { label: 'Version', value: o?.version },
    ].map(f => ({ label: f.label, value: this.display(f.value) }));
  });

  readonly terms = computed(() => {
    const t = this.order()?.termsReq;
    return [
      { label: 'Currency', value: t?.currency },
      { label: 'Payment Terms', value: t?.paymentTerms },
      { label: 'Delivery Terms', value: t?.deliveryTerms },
      { label: 'Remarks', value: t?.remarks },
    ].map(f => ({ label: f.label, value: this.display(f.value) }));
  });

  constructor() {
    queueMicrotask(() => this.load());
  }

  private display(v: unknown): string {
    return v == null || v === '' ? '—' : String(v);
  }

  party(block: 'customerDetails' | 'consigneeDetails' | 'supplierDetails'): { label: string; value: string }[] {
    const p = this.order()?.[block];
    if (!p || p.id == null) {
      return [];
    }
    return [
      { label: 'Name', value: this.display(p.name) },
      { label: 'Email', value: this.display(p.email) },
      { label: 'Phone', value: this.display(p.phoneNumber) },
      { label: 'Address', value: this.display(p.address) },
    ];
  }

  private load(): void {
    const id = this.orderId();
    if (id === null) {
      this.error.set('No order id supplied.');
      this.loading.set(false);
      return;
    }
    this.orderService.getPurchaseOrderById(id).subscribe({
      next: detail => {
        this.order.set(detail ?? null);
        if (!detail) {
          this.error.set('Order not found.');
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this purchase order.');
        this.loading.set(false);
      },
    });
  }
}
