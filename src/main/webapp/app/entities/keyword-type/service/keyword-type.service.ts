import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IKeywordType, NewKeywordType } from '../keyword-type.model';

export type PartialUpdateKeywordType = Partial<IKeywordType> & Pick<IKeywordType, 'id'>;

@Service()
export class KeywordTypesService {
  readonly keywordTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly keywordTypesResource = httpResource<IKeywordType[]>(() => {
    const params = this.keywordTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of keywordType that have been fetched. It is updated when the keywordTypesResource emits a new value.
   * In case of error while fetching the keywordTypes, the signal is set to an empty array.
   */
  readonly keywordTypes = computed(() => (this.keywordTypesResource.hasValue() ? this.keywordTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/keyword-types`;
}

@Service()
export class KeywordTypeService extends KeywordTypesService {
  protected readonly http = inject(HttpClient);

  create(keywordType: NewKeywordType): Observable<IKeywordType> {
    return this.http.post<IKeywordType>(this.resourceUrl, keywordType);
  }

  update(keywordType: IKeywordType): Observable<IKeywordType> {
    return this.http.put<IKeywordType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getKeywordTypeIdentifier(keywordType))}`,
      keywordType,
    );
  }

  partialUpdate(keywordType: PartialUpdateKeywordType): Observable<IKeywordType> {
    return this.http.patch<IKeywordType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getKeywordTypeIdentifier(keywordType))}`,
      keywordType,
    );
  }

  find(id: number): Observable<IKeywordType> {
    return this.http.get<IKeywordType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IKeywordType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IKeywordType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getKeywordTypeIdentifier(keywordType: Pick<IKeywordType, 'id'>): number {
    return keywordType.id;
  }

  compareKeywordType(o1: Pick<IKeywordType, 'id'> | null, o2: Pick<IKeywordType, 'id'> | null): boolean {
    return o1 && o2 ? this.getKeywordTypeIdentifier(o1) === this.getKeywordTypeIdentifier(o2) : o1 === o2;
  }

  addKeywordTypeToCollectionIfMissing<Type extends Pick<IKeywordType, 'id'>>(
    keywordTypeCollection: Type[],
    ...keywordTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const keywordTypes: Type[] = keywordTypesToCheck.filter(keywordTypeItem => keywordTypeItem !== null && keywordTypeItem !== undefined);
    if (keywordTypes.length > 0) {
      const keywordTypeCollectionIdentifiers = keywordTypeCollection.map(keywordTypeItem => this.getKeywordTypeIdentifier(keywordTypeItem));
      const keywordTypesToAdd = keywordTypes.filter(keywordTypeItem => {
        const keywordTypeIdentifier = this.getKeywordTypeIdentifier(keywordTypeItem);
        if (keywordTypeCollectionIdentifiers.includes(keywordTypeIdentifier)) {
          return false;
        }
        keywordTypeCollectionIdentifiers.push(keywordTypeIdentifier);
        return true;
      });
      return [...keywordTypesToAdd, ...keywordTypeCollection];
    }
    return keywordTypeCollection;
  }
}
