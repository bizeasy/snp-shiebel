import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { looksLikeId } from '../../shared/looks-like-id';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs/operators';

import { SupplierService } from '../supplier.service';

/**
 * Create / edit a supplier — ported from snp21 snp/supplier/create-supplier.
 *
 * One component serves both routes, switching on the `edit` query param, as snp21 did.
 *
 * snp21's form omitted TAN even though CreateSupplierReq carries tanNo and the list
 * surfaced it as a column, so TAN could never be filled in. It is included here.
 */
@Component({
  selector: 'sys-supplier-form',
  templateUrl: './supplier-form.html',
  styleUrl: './supplier-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class SupplierForm {
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly supplierService = inject(SupplierService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(200)]],
    vatNo: ['', [Validators.required, Validators.maxLength(100)]],
    tanNo: ['', [Validators.maxLength(60)]],
    code: ['', [Validators.required, Validators.maxLength(20)]],
    phone: ['', [Validators.maxLength(20)]],
    email: ['', [Validators.email, Validators.maxLength(75)]],
    address: ['', [Validators.maxLength(10000)]],
  });

  /** Warns when the address is nothing but a long number, the shape snp21 data arrived in. */
  readonly addressLooksLikeId = toSignal(this.form.controls.address.valueChanges.pipe(map(v => looksLikeId(v))), { initialValue: false });

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly supplierId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly isEdit = computed(() => this.params()['edit'] === 'true' && this.supplierId() !== null);
  readonly title = computed(() => (this.isEdit() ? 'Edit Supplier' : 'Create Supplier'));

  private loadedFor: number | null = null;

  constructor() {
    queueMicrotask(() => this.maybeLoad());
  }

  private maybeLoad(): void {
    const id = this.supplierId();
    if (!this.isEdit() || id === null || this.loadedFor === id) {
      return;
    }
    this.loadedFor = id;
    this.loading.set(true);
    this.supplierService.getSupplierById(id).subscribe({
      next: row => {
        this.form.patchValue({
          name: row?.name ?? '',
          vatNo: row?.vatNo ?? '',
          tanNo: row?.tanNumber ?? '',
          code: row?.code ?? '',
          phone: row?.phoneNumber ?? '',
          email: row?.emailId ?? '',
          address: row?.address ?? '',
        });
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this supplier.');
        this.loading.set(false);
      },
    });
  }

  invalid(control: 'name' | 'vatNo' | 'tanNo' | 'code' | 'phone' | 'email' | 'address'): boolean {
    const c = this.form.controls[control];
    return c.invalid && (c.dirty || c.touched);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const payload = this.form.getRawValue();
    const id = this.supplierId();
    const request$ =
      this.isEdit() && id !== null ? this.supplierService.updateSupplier(id, payload) : this.supplierService.createSupplier(payload);

    request$.subscribe({
      next: () => {
        this.saving.set(false);
        void this.router.navigate(['/supplier/supplier-list']);
      },
      error: () => {
        this.saving.set(false);
        this.error.set(this.isEdit() ? 'Could not update the supplier.' : 'Could not create the supplier.');
      },
    });
  }

  cancel(): void {
    void this.router.navigate(['/supplier/supplier-list']);
  }
}
