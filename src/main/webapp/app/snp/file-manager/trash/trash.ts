import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { FileManagerService, FileRow, FolderRow } from '../file-manager.service';

/**
 * Trash.
 *
 * Trashing has always flagged rows rather than erasing them, so everything in here is
 * recoverable - this is the screen that lets someone actually recover it. Emptying is the
 * only place in the file manager that destroys anything, so it asks first and says how much
 * it is about to remove.
 */
@Component({
  selector: 'sys-trash',
  templateUrl: './trash.html',
  styleUrl: './trash.scss',
  imports: [RouterLink, FaIconComponent, DatePipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Trash {
  readonly files = signal<FileRow[]>([]);
  readonly folders = signal<FolderRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly confirmingEmpty = signal(false);

  private readonly fileService = inject(FileManagerService);

  readonly isEmpty = computed(() => !this.loading() && this.files().length === 0 && this.folders().length === 0);
  readonly total = computed(() => this.files().length + this.folders().length);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.fileService.trash().subscribe({
      next: body => {
        this.files.set(body.files ?? []);
        this.folders.set(body.folders ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.files.set([]);
        this.folders.set([]);
        this.error.set('Could not load the trash.');
        this.loading.set(false);
      },
    });
  }

  restoreFile(file: FileRow): void {
    this.fileService.restoreFile(file.id).subscribe({
      next: () => {
        this.notice.set(`${file.name} restored.`);
        this.load();
      },
      error: () => this.error.set('Could not restore that file.'),
    });
  }

  restoreFolder(folder: FolderRow): void {
    this.fileService.restoreFolder(folder.id).subscribe({
      next: () => {
        this.notice.set(`${folder.name} restored with everything inside it.`);
        this.load();
      },
      error: () => this.error.set('Could not restore that folder.'),
    });
  }

  emptyTrash(): void {
    this.fileService.emptyTrash().subscribe({
      next: () => {
        this.confirmingEmpty.set(false);
        this.notice.set('Trash emptied.');
        this.load();
      },
      error: () => {
        this.confirmingEmpty.set(false);
        this.error.set('Could not empty the trash.');
      },
    });
  }

  size(bytes: number | null): string {
    if (bytes == null) {
      return '—';
    }
    const units = ['B', 'KB', 'MB', 'GB'];
    let value = bytes;
    let unit = 0;
    while (value >= 1024 && unit < units.length - 1) {
      value /= 1024;
      unit++;
    }
    return `${unit === 0 ? value : value.toFixed(1)} ${units[unit]}`;
  }
}
