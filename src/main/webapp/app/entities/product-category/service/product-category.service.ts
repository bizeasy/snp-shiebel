import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductCategory, NewProductCategory } from '../product-category.model';

export type PartialUpdateProductCategory = Partial<IProductCategory> & Pick<IProductCategory, 'id'>;

@Service()
export class ProductCategoriesService {
  readonly productCategoriesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productCategoriesResource = httpResource<IProductCategory[]>(() => {
    const params = this.productCategoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productCategory that have been fetched. It is updated when the productCategoriesResource emits a new value.
   * In case of error while fetching the productCategories, the signal is set to an empty array.
   */
  readonly productCategories = computed(() => (this.productCategoriesResource.hasValue() ? this.productCategoriesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-categories`;
}

@Service()
export class ProductCategoryService extends ProductCategoriesService {
  protected readonly http = inject(HttpClient);

  create(productCategory: NewProductCategory): Observable<IProductCategory> {
    return this.http.post<IProductCategory>(this.resourceUrl, productCategory);
  }

  update(productCategory: IProductCategory): Observable<IProductCategory> {
    return this.http.put<IProductCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductCategoryIdentifier(productCategory))}`,
      productCategory,
    );
  }

  partialUpdate(productCategory: PartialUpdateProductCategory): Observable<IProductCategory> {
    return this.http.patch<IProductCategory>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductCategoryIdentifier(productCategory))}`,
      productCategory,
    );
  }

  find(id: number): Observable<IProductCategory> {
    return this.http.get<IProductCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductCategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductCategoryIdentifier(productCategory: Pick<IProductCategory, 'id'>): number {
    return productCategory.id;
  }

  compareProductCategory(o1: Pick<IProductCategory, 'id'> | null, o2: Pick<IProductCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductCategoryIdentifier(o1) === this.getProductCategoryIdentifier(o2) : o1 === o2;
  }

  addProductCategoryToCollectionIfMissing<Type extends Pick<IProductCategory, 'id'>>(
    productCategoryCollection: Type[],
    ...productCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productCategories: Type[] = productCategoriesToCheck.filter(
      productCategoryItem => productCategoryItem !== null && productCategoryItem !== undefined,
    );
    if (productCategories.length > 0) {
      const productCategoryCollectionIdentifiers = productCategoryCollection.map(productCategoryItem =>
        this.getProductCategoryIdentifier(productCategoryItem),
      );
      const productCategoriesToAdd = productCategories.filter(productCategoryItem => {
        const productCategoryIdentifier = this.getProductCategoryIdentifier(productCategoryItem);
        if (productCategoryCollectionIdentifiers.includes(productCategoryIdentifier)) {
          return false;
        }
        productCategoryCollectionIdentifiers.push(productCategoryIdentifier);
        return true;
      });
      return [...productCategoriesToAdd, ...productCategoryCollection];
    }
    return productCategoryCollection;
  }
}
