import { ChangeDetectionStrategy, Component, computed, inject, signal, viewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { CustomerRow, CustomerService } from '../../sales-management/customer.service';
import { FileManagerService, FileRow, FolderRow, SharePermission, ShareRow } from '../file-manager.service';
import { FilePreview } from '../file-preview/file-preview';

/** One step in the breadcrumb trail. */
interface Crumb {
  id: number | null;
  name: string;
}

/**
 * Drop Box: browse folders, upload, download, share.
 *
 * snp21's version held everything in an in-memory array, so a reload emptied it, and its
 * share button only logged to the console. Every action here goes to the server.
 */
@Component({
  selector: 'sys-file-browser',
  templateUrl: './file-browser.html',
  styleUrl: './file-browser.scss',
  imports: [FormsModule, RouterLink, NgbDropdownModule, FaIconComponent, DatePipe, FilePreview],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class FileBrowser {
  readonly folders = signal<FolderRow[]>([]);
  readonly files = signal<FileRow[]>([]);
  readonly loading = signal(false);
  readonly uploading = signal(false);
  readonly error = signal<string | null>(null);
  readonly notice = signal<string | null>(null);
  readonly search = signal('');

  readonly currentFolder = signal<number | null>(null);
  readonly trail = signal<Crumb[]>([{ id: null, name: 'My Files' }]);

  readonly newFolderName = signal('');
  readonly showNewFolder = signal(false);

  // share panel
  readonly shareFor = signal<FileRow | null>(null);
  readonly shares = signal<ShareRow[]>([]);
  readonly shareEmail = signal('');
  readonly sharePartyId = signal<number | null>(null);
  readonly sharePermission = signal<SharePermission>('VIEW');
  readonly shareExpiryDays = signal<number>(14);
  readonly sharing = signal(false);
  readonly copiedToken = signal<string | null>(null);
  readonly people = signal<CustomerRow[]>([]);

  private readonly fileService = inject(FileManagerService);
  private readonly customerService = inject(CustomerService);
  private readonly preview = viewChild.required<FilePreview>('preview');

  readonly filteredFolders = computed(() => {
    const t = this.search().trim().toLowerCase();
    return t ? this.folders().filter(f => (f.name ?? '').toLowerCase().includes(t)) : this.folders();
  });

  readonly filteredFiles = computed(() => {
    const t = this.search().trim().toLowerCase();
    return t ? this.files().filter(f => (f.name ?? '').toLowerCase().includes(t)) : this.files();
  });

  readonly isEmpty = computed(() => !this.loading() && this.filteredFolders().length === 0 && this.filteredFiles().length === 0);

  constructor() {
    this.customerService.getAllCustomersList(0, 500).subscribe({ next: r => this.people.set(r.rows) });
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    const folderId = this.currentFolder();
    this.fileService.folders(folderId).subscribe({
      next: rows => this.folders.set(rows),
      error: () => this.error.set('Could not load folders.'),
    });
    this.fileService.files(folderId).subscribe({
      next: rows => {
        this.files.set(rows);
        this.loading.set(false);
      },
      error: () => {
        this.files.set([]);
        this.error.set('Could not load files.');
        this.loading.set(false);
      },
    });
  }

  openFolder(folder: FolderRow): void {
    this.currentFolder.set(folder.id);
    this.trail.update(t => [...t, { id: folder.id, name: folder.name ?? 'Folder' }]);
    this.load();
  }

  goTo(index: number): void {
    const trail = this.trail().slice(0, index + 1);
    this.trail.set(trail);
    this.currentFolder.set(trail[trail.length - 1].id);
    this.load();
  }

  createFolder(): void {
    const name = this.newFolderName().trim();
    if (!name) {
      return;
    }
    this.fileService.createFolder(name, this.currentFolder()).subscribe({
      next: body => {
        if (body.error) {
          this.error.set(String(body.error));
          return;
        }
        this.newFolderName.set('');
        this.showNewFolder.set(false);
        this.notice.set(`Folder ${name} created.`);
        this.load();
      },
      error: err => this.error.set(err?.error?.error ? String(err.error.error) : 'Could not create the folder.'),
    });
  }

  trashFolder(folder: FolderRow): void {
    this.fileService.trashFolder(folder.id).subscribe({
      next: () => {
        this.notice.set(`${folder.name} moved to trash.`);
        this.load();
      },
      error: () => this.error.set('Could not trash that folder.'),
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }
    this.uploading.set(true);
    this.error.set(null);
    this.fileService.upload(file, this.currentFolder()).subscribe({
      next: body => {
        this.uploading.set(false);
        input.value = '';
        if (body.error) {
          this.error.set(String(body.error));
          return;
        }
        this.notice.set(`${file.name} uploaded.`);
        this.load();
      },
      error: err => {
        this.uploading.set(false);
        input.value = '';
        this.error.set(err?.status === 413 ? 'That file is larger than the 50 MB limit.' : 'Could not upload that file.');
      },
    });
  }

  /**
   * Download through the API so the JWT goes with it, then hand the blob to the browser.
   * A plain link would arrive without the Authorization header and be rejected.
   */
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

  trashFile(file: FileRow): void {
    this.fileService.trashFile(file.id).subscribe({
      next: () => {
        this.notice.set(`${file.name} moved to trash.`);
        this.load();
      },
      error: () => this.error.set('Could not trash that file.'),
    });
  }

  // ------------------------------------------------------------------- sharing

  openShare(file: FileRow): void {
    this.shareFor.set(file);
    this.shareEmail.set('');
    this.sharePartyId.set(null);
    this.sharePermission.set('VIEW');
    this.shareExpiryDays.set(14);
    this.copiedToken.set(null);
    this.loadShares(file.id);
  }

  closeShare(): void {
    this.shareFor.set(null);
    this.shares.set([]);
  }

  private loadShares(attachmentId: number): void {
    this.fileService.sharesOf(attachmentId).subscribe({
      next: rows => this.shares.set(rows),
      error: () => this.shares.set([]),
    });
  }

  shareInternally(): void {
    const file = this.shareFor();
    const partyId = this.sharePartyId();
    if (!file || partyId == null) {
      return;
    }
    this.submitShare(file.id, { partyId, permission: this.sharePermission() });
  }

  shareByEmail(): void {
    const file = this.shareFor();
    const email = this.shareEmail().trim();
    if (!file || !email) {
      return;
    }
    this.submitShare(file.id, { email, permission: this.sharePermission(), expiresInDays: this.shareExpiryDays() });
  }

  private submitShare(id: number, options: Parameters<FileManagerService['share']>[1]): void {
    this.sharing.set(true);
    this.error.set(null);
    this.fileService.share(id, options).subscribe({
      next: body => {
        this.sharing.set(false);
        if (body.error) {
          this.error.set(String(body.error));
          return;
        }
        this.shareEmail.set('');
        this.sharePartyId.set(null);
        this.notice.set('Shared.');
        this.loadShares(id);
        this.load();
      },
      error: err => {
        this.sharing.set(false);
        this.error.set(err?.error?.error ? String(err.error.error) : 'Could not share that file.');
      },
    });
  }

  revoke(share: ShareRow): void {
    this.fileService.revoke(share.id).subscribe({
      next: () => {
        this.notice.set('Share revoked.');
        const file = this.shareFor();
        if (file) {
          this.loadShares(file.id);
        }
        this.load();
      },
      error: () => this.error.set('Could not revoke that share.'),
    });
  }

  linkFor(share: ShareRow): string {
    return share.accessToken ? this.fileService.publicLink(share.accessToken) : '';
  }

  copyLink(share: ShareRow): void {
    const link = this.linkFor(share);
    if (!link) {
      return;
    }
    void navigator.clipboard.writeText(link).then(
      () => this.copiedToken.set(share.accessToken),
      () => this.error.set('Could not copy the link.'),
    );
  }

  /** Bytes as something readable; file sizes are shown to people, not parsed. */
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

  icon(file: FileRow): string {
    const type = (file.mimeType ?? '').toLowerCase();
    if (type.startsWith('image/')) {
      return 'eye';
    }
    if (type.includes('pdf')) {
      return 'file-invoice';
    }
    return 'book';
  }
}
