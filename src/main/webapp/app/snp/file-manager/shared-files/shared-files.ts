import { ChangeDetectionStrategy, Component, computed, inject, signal, viewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { FileManagerService, FileRow } from '../file-manager.service';
import { FilePreview } from '../file-preview/file-preview';

/**
 * Files other people have shared with you.
 *
 * snp21's version was hard-coded: it listed one folder name, "mallikarjuna-folder", with no
 * call behind it. This reads the real share rows.
 */
@Component({
  selector: 'sys-shared-files',
  templateUrl: './shared-files.html',
  styleUrl: './shared-files.scss',
  imports: [FormsModule, RouterLink, FaIconComponent, DatePipe, FilePreview],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class SharedFiles {
  readonly rows = signal<FileRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly search = signal('');

  private readonly fileService = inject(FileManagerService);
  private readonly preview = viewChild.required<FilePreview>('preview');

  readonly filtered = computed<FileRow[]>(() => {
    const t = this.search().trim().toLowerCase();
    const all = this.rows();
    if (!t) {
      return all;
    }
    return all.filter(r => [r.name, r.sharedBy, r.permission].some(v => v != null && String(v).toLowerCase().includes(t)));
  });

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    // Null asks for every internal share. Once a signed-in user is mapped to their Party
    // this passes that id so the list is theirs alone.
    this.fileService.sharedWithMe(null).subscribe({
      next: rows => {
        this.rows.set(rows);
        this.loading.set(false);
      },
      error: () => {
        this.rows.set([]);
        this.error.set('Could not load the files shared with you.');
        this.loading.set(false);
      },
    });
  }

  openPreview(file: FileRow): void {
    this.preview().show(file);
  }

  download(file: FileRow): void {
    this.fileService.download(file.id).subscribe({
      next: blob => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = file.name ?? 'download';
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => this.error.set('Could not download that file.'),
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
