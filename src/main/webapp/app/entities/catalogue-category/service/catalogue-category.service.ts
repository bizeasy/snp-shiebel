import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICatalogueCategory, NewCatalogueCategory } from '../catalogue-category.model';

export type PartialUpdateCatalogueCategory = Partial<ICatalogueCategory> & Pick<ICatalogueCategory, 'id'>;

@Service()
export class CatalogueCategoriesService {
  readonly catalogueCategoriesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly catalogueCategoriesResource = httpResource<ICatalogueCategory[]>(() => {
    const params = this.catalogueCategoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of catalogueCategory that have been fetched. It is updated when the catalogueCategoriesResource emits a new value.
   * In case of error while fetching the catalogueCategories, the signal is set to an empty array.
   */
  readonly catalogueCategories = computed(() =>
    this.catalogueCategoriesResource.hasValue() ? this.catalogueCategoriesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/catalogue-categories`;
}

@Service()
export class CatalogueCategoryService extends CatalogueCategoriesService {
  protected readonly http = inject(HttpClient);

  create(catalogueCategory: NewCatalogueCategory): Observable<ICatalogueCategory> {
    return this.http.post<ICatalogueCategory>(this.resourceUrl, catalogueCategory);
  }

  update(catalogueCategory: ICatalogueCategory): Observable<ICatalogueCategory> {
    return this.http.put<ICatalogueCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCatalogueCategoryIdentifier(catalogueCategory))}`,
      catalogueCategory,
    );
  }

  partialUpdate(catalogueCategory: PartialUpdateCatalogueCategory): Observable<ICatalogueCategory> {
    return this.http.patch<ICatalogueCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCatalogueCategoryIdentifier(catalogueCategory))}`,
      catalogueCategory,
    );
  }

  find(id: number): Observable<ICatalogueCategory> {
    return this.http.get<ICatalogueCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICatalogueCategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogueCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCatalogueCategoryIdentifier(catalogueCategory: Pick<ICatalogueCategory, 'id'>): number {
    return catalogueCategory.id;
  }

  compareCatalogueCategory(o1: Pick<ICatalogueCategory, 'id'> | null, o2: Pick<ICatalogueCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getCatalogueCategoryIdentifier(o1) === this.getCatalogueCategoryIdentifier(o2) : o1 === o2;
  }

  addCatalogueCategoryToCollectionIfMissing<Type extends Pick<ICatalogueCategory, 'id'>>(
    catalogueCategoryCollection: Type[],
    ...catalogueCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const catalogueCategories: Type[] = catalogueCategoriesToCheck.filter(
      catalogueCategoryItem => catalogueCategoryItem !== null && catalogueCategoryItem !== undefined,
    );
    if (catalogueCategories.length > 0) {
      const catalogueCategoryCollectionIdentifiers = catalogueCategoryCollection.map(catalogueCategoryItem =>
        this.getCatalogueCategoryIdentifier(catalogueCategoryItem),
      );
      const catalogueCategoriesToAdd = catalogueCategories.filter(catalogueCategoryItem => {
        const catalogueCategoryIdentifier = this.getCatalogueCategoryIdentifier(catalogueCategoryItem);
        if (catalogueCategoryCollectionIdentifiers.includes(catalogueCategoryIdentifier)) {
          return false;
        }
        catalogueCategoryCollectionIdentifiers.push(catalogueCategoryIdentifier);
        return true;
      });
      return [...catalogueCategoriesToAdd, ...catalogueCategoryCollection];
    }
    return catalogueCategoryCollection;
  }
}
