import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { UserManagementService } from '../user-management.service';

/**
 * Create / edit a user — ported from snp21 create-user-management.
 *
 * snp21 used ng-multiselect-dropdown for the profile picker; that library is a
 * ControlValueAccessor built for zone-based change detection and is unmaintained, so
 * profiles are plain checkboxes here — there are only a couple of authorities.
 *
 * On edit the password field is optional: leaving it blank keeps the existing password
 * instead of clearing it.
 */
@Component({
  selector: 'sys-user-form',
  templateUrl: './user-form.html',
  styleUrl: './user-form.scss',
  imports: [ReactiveFormsModule, RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class UserForm {
  readonly saving = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly availableProfiles = signal<string[]>([]);
  readonly selectedProfiles = signal<Set<string>>(new Set());

  private readonly fb = inject(FormBuilder);
  private readonly userService = inject(UserManagementService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly form = this.fb.nonNullable.group({
    login: ['', [Validators.required, Validators.maxLength(50)]],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]],
    password: ['', [Validators.minLength(6), Validators.maxLength(60)]],
    activated: [true],
    langKey: ['en'],
  });

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });

  readonly userId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly isEdit = computed(() => this.params()['edit'] === 'true' && this.userId() !== null);
  readonly title = computed(() => (this.isEdit() ? 'Edit User' : 'Create User'));

  private loadedFor: number | null = null;

  constructor() {
    this.userService.getProfiles().subscribe({
      next: list => this.availableProfiles.set(list),
      error: () => this.availableProfiles.set([]),
    });
    queueMicrotask(() => this.maybeLoad());
  }

  private maybeLoad(): void {
    const id = this.userId();
    if (!this.isEdit() || id === null || this.loadedFor === id) {
      return;
    }
    this.loadedFor = id;
    this.loading.set(true);
    this.userService.getUserById(id).subscribe({
      next: row => {
        this.form.patchValue({
          login: row?.login ?? '',
          firstName: row?.firstName ?? '',
          lastName: row?.lastName ?? '',
          email: row?.email ?? '',
          activated: row?.isActivated ?? true,
          langKey: row?.langKey ?? 'en',
        });
        this.selectedProfiles.set(new Set(row?.profile ?? []));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this user.');
        this.loading.set(false);
      },
    });
  }

  toggleProfile(name: string): void {
    this.selectedProfiles.update(set => {
      const next = new Set(set);
      if (next.has(name)) {
        next.delete(name);
      } else {
        next.add(name);
      }
      return next;
    });
  }

  invalid(control: 'login' | 'firstName' | 'lastName' | 'email' | 'password'): boolean {
    const c = this.form.controls[control];
    return c.invalid && (c.dirty || c.touched);
  }

  save(): void {
    // password is mandatory on create only
    if (!this.isEdit() && !this.form.controls.password.value) {
      this.form.controls.password.setErrors({ required: true });
      this.form.controls.password.markAsTouched();
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const raw = this.form.getRawValue();
    const payload = { ...raw, profiles: [...this.selectedProfiles()] };
    const id = this.userId();
    const request$ = this.isEdit() && id !== null ? this.userService.update({ ...payload, id }) : this.userService.create(payload);

    request$.subscribe({
      next: () => {
        this.saving.set(false);
        void this.router.navigate(['/user-management/userList']);
      },
      error: (err: { error?: { error?: string } }) => {
        this.saving.set(false);
        this.error.set(err?.error?.error ?? (this.isEdit() ? 'Could not update the user.' : 'Could not create the user.'));
      },
    });
  }

  cancel(): void {
    void this.router.navigate(['/user-management/userList']);
  }
}
