import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';

export interface FolderRow {
  id: number;
  name: string | null;
  path: string | null;
  parentId: number | null;
  owner: string | null;
  createdDate: string | null;
  isSystemFolder: boolean | null;
}

export interface FileRow {
  id: number;
  name: string | null;
  mimeType: string | null;
  fileSize: number | null;
  uploadedDate: string | null;
  folderId: number | null;
  owner: string | null;
  storageProvider: string | null;
  checksum: string | null;
  shareCount: number;
  /** Only present on the shared-with-me list. */
  shareId?: number;
  permission?: string | null;
  sharedBy?: string | null;
  sharedDate?: string | null;
}

export interface ShareRow {
  id: number;
  attachmentId: number | null;
  fileName: string | null;
  permission: string | null;
  sharedWith: string | null;
  sharedWithId: number | null;
  sharedWithEmail: string | null;
  sharedBy: string | null;
  sharedDate: string | null;
  expiryDate: string | null;
  isRevoked: boolean | null;
  downloadCount: number | null;
  lastAccessedDate: string | null;
  /** Returned for external shares only; it is the credential for the public link. */
  accessToken: string | null;
  external: boolean;
}

export type SharePermission = 'VIEW' | 'COMMENT' | 'EDIT' | 'OWNER';

/**
 * Drop Box.
 *
 * snp21 kept the file list in an in-memory array that emptied on reload, its shareFile() was
 * a console.log, and the shared list was hard-coded to one folder name. This talks to real
 * endpoints: uploads are multipart, downloads stream, and shares are rows.
 */
@Injectable({ providedIn: 'root' })
export class FileManagerService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = `${serverApiUrl}api/files`;

  folders(parentId: number | null): Observable<FolderRow[]> {
    let params = new HttpParams();
    if (parentId != null) {
      params = params.set('parentId', parentId);
    }
    return this.http.get<FolderRow[]>(`${this.resourceUrl}/folders`, { params });
  }

  createFolder(name: string, parentId: number | null): Observable<{ success?: FolderRow; error?: unknown }> {
    let params = new HttpParams().set('name', name);
    if (parentId != null) {
      params = params.set('parentId', parentId);
    }
    return this.http.post<{ success?: FolderRow; error?: unknown }>(`${this.resourceUrl}/folders`, null, { params });
  }

  trashFolder(id: number): Observable<unknown> {
    return this.http.delete(`${this.resourceUrl}/folders/${id}`);
  }

  files(folderId: number | null): Observable<FileRow[]> {
    let params = new HttpParams();
    if (folderId != null) {
      params = params.set('folderId', folderId);
    }
    return this.http.get<FileRow[]>(this.resourceUrl, { params });
  }

  /** Multipart upload. The browser sets the boundary, so no Content-Type header here. */
  upload(file: File, folderId: number | null): Observable<{ success?: FileRow; error?: unknown }> {
    const form = new FormData();
    form.append('file', file, file.name);
    let params = new HttpParams();
    if (folderId != null) {
      params = params.set('folderId', folderId);
    }
    return this.http.post<{ success?: FileRow; error?: unknown }>(`${this.resourceUrl}/upload`, form, { params });
  }

  /** Authenticated download. Blob, because the JWT interceptor has to add the header. */
  download(id: number): Observable<Blob> {
    return this.http.get(`${this.resourceUrl}/${id}/download`, { responseType: 'blob' });
  }

  trashFile(id: number): Observable<unknown> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }

  deleteFile(id: number): Observable<unknown> {
    return this.http.delete(`${this.resourceUrl}/${id}/permanent`);
  }

  share(
    attachmentId: number,
    options: { partyId?: number | null; email?: string | null; permission?: SharePermission; expiresInDays?: number | null },
  ): Observable<{ success?: ShareRow; error?: unknown }> {
    let params = new HttpParams();
    if (options.partyId != null) {
      params = params.set('partyId', options.partyId);
    }
    if (options.email) {
      params = params.set('email', options.email);
    }
    if (options.permission) {
      params = params.set('permission', options.permission);
    }
    if (options.expiresInDays != null) {
      params = params.set('expiresInDays', options.expiresInDays);
    }
    return this.http.post<{ success?: ShareRow; error?: unknown }>(`${this.resourceUrl}/${attachmentId}/share`, null, { params });
  }

  sharesOf(attachmentId: number): Observable<ShareRow[]> {
    return this.http.get<ShareRow[]>(`${this.resourceUrl}/${attachmentId}/shares`);
  }

  sharedWithMe(partyId: number | null): Observable<FileRow[]> {
    let params = new HttpParams();
    if (partyId != null) {
      params = params.set('partyId', partyId);
    }
    return this.http.get<FileRow[]>(`${this.resourceUrl}/shared-with-me`, { params });
  }

  revoke(shareId: number): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/shares/${shareId}/revoke`, null);
  }

  // -------------------------------------------------------------------- trash

  trash(): Observable<{ files: FileRow[]; folders: FolderRow[] }> {
    return this.http.get<{ files: FileRow[]; folders: FolderRow[] }>(`${this.resourceUrl}/trash`);
  }

  restoreFile(id: number): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/${id}/restore`, null);
  }

  restoreFolder(id: number): Observable<unknown> {
    return this.http.put(`${this.resourceUrl}/folders/${id}/restore`, null);
  }

  emptyTrash(): Observable<unknown> {
    return this.http.delete(`${this.resourceUrl}/trash`);
  }

  /** The URL an external recipient uses. Absolute, since it is pasted into an email. */
  publicLink(token: string): string {
    return `${window.location.origin}${serverApiUrl}api/public/shares/${token}`;
  }
}
