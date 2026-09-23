import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { QuoteRow, QuoteService } from '../quote.service';

/**
 * Quote list.
 *
 * snp21's quote.service.ts contained a single call, to /api/experiments/user/table, which no
 * controller served - so this screen never showed a quote. The list is real here.
 */
@Component({
  selector: 'sys-quote-list',
  templateUrl: './quote-list.html',
  styleUrl: './quote-list.scss',
  imports: [FormsModule, RouterLink, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class QuoteList {
  readonly rows = signal<QuoteRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100];

  private readonly quoteService = inject(QuoteService);

  readonly filteredRows = computed<QuoteRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r => [r.quoteNo, r.supplier, r.enquiryNo, r.status].some(v => v != null && String(v).toLowerCase().includes(term)));
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
    this.quoteService.list(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the quote list.');
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

  statusClass(row: QuoteRow): string {
    switch (row.statusName) {
      case 'QUOTE_APPROVED':
        return 'bg-success-subtle text-success-emphasis';
      case 'QUOTE_REJECTED':
        return 'bg-danger-subtle text-danger-emphasis';
      case 'QUOTE_ORDERED':
        return 'bg-primary-subtle text-primary-emphasis';
      default:
        return 'bg-light text-dark';
    }
  }
}
