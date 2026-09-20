import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureApplType, NewProductFeatureApplType } from '../product-feature-appl-type.model';

export type PartialUpdateProductFeatureApplType = Partial<IProductFeatureApplType> & Pick<IProductFeatureApplType, 'id'>;

@Service()
export class ProductFeatureApplTypesService {
  readonly productFeatureApplTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureApplTypesResource = httpResource<IProductFeatureApplType[]>(() => {
    const params = this.productFeatureApplTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureApplType that have been fetched. It is updated when the productFeatureApplTypesResource emits a new value.
   * In case of error while fetching the productFeatureApplTypes, the signal is set to an empty array.
   */
  readonly productFeatureApplTypes = computed(() =>
    this.productFeatureApplTypesResource.hasValue() ? this.productFeatureApplTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-appl-types`;
}

@Service()
export class ProductFeatureApplTypeService extends ProductFeatureApplTypesService {
  protected readonly http = inject(HttpClient);

  create(productFeatureApplType: NewProductFeatureApplType): Observable<IProductFeatureApplType> {
    return this.http.post<IProductFeatureApplType>(this.resourceUrl, productFeatureApplType);
  }

  update(productFeatureApplType: IProductFeatureApplType): Observable<IProductFeatureApplType> {
    return this.http.put<IProductFeatureApplType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureApplTypeIdentifier(productFeatureApplType))}`,
      productFeatureApplType,
    );
  }

  partialUpdate(productFeatureApplType: PartialUpdateProductFeatureApplType): Observable<IProductFeatureApplType> {
    return this.http.patch<IProductFeatureApplType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureApplTypeIdentifier(productFeatureApplType))}`,
      productFeatureApplType,
    );
  }

  find(id: number): Observable<IProductFeatureApplType> {
    return this.http.get<IProductFeatureApplType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureApplType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFeatureApplType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureApplTypeIdentifier(productFeatureApplType: Pick<IProductFeatureApplType, 'id'>): number {
    return productFeatureApplType.id;
  }

  compareProductFeatureApplType(o1: Pick<IProductFeatureApplType, 'id'> | null, o2: Pick<IProductFeatureApplType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFeatureApplTypeIdentifier(o1) === this.getProductFeatureApplTypeIdentifier(o2) : o1 === o2;
  }

  addProductFeatureApplTypeToCollectionIfMissing<Type extends Pick<IProductFeatureApplType, 'id'>>(
    productFeatureApplTypeCollection: Type[],
    ...productFeatureApplTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureApplTypes: Type[] = productFeatureApplTypesToCheck.filter(
      productFeatureApplTypeItem => productFeatureApplTypeItem !== null && productFeatureApplTypeItem !== undefined,
    );
    if (productFeatureApplTypes.length > 0) {
      const productFeatureApplTypeCollectionIdentifiers = productFeatureApplTypeCollection.map(productFeatureApplTypeItem =>
        this.getProductFeatureApplTypeIdentifier(productFeatureApplTypeItem),
      );
      const productFeatureApplTypesToAdd = productFeatureApplTypes.filter(productFeatureApplTypeItem => {
        const productFeatureApplTypeIdentifier = this.getProductFeatureApplTypeIdentifier(productFeatureApplTypeItem);
        if (productFeatureApplTypeCollectionIdentifiers.includes(productFeatureApplTypeIdentifier)) {
          return false;
        }
        productFeatureApplTypeCollectionIdentifiers.push(productFeatureApplTypeIdentifier);
        return true;
      });
      return [...productFeatureApplTypesToAdd, ...productFeatureApplTypeCollection];
    }
    return productFeatureApplTypeCollection;
  }
}
