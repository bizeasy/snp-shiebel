import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { CashBookEntry, CashBookFilter, CashBookService, CashBookSummary, NamedRef, partyDisplayName } from '../cash-book.service';

/** First and last day of the current month, as yyyy-MM-dd. */
function currentMonthRange(): { from: string; to: string } {
  const now = new Date();
  const from = new Date(now.getFullYear(), now.getMonth(), 1);
  const to = new Date(now.getFullYear(), now.getMonth() + 1, 0);
  const iso = (d: Date) => d.toISOString().slice(0, 10);
  return { from: iso(from), to: iso(to) };
}

/**
 * Cash book entries.
 *
 * snp21 had no list at all — only a create form, which could not render (17 of its 24
 * formControlNames were never declared) and posted to an endpoint that did not exist.
 * A ledger with no way to read it back, filter it, or total it by type is not much use,
 * so all three are added here: filtering is done server-side (the generated criteria
 * API already supports it), and the summary is signed by transaction type rather than
 * a single "total" that would otherwise mix income and expense into a meaningless sum.
 */
@Component({
  selector: 'sys-cash-book-list',
  templateUrl: './cash-book-list.html',
  styleUrl: './cash-book-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class CashBookList {
  readonly rows = signal<CashBookEntry[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly confirmDelete = signal<CashBookEntry | null>(null);

  readonly types = signal<NamedRef[]>([]);
  readonly categories = signal<NamedRef[]>([]);
  readonly departments = signal<NamedRef[]>([]);

  private readonly defaultRange = currentMonthRange();
  readonly search = signal('');
  readonly dateFrom = signal(this.defaultRange.from);
  readonly dateTo = signal(this.defaultRange.to);
  readonly typeId = signal<number | null>(null);
  readonly categoryId = signal<number | null>(null);
  readonly departmentId = signal<number | null>(null);

  readonly summary = signal<CashBookSummary | null>(null);
  readonly summaryLoading = signal(false);

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100];

  private readonly cashBookService = inject(CashBookService);

  /** Categories offered for the selected type, plus unscoped catch-alls (e.g. Miscellaneous). */
  readonly categoryOptions = computed<NamedRef[]>(() => {
    const type = this.typeId();
    const all = this.categories();
    return type == null ? all : all.filter(c => c.transactionType == null || c.transactionType.id === type);
  });

  readonly totalPages = computed(() => Math.max(1, Math.ceil(this.totalRecords() / this.size())));
  readonly rangeStart = computed(() => (this.totalRecords() === 0 ? 0 : this.page() * this.size() + 1));
  readonly rangeEnd = computed(() => Math.min((this.page() + 1) * this.size(), this.totalRecords()));

  readonly hasFilters = computed(
    () =>
      !!this.search() ||
      this.dateFrom() !== this.defaultRange.from ||
      this.dateTo() !== this.defaultRange.to ||
      this.typeId() != null ||
      this.categoryId() != null ||
      this.departmentId() != null,
  );

  private readonly currentFilter = computed<CashBookFilter>(() => ({
    dateFrom: this.dateFrom() || null,
    dateTo: this.dateTo() || null,
    transactionTypeId: this.typeId(),
    transactionCategoryId: this.categoryId(),
    departmentId: this.departmentId(),
    search: this.search().trim() || null,
  }));

  constructor() {
    this.cashBookService.transactionTypes().subscribe({ next: r => this.types.set(r), error: () => this.types.set([]) });
    this.cashBookService.transactionCategories().subscribe({ next: r => this.categories.set(r), error: () => this.categories.set([]) });
    this.cashBookService.departments().subscribe({ next: r => this.departments.set(r), error: () => this.departments.set([]) });
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    const filter = this.currentFilter();
    this.cashBookService.search(filter, this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the cash book.');
        this.loading.set(false);
      },
    });
    this.loadSummary(filter);
  }

  private loadSummary(filter: CashBookFilter): void {
    this.summaryLoading.set(true);
    this.cashBookService.summary(filter).subscribe({
      next: s => {
        this.summary.set(s);
        this.summaryLoading.set(false);
      },
      error: () => {
        this.summary.set(null);
        this.summaryLoading.set(false);
      },
    });
  }

  applyFilters(): void {
    this.page.set(0);
    this.load();
  }

  resetFilters(): void {
    this.search.set('');
    this.dateFrom.set(this.defaultRange.from);
    this.dateTo.set(this.defaultRange.to);
    this.typeId.set(null);
    this.categoryId.set(null);
    this.departmentId.set(null);
    this.applyFilters();
  }

  onTypeChange(value: string): void {
    this.typeId.set(value ? Number(value) : null);
    // A category picked under one type rarely belongs to another; clearing avoids a stale filter.
    this.categoryId.set(null);
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

  askDelete(entry: CashBookEntry): void {
    this.confirmDelete.set(entry);
  }

  cancelDelete(): void {
    this.confirmDelete.set(null);
  }

  doDelete(): void {
    const entry = this.confirmDelete();
    if (!entry) {
      return;
    }
    this.busy.set(true);
    this.cashBookService.remove(entry.id).subscribe({
      next: () => {
        this.busy.set(false);
        this.confirmDelete.set(null);
        this.notice.set('Entry deleted.');
        this.load();
      },
      error: () => {
        this.busy.set(false);
        this.confirmDelete.set(null);
        this.error.set('Could not delete that entry.');
      },
    });
  }

  categoryLabel(r: CashBookEntry): string {
    return r.transactionCategory?.description ?? r.transactionCategory?.name ?? '—';
  }

  /** A linked Customer/Supplier record wins over the free-text name when both are present. */
  partyLabel(r: CashBookEntry): string {
    return partyDisplayName(r.fromParty ?? r.toParty) ?? r.vendorCustomerName ?? '—';
  }

  /** Sign amounts by type so a printed/exported ledger reads the same way the summary bar does. */
  signedAmount(r: CashBookEntry): number {
    const type = (r.transactionType?.name ?? '').toUpperCase();
    const amount = r.amount ?? 0;
    return type === 'INCOME' ? amount : type === 'TRANSFER' ? amount : -amount;
  }

  /**
   * Exports every row matching the current filter, not just the current page - fetched
   * fresh at the same SUMMARY_PAGE_SIZE cap the totals use, so a very large ledger export
   * is capped rather than hanging the browser.
   */
  exportCsv(): void {
    this.busy.set(true);
    this.cashBookService.search(this.currentFilter(), 0, 2000).subscribe({
      next: result => {
        this.busy.set(false);
        this.downloadCsv(result.rows);
      },
      error: () => {
        this.busy.set(false);
        this.error.set('Could not export the cash book.');
      },
    });
  }

  private downloadCsv(rows: CashBookEntry[]): void {
    const header = ['Id', 'Date', 'Type', 'Category', 'Department', 'Description', 'Vendor/Customer', 'Invoice No', 'Amount'];
    const escape = (v: unknown) => `"${String(v ?? '').replace(/"/g, '""')}"`;
    const lines = [header.join(',')];
    for (const r of rows) {
      lines.push(
        [
          r.id,
          r.transactionDate ?? '',
          r.transactionType?.name ?? '',
          this.categoryLabel(r),
          r.department?.name ?? '',
          r.description ?? '',
          this.partyLabel(r),
          r.invoiceNo ?? '',
          r.amount ?? 0,
        ]
          .map(escape)
          .join(','),
      );
    }
    const blob = new Blob([lines.join('\n')], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `cash-book-${this.dateFrom() || 'all'}-to-${this.dateTo() || 'all'}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }
}
