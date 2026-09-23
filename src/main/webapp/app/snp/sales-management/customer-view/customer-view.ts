import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { CustomerRow, CustomerService } from '../customer.service';

/**
 * Read-only customer details — ported from snp21 customer-view.
 *
 * snp21 called /api/customer/id/{id}/details, which nothing served, so this screen
 * always rendered blank. It now calls the path the controller actually exposes.
 */
@Component({
  selector: 'sys-customer-view',
  templateUrl: './customer-view.html',
  styleUrl: './customer-view.scss',
  imports: [RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class CustomerView {
  readonly customer = signal<CustomerRow | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  private readonly route = inject(ActivatedRoute);
  private readonly customerService = inject(CustomerService);

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });
  readonly customerId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly fields = computed(() => {
    const c = this.customer();
    return [
      { label: 'Id', value: c?.id },
      { label: 'Customer Name', value: c?.name },
      { label: 'Phone Number', value: c?.phoneNumber },
      { label: 'Email', value: c?.email },
      { label: 'Billing Address', value: c?.address },
    ].map(f => ({ label: f.label, value: f.value == null || f.value === '' ? '—' : String(f.value) }));
  });

  constructor() {
    queueMicrotask(() => this.load());
  }

  private load(): void {
    const id = this.customerId();
    if (id === null) {
      this.error.set('No customer id supplied.');
      this.loading.set(false);
      return;
    }
    this.customerService.getCustomerById(id).subscribe({
      next: row => {
        this.customer.set(row ?? null);
        if (!row) {
          this.error.set('Customer not found.');
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this customer.');
        this.loading.set(false);
      },
    });
  }
}
