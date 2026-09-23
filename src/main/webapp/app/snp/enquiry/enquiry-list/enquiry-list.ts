import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { EnquiryRow, EnquiryService } from '../enquiry.service';

/**
 * Enquiry (RFQ) list.
 *
 * New in practice: snp21 had this screen, but its list call went to a path the server never
 * served, so it could only ever show an empty table.
 */
@Component({
  selector: 'sys-enquiry-list',
  templateUrl: './enquiry-list.html',
  styleUrl: './enquiry-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DatePipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class EnquiryList {
  readonly rows = signal<EnquiryRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100];

  private readonly enquiryService = inject(EnquiryService);

  readonly filteredRows = computed<EnquiryRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r => [r.enquiryNo, r.customer, r.status].some(v => v != null && String(v).toLowerCase().includes(term)));
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
    this.enquiryService.list(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the enquiry list.');
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

  statusClass(row: EnquiryRow): string {
    switch (row.statusName) {
      case 'CRQ_COMPLETED':
        return 'bg-success-subtle text-success-emphasis';
      case 'CRQ_QUOTE_RECEIVED':
        return 'bg-primary-subtle text-primary-emphasis';
      case 'CRQ_CANCELLED':
      case 'CRQ_REJECTED':
        return 'bg-danger-subtle text-danger-emphasis';
      default:
        return 'bg-light text-dark';
    }
  }
}
