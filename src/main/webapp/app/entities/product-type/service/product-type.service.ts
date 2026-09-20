import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductType, NewProductType } from '../product-type.model';

export type PartialUpdateProductType = Partial<IProductType> & Pick<IProductType, 'id'>;

@Service()
export class ProductTypesService {
  readonly productTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productTypesResource = httpResource<IProductType[]>(() => {
    const params = this.productTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productType that have been fetched. It is updated when the productTypesResource emits a new value.
   * In case of error while fetching the productTypes, the signal is set to an empty array.
   */
  readonly productTypes = computed(() => (this.productTypesResource.hasValue() ? this.productTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-types`;
}

@Service()
export class ProductTypeService extends ProductTypesService {
  protected readonly http = inject(HttpClient);

  create(productType: NewProductType): Observable<IProductType> {
    return this.http.post<IProductType>(this.resourceUrl, productType);
  }

  update(productType: IProductType): Observable<IProductType> {
    return this.http.put<IProductType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductTypeIdentifier(productType))}`,
      productType,
    );
  }

  partialUpdate(productType: PartialUpdateProductType): Observable<IProductType> {
    return this.http.patch<IProductType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductTypeIdentifier(productType))}`,
      productType,
    );
  }

  find(id: number): Observable<IProductType> {
    return this.http.get<IProductType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductTypeIdentifier(productType: Pick<IProductType, 'id'>): number {
    return productType.id;
  }

  compareProductType(o1: Pick<IProductType, 'id'> | null, o2: Pick<IProductType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductTypeIdentifier(o1) === this.getProductTypeIdentifier(o2) : o1 === o2;
  }

  addProductTypeToCollectionIfMissing<Type extends Pick<IProductType, 'id'>>(
    productTypeCollection: Type[],
    ...productTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productTypes: Type[] = productTypesToCheck.filter(productTypeItem => productTypeItem !== null && productTypeItem !== undefined);
    if (productTypes.length > 0) {
      const productTypeCollectionIdentifiers = productTypeCollection.map(productTypeItem => this.getProductTypeIdentifier(productTypeItem));
      const productTypesToAdd = productTypes.filter(productTypeItem => {
        const productTypeIdentifier = this.getProductTypeIdentifier(productTypeItem);
        if (productTypeCollectionIdentifiers.includes(productTypeIdentifier)) {
          return false;
        }
        productTypeCollectionIdentifiers.push(productTypeIdentifier);
        return true;
      });
      return [...productTypesToAdd, ...productTypeCollection];
    }
    return productTypeCollection;
  }
}
