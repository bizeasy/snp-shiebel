import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IContentType, NewContentType } from '../content-type.model';

export type PartialUpdateContentType = Partial<IContentType> & Pick<IContentType, 'id'>;

@Service()
export class ContentTypesService {
  readonly contentTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly contentTypesResource = httpResource<IContentType[]>(() => {
    const params = this.contentTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of contentType that have been fetched. It is updated when the contentTypesResource emits a new value.
   * In case of error while fetching the contentTypes, the signal is set to an empty array.
   */
  readonly contentTypes = computed(() => (this.contentTypesResource.hasValue() ? this.contentTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/content-types`;
}

@Service()
export class ContentTypeService extends ContentTypesService {
  protected readonly http = inject(HttpClient);

  create(contentType: NewContentType): Observable<IContentType> {
    return this.http.post<IContentType>(this.resourceUrl, contentType);
  }

  update(contentType: IContentType): Observable<IContentType> {
    return this.http.put<IContentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContentTypeIdentifier(contentType))}`,
      contentType,
    );
  }

  partialUpdate(contentType: PartialUpdateContentType): Observable<IContentType> {
    return this.http.patch<IContentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getContentTypeIdentifier(contentType))}`,
      contentType,
    );
  }

  find(id: number): Observable<IContentType> {
    return this.http.get<IContentType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IContentType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IContentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getContentTypeIdentifier(contentType: Pick<IContentType, 'id'>): number {
    return contentType.id;
  }

  compareContentType(o1: Pick<IContentType, 'id'> | null, o2: Pick<IContentType, 'id'> | null): boolean {
    return o1 && o2 ? this.getContentTypeIdentifier(o1) === this.getContentTypeIdentifier(o2) : o1 === o2;
  }

  addContentTypeToCollectionIfMissing<Type extends Pick<IContentType, 'id'>>(
    contentTypeCollection: Type[],
    ...contentTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contentTypes: Type[] = contentTypesToCheck.filter(contentTypeItem => contentTypeItem !== null && contentTypeItem !== undefined);
    if (contentTypes.length > 0) {
      const contentTypeCollectionIdentifiers = contentTypeCollection.map(contentTypeItem => this.getContentTypeIdentifier(contentTypeItem));
      const contentTypesToAdd = contentTypes.filter(contentTypeItem => {
        const contentTypeIdentifier = this.getContentTypeIdentifier(contentTypeItem);
        if (contentTypeCollectionIdentifiers.includes(contentTypeIdentifier)) {
          return false;
        }
        contentTypeCollectionIdentifiers.push(contentTypeIdentifier);
        return true;
      });
      return [...contentTypesToAdd, ...contentTypeCollection];
    }
    return contentTypeCollection;
  }
}
