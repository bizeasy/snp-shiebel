import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureCategory, NewProductFeatureCategory } from '../product-feature-category.model';

export type PartialUpdateProductFeatureCategory = Partial<IProductFeatureCategory> & Pick<IProductFeatureCategory, 'id'>;

@Service()
export class ProductFeatureCategoriesService {
  readonly productFeatureCategoriesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureCategoriesResource = httpResource<IProductFeatureCategory[]>(() => {
    const params = this.productFeatureCategoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureCategory that have been fetched. It is updated when the productFeatureCategoriesResource emits a new value.
   * In case of error while fetching the productFeatureCategories, the signal is set to an empty array.
   */
  readonly productFeatureCategories = computed(() =>
    this.productFeatureCategoriesResource.hasValue() ? this.productFeatureCategoriesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-categories`;
}

@Service()
export class ProductFeatureCategoryService extends ProductFeatureCategoriesService {
  protected readonly http = inject(HttpClient);

  create(productFeatureCategory: NewProductFeatureCategory): Observable<IProductFeatureCategory> {
    return this.http.post<IProductFeatureCategory>(this.resourceUrl, productFeatureCategory);
  }

  update(productFeatureCategory: IProductFeatureCategory): Observable<IProductFeatureCategory> {
    return this.http.put<IProductFeatureCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureCategoryIdentifier(productFeatureCategory))}`,
      productFeatureCategory,
    );
  }

  partialUpdate(productFeatureCategory: PartialUpdateProductFeatureCategory): Observable<IProductFeatureCategory> {
    return this.http.patch<IProductFeatureCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureCategoryIdentifier(productFeatureCategory))}`,
      productFeatureCategory,
    );
  }

  find(id: number): Observable<IProductFeatureCategory> {
    return this.http.get<IProductFeatureCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureCategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFeatureCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureCategoryIdentifier(productFeatureCategory: Pick<IProductFeatureCategory, 'id'>): number {
    return productFeatureCategory.id;
  }

  compareProductFeatureCategory(o1: Pick<IProductFeatureCategory, 'id'> | null, o2: Pick<IProductFeatureCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFeatureCategoryIdentifier(o1) === this.getProductFeatureCategoryIdentifier(o2) : o1 === o2;
  }

  addProductFeatureCategoryToCollectionIfMissing<Type extends Pick<IProductFeatureCategory, 'id'>>(
    productFeatureCategoryCollection: Type[],
    ...productFeatureCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureCategories: Type[] = productFeatureCategoriesToCheck.filter(
      productFeatureCategoryItem => productFeatureCategoryItem !== null && productFeatureCategoryItem !== undefined,
    );
    if (productFeatureCategories.length > 0) {
      const productFeatureCategoryCollectionIdentifiers = productFeatureCategoryCollection.map(productFeatureCategoryItem =>
        this.getProductFeatureCategoryIdentifier(productFeatureCategoryItem),
      );
      const productFeatureCategoriesToAdd = productFeatureCategories.filter(productFeatureCategoryItem => {
        const productFeatureCategoryIdentifier = this.getProductFeatureCategoryIdentifier(productFeatureCategoryItem);
        if (productFeatureCategoryCollectionIdentifiers.includes(productFeatureCategoryIdentifier)) {
          return false;
        }
        productFeatureCategoryCollectionIdentifiers.push(productFeatureCategoryIdentifier);
        return true;
      });
      return [...productFeatureCategoriesToAdd, ...productFeatureCategoryCollection];
    }
    return productFeatureCategoryCollection;
  }
}
