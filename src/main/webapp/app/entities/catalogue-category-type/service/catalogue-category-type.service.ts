import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICatalogueCategoryType, NewCatalogueCategoryType } from '../catalogue-category-type.model';

export type PartialUpdateCatalogueCategoryType = Partial<ICatalogueCategoryType> & Pick<ICatalogueCategoryType, 'id'>;

@Service()
export class CatalogueCategoryTypesService {
  readonly catalogueCategoryTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly catalogueCategoryTypesResource = httpResource<ICatalogueCategoryType[]>(() => {
    const params = this.catalogueCategoryTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of catalogueCategoryType that have been fetched. It is updated when the catalogueCategoryTypesResource emits a new value.
   * In case of error while fetching the catalogueCategoryTypes, the signal is set to an empty array.
   */
  readonly catalogueCategoryTypes = computed(() =>
    this.catalogueCategoryTypesResource.hasValue() ? this.catalogueCategoryTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/catalogue-category-types`;
}

@Service()
export class CatalogueCategoryTypeService extends CatalogueCategoryTypesService {
  protected readonly http = inject(HttpClient);

  create(catalogueCategoryType: NewCatalogueCategoryType): Observable<ICatalogueCategoryType> {
    return this.http.post<ICatalogueCategoryType>(this.resourceUrl, catalogueCategoryType);
  }

  update(catalogueCategoryType: ICatalogueCategoryType): Observable<ICatalogueCategoryType> {
    return this.http.put<ICatalogueCategoryType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCatalogueCategoryTypeIdentifier(catalogueCategoryType))}`,
      catalogueCategoryType,
    );
  }

  partialUpdate(catalogueCategoryType: PartialUpdateCatalogueCategoryType): Observable<ICatalogueCategoryType> {
    return this.http.patch<ICatalogueCategoryType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCatalogueCategoryTypeIdentifier(catalogueCategoryType))}`,
      catalogueCategoryType,
    );
  }

  find(id: number): Observable<ICatalogueCategoryType> {
    return this.http.get<ICatalogueCategoryType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICatalogueCategoryType[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogueCategoryType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCatalogueCategoryTypeIdentifier(catalogueCategoryType: Pick<ICatalogueCategoryType, 'id'>): number {
    return catalogueCategoryType.id;
  }

  compareCatalogueCategoryType(o1: Pick<ICatalogueCategoryType, 'id'> | null, o2: Pick<ICatalogueCategoryType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCatalogueCategoryTypeIdentifier(o1) === this.getCatalogueCategoryTypeIdentifier(o2) : o1 === o2;
  }

  addCatalogueCategoryTypeToCollectionIfMissing<Type extends Pick<ICatalogueCategoryType, 'id'>>(
    catalogueCategoryTypeCollection: Type[],
    ...catalogueCategoryTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const catalogueCategoryTypes: Type[] = catalogueCategoryTypesToCheck.filter(
      catalogueCategoryTypeItem => catalogueCategoryTypeItem !== null && catalogueCategoryTypeItem !== undefined,
    );
    if (catalogueCategoryTypes.length > 0) {
      const catalogueCategoryTypeCollectionIdentifiers = catalogueCategoryTypeCollection.map(catalogueCategoryTypeItem =>
        this.getCatalogueCategoryTypeIdentifier(catalogueCategoryTypeItem),
      );
      const catalogueCategoryTypesToAdd = catalogueCategoryTypes.filter(catalogueCategoryTypeItem => {
        const catalogueCategoryTypeIdentifier = this.getCatalogueCategoryTypeIdentifier(catalogueCategoryTypeItem);
        if (catalogueCategoryTypeCollectionIdentifiers.includes(catalogueCategoryTypeIdentifier)) {
          return false;
        }
        catalogueCategoryTypeCollectionIdentifiers.push(catalogueCategoryTypeIdentifier);
        return true;
      });
      return [...catalogueCategoryTypesToAdd, ...catalogueCategoryTypeCollection];
    }
    return catalogueCategoryTypeCollection;
  }
}
