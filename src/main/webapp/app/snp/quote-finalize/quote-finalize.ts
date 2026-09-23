import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { ComparableLine, Offer, QuoteService } from '../quote/quote.service';

/**
 * Quote finalization: compare the offers on an enquiry line and pick one.
 *
 * snp21 had a list and a create screen here, both talking to /api/quote/... paths the server
 * never served. It also offered no comparison - you picked a quote without seeing what it
 * was being picked over. Offers are shown side by side, cheapest first, with the difference
 * against the best price spelled out.
 *
 * Choosing a winner approves it and rejects the competing quotes on the same line. snp21
 * went straight on to raise a purchase order in the same action; that stays separate here so
 * the commercial decision and the commitment to buy are two deliberate steps.
 */
@Component({
  selector: 'sys-quote-finalize',
  templateUrl: './quote-finalize.html',
  styleUrl: './quote-finalize.scss',
  imports: [FormsModule, RouterLink, FaIconComponent, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class QuoteFinalize {
  readonly lines = signal<ComparableLine[]>([]);
  readonly loading = signal(false);
  readonly saving = signal<number | null>(null);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly search = signal('');
  readonly remarks = signal('');

  private readonly quoteService = inject(QuoteService);

  readonly filtered = computed<ComparableLine[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.lines();
    if (!term) {
      return all;
    }
    return all.filter(l => [l.enquiryNo, l.customer, l.product].some(v => v != null && String(v).toLowerCase().includes(term)));
  });

  readonly openCount = computed(() => this.lines().filter(l => !this.isDecided(l)).length);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.quoteService.comparableLines().subscribe({
      next: rows => {
        this.lines.set(rows);
        this.loading.set(false);
      },
      error: () => {
        this.lines.set([]);
        this.error.set('Could not load the quotes to compare.');
        this.loading.set(false);
      },
    });
  }

  /** A line is settled once one of its offers has been approved. */
  isDecided(line: ComparableLine): boolean {
    return line.offers.some(o => o.statusName === 'QUOTE_APPROVED');
  }

  isWinner(offer: Offer): boolean {
    return offer.statusName === 'QUOTE_APPROVED';
  }

  isRejected(offer: Offer): boolean {
    return offer.statusName === 'QUOTE_REJECTED';
  }

  /** How much more than the cheapest offer on the line, so the trade-off is visible. */
  difference(line: ComparableLine, offer: Offer): number | null {
    if (line.bestTotal == null || offer.totalAmount == null) {
      return null;
    }
    const diff = offer.totalAmount - line.bestTotal;
    return diff === 0 ? null : diff;
  }

  isBest(line: ComparableLine, offer: Offer): boolean {
    return line.bestTotal != null && offer.totalAmount === line.bestTotal;
  }

  finalize(line: ComparableLine, offer: Offer): void {
    if (this.saving() !== null || this.isDecided(line)) {
      return;
    }
    this.saving.set(offer.quoteId);
    this.error.set(null);
    this.notice.set(null);
    this.quoteService.finalize(offer.quoteId, this.remarks() || null).subscribe({
      next: body => {
        this.saving.set(null);
        if (body.error) {
          this.error.set(String(body.error));
          return;
        }
        const rejected = body.success?.rejectedCount ?? 0;
        this.notice.set(
          `${offer.quoteNo ?? 'Quote'} from ${offer.supplier ?? 'supplier'} approved` +
            (rejected > 0 ? `, ${rejected} competing quote${rejected === 1 ? '' : 's'} rejected.` : '.'),
        );
        this.remarks.set('');
        this.load();
      },
      error: err => {
        this.saving.set(null);
        this.error.set(err?.error?.error ? String(err.error.error) : 'Could not finalize that quote.');
      },
    });
  }
}
