import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFileShare, NewFileShare } from '../file-share.model';

export type PartialUpdateFileShare = Partial<IFileShare> & Pick<IFileShare, 'id'>;

type RestOf<T extends IFileShare | NewFileShare> = Omit<T, 'sharedDate' | 'expiryDate'> & {
  sharedDate?: string | null;
  expiryDate?: string | null;
};

export type RestFileShare = RestOf<IFileShare>;

export type NewRestFileShare = RestOf<NewFileShare>;

export type PartialUpdateRestFileShare = RestOf<PartialUpdateFileShare>;

@Service()
export class FileSharesService {
  readonly fileSharesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly fileSharesResource = httpResource<RestFileShare[]>(() => {
    const params = this.fileSharesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fileShare that have been fetched. It is updated when the fileSharesResource emits a new value.
   * In case of error while fetching the fileShares, the signal is set to an empty array.
   */
  readonly fileShares = computed(() =>
    (this.fileSharesResource.hasValue() ? this.fileSharesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/file-shares`;

  protected convertValueFromServer(restFileShare: RestFileShare): IFileShare {
    return {
      ...restFileShare,
      sharedDate: restFileShare.sharedDate ? dayjs(restFileShare.sharedDate) : undefined,
      expiryDate: restFileShare.expiryDate ? dayjs(restFileShare.expiryDate) : undefined,
    };
  }
}

@Service()
export class FileShareService extends FileSharesService {
  protected readonly http = inject(HttpClient);

  create(fileShare: NewFileShare): Observable<IFileShare> {
    const copy = this.convertValueFromClient(fileShare);
    return this.http.post<RestFileShare>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fileShare: IFileShare): Observable<IFileShare> {
    const copy = this.convertValueFromClient(fileShare);
    return this.http
      .put<RestFileShare>(`${this.resourceUrl}/${encodeURIComponent(this.getFileShareIdentifier(fileShare))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fileShare: PartialUpdateFileShare): Observable<IFileShare> {
    const copy = this.convertValueFromClient(fileShare);
    return this.http
      .patch<RestFileShare>(`${this.resourceUrl}/${encodeURIComponent(this.getFileShareIdentifier(fileShare))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFileShare> {
    return this.http
      .get<RestFileShare>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFileShare[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFileShare[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFileShareIdentifier(fileShare: Pick<IFileShare, 'id'>): number {
    return fileShare.id;
  }

  compareFileShare(o1: Pick<IFileShare, 'id'> | null, o2: Pick<IFileShare, 'id'> | null): boolean {
    return o1 && o2 ? this.getFileShareIdentifier(o1) === this.getFileShareIdentifier(o2) : o1 === o2;
  }

  addFileShareToCollectionIfMissing<Type extends Pick<IFileShare, 'id'>>(
    fileShareCollection: Type[],
    ...fileSharesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fileShares: Type[] = fileSharesToCheck.filter(fileShareItem => fileShareItem !== null && fileShareItem !== undefined);
    if (fileShares.length > 0) {
      const fileShareCollectionIdentifiers = fileShareCollection.map(fileShareItem => this.getFileShareIdentifier(fileShareItem));
      const fileSharesToAdd = fileShares.filter(fileShareItem => {
        const fileShareIdentifier = this.getFileShareIdentifier(fileShareItem);
        if (fileShareCollectionIdentifiers.includes(fileShareIdentifier)) {
          return false;
        }
        fileShareCollectionIdentifiers.push(fileShareIdentifier);
        return true;
      });
      return [...fileSharesToAdd, ...fileShareCollection];
    }
    return fileShareCollection;
  }

  protected convertValueFromClient<T extends IFileShare | NewFileShare | PartialUpdateFileShare>(fileShare: T): RestOf<T> {
    return {
      ...fileShare,
      sharedDate: fileShare.sharedDate?.toJSON() ?? null,
      expiryDate: fileShare.expiryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFileShare): IFileShare {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFileShare[]): IFileShare[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
