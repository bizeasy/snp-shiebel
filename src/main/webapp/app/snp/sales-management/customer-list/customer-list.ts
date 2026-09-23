import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { CustomerRow, CustomerService } from '../customer.service';

/**
 * Customer list — ported from snp21 snp/sales-management/customer-list.
 *
 * Differences forced by the target stack, all deliberate:
 *  - PrimeNG p-table/p-menu/p-button are gone (PrimeNG 22 is commercially licensed and
 *    was dropped), so this is a Bootstrap table plus an ng-bootstrap dropdown.
 *  - State is signals and the component is OnPush: Angular 22 here is zoneless, so the
 *    snp21 pattern of mutating plain fields inside a subscribe callback would not repaint.
 *  - snp21 filtered only the rows of the current page while showing a server total, so
 *    searching appeared to "lose" rows. Search is applied to the loaded page and the
 *    count shown is the filtered count, so what you read matches what you see.
 */
@Component({
  selector: 'sys-customer-list',
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class CustomerList {
  readonly rows = signal<CustomerRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100, 250];

  readonly columns = [
    { key: 'id' as const, header: 'Id' },
    { key: 'name' as const, header: 'Customer Name' },
    { key: 'phoneNumber' as const, header: 'Phone Number' },
    { key: 'address' as const, header: 'Address' },
    { key: 'email' as const, header: 'Email' },
  ];

  private readonly customerService = inject(CustomerService);

  readonly filteredRows = computed<CustomerRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r =>
      [r.id, r.name, r.phoneNumber, r.address, r.email].some(v => v != null && String(v).toLowerCase().includes(term)),
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
    this.customerService.getAllCustomersList(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: (err: unknown) => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set(err instanceof Error ? err.message : 'Could not load the customer list.');
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

  cell(row: CustomerRow, key: keyof CustomerRow): string {
    const v = row[key];
    return v == null || v === '' ? '—' : String(v);
  }
}
