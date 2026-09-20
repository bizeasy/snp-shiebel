import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductCategoryType, NewProductCategoryType } from '../product-category-type.model';

export type PartialUpdateProductCategoryType = Partial<IProductCategoryType> & Pick<IProductCategoryType, 'id'>;

@Service()
export class ProductCategoryTypesService {
  readonly productCategoryTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productCategoryTypesResource = httpResource<IProductCategoryType[]>(() => {
    const params = this.productCategoryTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productCategoryType that have been fetched. It is updated when the productCategoryTypesResource emits a new value.
   * In case of error while fetching the productCategoryTypes, the signal is set to an empty array.
   */
  readonly productCategoryTypes = computed(() =>
    this.productCategoryTypesResource.hasValue() ? this.productCategoryTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-category-types`;
}

@Service()
export class ProductCategoryTypeService extends ProductCategoryTypesService {
  protected readonly http = inject(HttpClient);

  create(productCategoryType: NewProductCategoryType): Observable<IProductCategoryType> {
    return this.http.post<IProductCategoryType>(this.resourceUrl, productCategoryType);
  }

  update(productCategoryType: IProductCategoryType): Observable<IProductCategoryType> {
    return this.http.put<IProductCategoryType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductCategoryTypeIdentifier(productCategoryType))}`,
      productCategoryType,
    );
  }

  partialUpdate(productCategoryType: PartialUpdateProductCategoryType): Observable<IProductCategoryType> {
    return this.http.patch<IProductCategoryType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductCategoryTypeIdentifier(productCategoryType))}`,
      productCategoryType,
    );
  }

  find(id: number): Observable<IProductCategoryType> {
    return this.http.get<IProductCategoryType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductCategoryType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductCategoryType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductCategoryTypeIdentifier(productCategoryType: Pick<IProductCategoryType, 'id'>): number {
    return productCategoryType.id;
  }

  compareProductCategoryType(o1: Pick<IProductCategoryType, 'id'> | null, o2: Pick<IProductCategoryType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductCategoryTypeIdentifier(o1) === this.getProductCategoryTypeIdentifier(o2) : o1 === o2;
  }

  addProductCategoryTypeToCollectionIfMissing<Type extends Pick<IProductCategoryType, 'id'>>(
    productCategoryTypeCollection: Type[],
    ...productCategoryTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productCategoryTypes: Type[] = productCategoryTypesToCheck.filter(
      productCategoryTypeItem => productCategoryTypeItem !== null && productCategoryTypeItem !== undefined,
    );
    if (productCategoryTypes.length > 0) {
      const productCategoryTypeCollectionIdentifiers = productCategoryTypeCollection.map(productCategoryTypeItem =>
        this.getProductCategoryTypeIdentifier(productCategoryTypeItem),
      );
      const productCategoryTypesToAdd = productCategoryTypes.filter(productCategoryTypeItem => {
        const productCategoryTypeIdentifier = this.getProductCategoryTypeIdentifier(productCategoryTypeItem);
        if (productCategoryTypeCollectionIdentifiers.includes(productCategoryTypeIdentifier)) {
          return false;
        }
        productCategoryTypeCollectionIdentifiers.push(productCategoryTypeIdentifier);
        return true;
      });
      return [...productCategoryTypesToAdd, ...productCategoryTypeCollection];
    }
    return productCategoryTypeCollection;
  }
}
