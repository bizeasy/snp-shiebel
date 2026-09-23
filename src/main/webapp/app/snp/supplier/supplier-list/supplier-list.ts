import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { SupplierRow, SupplierService } from '../supplier.service';

/**
 * Supplier list, ported from snp21 snp/supplier/supplier-list.
 *
 * snp21's service assembled firstName/email/phoneNumber/code/vat/status query strings
 * for server-side filtering, but the list never passed a formData argument and the
 * backend endpoint only accepts page and size, so none of it ever ran. Search here
 * filters the loaded page and is labelled as such.
 */
@Component({
  selector: 'sys-supplier-list',
  templateUrl: './supplier-list.html',
  styleUrl: './supplier-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class SupplierList {
  readonly rows = signal<SupplierRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100, 250];

  readonly columns = [
    { key: 'id' as const, header: 'ID' },
    { key: 'name' as const, header: 'Name' },
    { key: 'emailId' as const, header: 'Email' },
    { key: 'phoneNumber' as const, header: 'Phone Number' },
    { key: 'code' as const, header: 'Code' },
    { key: 'vatNo' as const, header: 'VAT' },
  ];

  private readonly supplierService = inject(SupplierService);

  readonly filteredRows = computed<SupplierRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r =>
      [r.id, r.name, r.emailId, r.phoneNumber, r.code, r.vatNo].some(v => v != null && String(v).toLowerCase().includes(term)),
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
    this.supplierService.getAllSuppliersList(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the supplier list.');
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

  cell(row: SupplierRow, key: keyof SupplierRow): string {
    const v = row[key];
    return v == null || v === '' ? '—' : String(v);
  }
}
