import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { looksLikeId } from '../../shared/looks-like-id';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs/operators';

import { CustomerService } from '../customer.service';

/**
 * Create / edit a customer — ported from snp21 create-customer.
 *
 * As in snp21 a single component serves both routes, switching on the `edit` query
 * param, so the two paths cannot drift apart.
 *
 * Scope note: snp21's form also rendered Agreement Available, Validity Date,
 * Attachment and a contact-person block (name / phone / email). The backend contract
 * is CustomerDetailsReq, which carries only name, email, phoneNumber and address, so
 * those six inputs were collected and silently discarded. They are left out here
 * rather than reproducing a form that pretends to save data it drops; wiring them up
 * means modelling them (PartyContactMech for the contact person, Attachment for the
 * file, PartyIdentification for the agreement) on both sides.
 */
@Component({
  selector: 'sys-customer-form',
  templateUrl: './customer-form.html',
  styleUrl: './customer-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class CustomerForm {
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  private readonly fb = inject(FormBuilder);
  private readonly customerService = inject(CustomerService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
    email: ['', [Validators.email, Validators.maxLength(75)]],
    phoneNumber: ['', [Validators.maxLength(20)]],
    address: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10000)]],
  });

  /** Warns when the address is nothing but a long number, the shape snp21 data arrived in. */
  readonly addressLooksLikeId = toSignal(this.form.controls.address.valueChanges.pipe(map(v => looksLikeId(v))), { initialValue: false });

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly customerId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly isEdit = computed(() => this.params()['edit'] === 'true' && this.customerId() !== null);
  readonly title = computed(() => (this.isEdit() ? 'Edit Customer' : 'Create Customer'));

  private loadedFor: number | null = null;

  constructor() {
    // load once when editing; toSignal makes params reactive without a subscribe callback
    queueMicrotask(() => this.maybeLoad());
  }

  private maybeLoad(): void {
    const id = this.customerId();
    if (!this.isEdit() || id === null || this.loadedFor === id) {
      return;
    }
    this.loadedFor = id;
    this.loading.set(true);
    this.customerService.getCustomerById(id).subscribe({
      next: row => {
        this.form.patchValue({
          name: row?.name ?? '',
          email: row?.email ?? '',
          phoneNumber: row?.phoneNumber ?? '',
          address: row?.address ?? '',
        });
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this customer.');
        this.loading.set(false);
      },
    });
  }

  invalid(control: 'name' | 'email' | 'phoneNumber' | 'address'): boolean {
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
    const id = this.customerId();
    const request$ =
      this.isEdit() && id !== null ? this.customerService.updateCustomer(id, payload) : this.customerService.createCustomer(payload);

    request$.subscribe({
      next: () => {
        this.saving.set(false);
        void this.router.navigate(['/sales-management/customer-list']);
      },
      error: (err: unknown) => {
        this.saving.set(false);
        this.error.set(this.isEdit() ? 'Could not update the customer.' : 'Could not create the customer.');
        // eslint-disable-next-line no-console
        console.error('customer save failed', err);
      },
    });
  }

  cancel(): void {
    void this.router.navigate(['/sales-management/customer-list']);
  }
}
