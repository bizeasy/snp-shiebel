import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { UserManagementService, UserRow } from '../user-management.service';

/**
 * User list — ported from snp21 snp/user-management/user-list.
 *
 * Row actions are View, Edit and Activate/Deactivate, as in snp21; the activate and
 * deactivate entries are shown only when they would change something.
 */
@Component({
  selector: 'sys-user-list',
  templateUrl: './user-list.html',
  styleUrl: './user-list.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class UserList {
  readonly rows = signal<UserRow[]>([]);
  readonly totalRecords = signal(0);
  readonly loading = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly search = signal('');

  readonly page = signal(0);
  readonly size = signal(10);
  readonly sizeOptions = [10, 25, 50, 100, 250];

  private readonly userService = inject(UserManagementService);

  readonly filteredRows = computed<UserRow[]>(() => {
    const term = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!term) {
      return all;
    }
    return all.filter(r =>
      [r.id, r.login, r.firstName, r.lastName, r.email, r.status, (r.profile ?? []).join(' ')].some(
        v => v != null && String(v).toLowerCase().includes(term),
      ),
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
    this.userService.getAllUserList(this.page(), this.size()).subscribe({
      next: result => {
        this.rows.set(result.rows);
        this.totalRecords.set(result.totalRecords);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.totalRecords.set(0);
        this.error.set('Could not load the user list.');
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

  setActive(row: UserRow, active: boolean): void {
    this.busy.set(true);
    this.notice.set(null);
    this.userService.setActive(row.id, active).subscribe({
      next: () => {
        this.busy.set(false);
        this.notice.set(`${row.login} is now ${active ? 'active' : 'inactive'}.`);
        this.load();
      },
      error: () => {
        this.busy.set(false);
        this.error.set(`Could not ${active ? 'activate' : 'deactivate'} ${row.login}.`);
      },
    });
  }

  profiles(row: UserRow): string {
    return (row.profile ?? []).join(', ') || '—';
  }
}
