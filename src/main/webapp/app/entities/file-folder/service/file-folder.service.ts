import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFileFolder, NewFileFolder } from '../file-folder.model';

export type PartialUpdateFileFolder = Partial<IFileFolder> & Pick<IFileFolder, 'id'>;

type RestOf<T extends IFileFolder | NewFileFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestFileFolder = RestOf<IFileFolder>;

export type NewRestFileFolder = RestOf<NewFileFolder>;

export type PartialUpdateRestFileFolder = RestOf<PartialUpdateFileFolder>;

@Service()
export class FileFoldersService {
  readonly fileFoldersParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly fileFoldersResource = httpResource<RestFileFolder[]>(() => {
    const params = this.fileFoldersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fileFolder that have been fetched. It is updated when the fileFoldersResource emits a new value.
   * In case of error while fetching the fileFolders, the signal is set to an empty array.
   */
  readonly fileFolders = computed(() =>
    (this.fileFoldersResource.hasValue() ? this.fileFoldersResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/file-folders`;

  protected convertValueFromServer(restFileFolder: RestFileFolder): IFileFolder {
    return {
      ...restFileFolder,
      createdDate: restFileFolder.createdDate ? dayjs(restFileFolder.createdDate) : undefined,
    };
  }
}

@Service()
export class FileFolderService extends FileFoldersService {
  protected readonly http = inject(HttpClient);

  create(fileFolder: NewFileFolder): Observable<IFileFolder> {
    const copy = this.convertValueFromClient(fileFolder);
    return this.http.post<RestFileFolder>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fileFolder: IFileFolder): Observable<IFileFolder> {
    const copy = this.convertValueFromClient(fileFolder);
    return this.http
      .put<RestFileFolder>(`${this.resourceUrl}/${encodeURIComponent(this.getFileFolderIdentifier(fileFolder))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fileFolder: PartialUpdateFileFolder): Observable<IFileFolder> {
    const copy = this.convertValueFromClient(fileFolder);
    return this.http
      .patch<RestFileFolder>(`${this.resourceUrl}/${encodeURIComponent(this.getFileFolderIdentifier(fileFolder))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFileFolder> {
    return this.http
      .get<RestFileFolder>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFileFolder[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFileFolder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFileFolderIdentifier(fileFolder: Pick<IFileFolder, 'id'>): number {
    return fileFolder.id;
  }

  compareFileFolder(o1: Pick<IFileFolder, 'id'> | null, o2: Pick<IFileFolder, 'id'> | null): boolean {
    return o1 && o2 ? this.getFileFolderIdentifier(o1) === this.getFileFolderIdentifier(o2) : o1 === o2;
  }

  addFileFolderToCollectionIfMissing<Type extends Pick<IFileFolder, 'id'>>(
    fileFolderCollection: Type[],
    ...fileFoldersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fileFolders: Type[] = fileFoldersToCheck.filter(fileFolderItem => fileFolderItem !== null && fileFolderItem !== undefined);
    if (fileFolders.length > 0) {
      const fileFolderCollectionIdentifiers = fileFolderCollection.map(fileFolderItem => this.getFileFolderIdentifier(fileFolderItem));
      const fileFoldersToAdd = fileFolders.filter(fileFolderItem => {
        const fileFolderIdentifier = this.getFileFolderIdentifier(fileFolderItem);
        if (fileFolderCollectionIdentifiers.includes(fileFolderIdentifier)) {
          return false;
        }
        fileFolderCollectionIdentifiers.push(fileFolderIdentifier);
        return true;
      });
      return [...fileFoldersToAdd, ...fileFolderCollection];
    }
    return fileFolderCollection;
  }

  protected convertValueFromClient<T extends IFileFolder | NewFileFolder | PartialUpdateFileFolder>(fileFolder: T): RestOf<T> {
    return {
      ...fileFolder,
      createdDate: fileFolder.createdDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFileFolder): IFileFolder {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFileFolder[]): IFileFolder[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
