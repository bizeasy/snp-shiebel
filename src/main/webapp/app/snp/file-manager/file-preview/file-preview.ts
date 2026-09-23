import { ChangeDetectionStrategy, Component, DestroyRef, inject, signal } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

import { FileManagerService, FileRow } from '../file-manager.service';
import { detectPreviewKind, PreviewKind } from './preview-kind';

/**
 * Open a file inside the app rather than always downloading it.
 *
 * PDFs and images render through the browser's own viewer, via a blob: URL - the same
 * bytes the download button would save, just handed to an <iframe>/<img> instead of the
 * disk. Text files are read and shown verbatim; a plain {{ }} binding escapes them, so no
 * sanitizer is needed there. .docx is converted to HTML client-side by mammoth and bound
 * through [innerHTML], which Angular sanitizes by default - mammoth's output is only
 * structural markup (headings, tables, bold/italic), so the default sanitizer is enough
 * without reaching for bypassSecurityTrustHtml.
 *
 * Legacy .doc is not supported: it is a binary OLE format, not XML, and mammoth cannot read
 * it. That is said plainly rather than attempted and left broken.
 *
 * Nothing here talks to a new endpoint - it reuses FileManagerService.download(), the same
 * authenticated call the download button already makes, so a presigned-S3 redirect (once
 * the bucket's CORS allows the browser to follow it) works for preview exactly as it does
 * for download.
 */
@Component({
  selector: 'sys-file-preview',
  templateUrl: './file-preview.html',
  styleUrl: './file-preview.scss',
  imports: [FaIconComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilePreview {
  /** Text files above this are not rendered inline; a huge log can freeze the tab. */
  private static readonly TEXT_PREVIEW_LIMIT = 3 * 1024 * 1024;
  /** docx is parsed fully client-side by mammoth before anything can show. */
  private static readonly DOCX_PREVIEW_LIMIT = 20 * 1024 * 1024;

  readonly file = signal<FileRow | null>(null);
  readonly kind = signal<PreviewKind>('unsupported');
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly tooLarge = signal(false);

  readonly resourceUrl = signal<SafeResourceUrl | null>(null);
  readonly textContent = signal<string | null>(null);
  readonly htmlContent = signal<string | null>(null);

  private readonly fileService = inject(FileManagerService);
  private readonly sanitizer = inject(DomSanitizer);
  private objectUrl: string | null = null;

  constructor() {
    inject(DestroyRef).onDestroy(() => this.revokeObjectUrl());
  }

  /** Open the preview for this file. Called by the file browser and the shared-files list. */
  show(file: FileRow): void {
    this.file.set(file);
    this.error.set(null);
    this.tooLarge.set(false);
    this.textContent.set(null);
    this.htmlContent.set(null);
    this.revokeObjectUrl();
    this.resourceUrl.set(null);

    const kind = detectPreviewKind(file.name, file.mimeType);
    this.kind.set(kind);

    if (kind === 'unsupported' || kind === 'legacy-doc') {
      return;
    }
    if (kind === 'text' && (file.fileSize ?? 0) > FilePreview.TEXT_PREVIEW_LIMIT) {
      this.tooLarge.set(true);
      return;
    }
    if (kind === 'docx' && (file.fileSize ?? 0) > FilePreview.DOCX_PREVIEW_LIMIT) {
      this.tooLarge.set(true);
      return;
    }

    this.loading.set(true);
    this.fileService.download(file.id).subscribe({
      next: blob => this.render(kind, blob),
      error: () => {
        this.loading.set(false);
        this.error.set('Could not load that file.');
      },
    });
  }

  close(): void {
    this.file.set(null);
    this.revokeObjectUrl();
    this.resourceUrl.set(null);
    this.textContent.set(null);
    this.htmlContent.set(null);
  }

  download(): void {
    const file = this.file();
    if (file) {
      this.fileService.download(file.id).subscribe(blob => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = file.name ?? 'download';
        a.click();
        URL.revokeObjectURL(url);
      });
    }
  }

  private render(kind: PreviewKind, blob: Blob): void {
    if (kind === 'pdf' || kind === 'image') {
      this.objectUrl = URL.createObjectURL(blob);
      this.resourceUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(this.objectUrl));
      this.loading.set(false);
      return;
    }

    if (kind === 'text') {
      blob
        .text()
        .then(text => {
          this.textContent.set(text);
          this.loading.set(false);
        })
        .catch(() => {
          this.loading.set(false);
          this.error.set('Could not read that file as text.');
        });
      return;
    }

    if (kind === 'docx') {
      blob
        .arrayBuffer()
        .then(async arrayBuffer => {
          // Loaded on demand rather than as a top-level import: nobody previewing a PDF or
          // a text file should pay for mammoth's ~150KB in their bundle.
          //
          // mammoth is CJS with plain `exports.foo = ...` assignments. A dynamic import()
          // of a CJS module without an __esModule marker wraps the whole exports object
          // under .default rather than spreading it onto the namespace, so
          // `mammoth.convertToHtml` is undefined - only `mammoth.default.convertToHtml`
          // exists. Reading whichever one actually has it keeps this working regardless of
          // which way a given bundler chooses to interop it.
          const imported = await import('mammoth');
          const mammoth = ((imported as { default?: unknown }).default ?? imported) as {
            convertToHtml: (input: { arrayBuffer: ArrayBuffer }) => Promise<{ value: string }>;
          };
          const result = await mammoth.convertToHtml({ arrayBuffer });
          this.htmlContent.set(result.value);
          this.loading.set(false);
        })
        .catch(() => {
          this.loading.set(false);
          this.error.set('Could not open that document. It may not be a valid .docx file.');
        });
    }
  }

  private revokeObjectUrl(): void {
    if (this.objectUrl) {
      URL.revokeObjectURL(this.objectUrl);
      this.objectUrl = null;
    }
  }
}
