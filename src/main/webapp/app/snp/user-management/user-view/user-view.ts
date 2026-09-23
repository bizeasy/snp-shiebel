import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { toSignal } from '@angular/core/rxjs-interop';

import { UserManagementService, UserRow } from '../user-management.service';

/** Read-only user details — ported from snp21 view-user. */
@Component({
  selector: 'sys-user-view',
  templateUrl: './user-view.html',
  styleUrl: './user-view.scss',
  imports: [RouterLink, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class UserView {
  readonly user = signal<UserRow | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  private readonly route = inject(ActivatedRoute);
  private readonly userService = inject(UserManagementService);

  private readonly params = toSignal(this.route.queryParams, { initialValue: {} as Record<string, string> });
  readonly userId = computed<number | null>(() => {
    const raw = this.params()['id'];
    return raw ? Number(raw) : null;
  });

  readonly fields = computed(() => {
    const u = this.user();
    return [
      { label: 'Id', value: u?.id },
      { label: 'Login', value: u?.login },
      { label: 'First Name', value: u?.firstName },
      { label: 'Last Name', value: u?.lastName },
      { label: 'Email', value: u?.email },
      { label: 'Language', value: u?.langKey },
      { label: 'Profiles', value: (u?.profile ?? []).join(', ') },
      { label: 'Status', value: u?.status },
    ].map(f => ({ label: f.label, value: f.value == null || f.value === '' ? '—' : String(f.value) }));
  });

  constructor() {
    queueMicrotask(() => this.load());
  }

  private load(): void {
    const id = this.userId();
    if (id === null) {
      this.error.set('No user id supplied.');
      this.loading.set(false);
      return;
    }
    this.userService.getUserById(id).subscribe({
      next: row => {
        this.user.set(row ?? null);
        if (!row) {
          this.error.set('User not found.');
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load this user.');
        this.loading.set(false);
      },
    });
  }
}
