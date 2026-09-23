import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { SupplierRow, SupplierService } from '../../supplier/supplier.service';
import { OpenEnquiryLine, QuoteService } from '../quote.service';

/**
 * Record a supplier quote against one enquiry line.
 *
 * snp21 collected the same information and posted it to /api/quote/create/quote, which the
 * server never served. Where it did store things, it put them in the wrong columns: the
 * quote number into offerValue (a decimal here), the quantity into Quote.name, and the
 * enquiry link into CustRequestItem.story. Each value now goes where it belongs.
 */
@Component({
  selector: 'sys-quote-form',
  templateUrl: './quote-form.html',
  styleUrl: './quote-form.scss',
  imports: [ReactiveFormsModule, FormsModule, RouterLink, FaIconComponent, DatePipe, DecimalPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class QuoteForm {
  readonly openLines = signal<OpenEnquiryLine[]>([]);
  readonly suppliers = signal<SupplierRow[]>([]);
  readonly selected = signal<OpenEnquiryLine | null>(null);
  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  private readonly fb = inject(FormBuilder);
  private readonly quoteService = inject(QuoteService);
  private readonly supplierService = inject(SupplierService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly form = this.fb.nonNullable.group({
    partyId: [null as number | null, Validators.required],
    quoteNumber: [''],
    issueDate: [this.today()],
    validThruDate: [''],
    quantity: [null as number | null, [Validators.required, Validators.min(0.0001)]],
    unitPrice: [null as number | null, [Validators.required, Validators.min(0)]],
    leadTimeDays: [null as number | null],
    estimatedDeliveryDate: [''],
    remarks: [''],
    comments: [''],
  });

  readonly filteredLines = computed<OpenEnquiryLine[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.openLines();
    if (!term) {
      return all;
    }
    return all.filter(l => [l.enquiryNo, l.customer, l.product].some(v => v != null && String(v).toLowerCase().includes(term)));
  });

  readonly lineTotal = computed(() => {
    const v = this.form.getRawValue();
    return (v.quantity ?? 0) * (v.unitPrice ?? 0);
  });

  constructor() {
    this.supplierService.getAllSuppliersList(0, 500).subscribe({ next: r => this.suppliers.set(r.rows) });
    this.loadLines();
  }

  loadLines(): void {
    this.loading.set(true);
    this.quoteService.openEnquiryLines().subscribe({
      next: rows => {
        this.openLines.set(rows);
        this.loading.set(false);
        // Deep link from the enquiry list preselects that enquiry's first open line.
        const wanted = Number(this.params()['enquiryId']);
        if (Number.isFinite(wanted) && wanted > 0) {
          const match = rows.find(l => l.enquiryId === wanted);
          if (match) {
            this.choose(match);
          }
        }
      },
      error: () => {
        this.openLines.set([]);
        this.error.set('Could not load enquiry lines.');
        this.loading.set(false);
      },
    });
  }

  choose(line: OpenEnquiryLine): void {
    this.selected.set(line);
    this.form.patchValue({ quantity: line.quantity ?? null });
  }

  clearSelection(): void {
    this.selected.set(null);
  }

  invalid(name: 'partyId' | 'quantity' | 'unitPrice'): boolean {
    const c = this.form.controls[name];
    return c.invalid && (c.dirty || c.touched);
  }

  submit(): void {
    const line = this.selected();
    if (!line || this.saving()) {
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();

    this.quoteService
      .create({
        custRequestItemId: line.custRequestItemId,
        partyId: v.partyId as number,
        quoteNumber: v.quoteNumber || null,
        issueDate: this.toInstant(v.issueDate),
        validThruDate: this.toInstant(v.validThruDate),
        quantity: v.quantity,
        unitPrice: v.unitPrice,
        leadTimeDays: v.leadTimeDays,
        estimatedDeliveryDate: this.toInstant(v.estimatedDeliveryDate),
        remarks: v.remarks || null,
        comments: v.comments || null,
      })
      .subscribe({
        next: body => {
          this.saving.set(false);
          if (body.error) {
            this.error.set(String(body.error));
            return;
          }
          void this.router.navigate(['/quote/quote-list']);
        },
        error: err => {
          this.saving.set(false);
          this.error.set(err?.error?.error ? String(err.error.error) : 'Could not record the quote.');
        },
      });
  }

  cancel(): void {
    void this.router.navigate(['/quote/quote-list']);
  }

  private today(): string {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
  }

  private toInstant(value: string | null | undefined): string | null {
    return value ? new Date(value + 'T00:00:00Z').toISOString() : null;
  }
}
