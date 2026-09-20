import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFeatureType, NewProductFeatureType } from '../product-feature-type.model';

export type PartialUpdateProductFeatureType = Partial<IProductFeatureType> & Pick<IProductFeatureType, 'id'>;

@Service()
export class ProductFeatureTypesService {
  readonly productFeatureTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFeatureTypesResource = httpResource<IProductFeatureType[]>(() => {
    const params = this.productFeatureTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFeatureType that have been fetched. It is updated when the productFeatureTypesResource emits a new value.
   * In case of error while fetching the productFeatureTypes, the signal is set to an empty array.
   */
  readonly productFeatureTypes = computed(() =>
    this.productFeatureTypesResource.hasValue() ? this.productFeatureTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-feature-types`;
}

@Service()
export class ProductFeatureTypeService extends ProductFeatureTypesService {
  protected readonly http = inject(HttpClient);

  create(productFeatureType: NewProductFeatureType): Observable<IProductFeatureType> {
    return this.http.post<IProductFeatureType>(this.resourceUrl, productFeatureType);
  }

  update(productFeatureType: IProductFeatureType): Observable<IProductFeatureType> {
    return this.http.put<IProductFeatureType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureTypeIdentifier(productFeatureType))}`,
      productFeatureType,
    );
  }

  partialUpdate(productFeatureType: PartialUpdateProductFeatureType): Observable<IProductFeatureType> {
    return this.http.patch<IProductFeatureType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFeatureTypeIdentifier(productFeatureType))}`,
      productFeatureType,
    );
  }

  find(id: number): Observable<IProductFeatureType> {
    return this.http.get<IProductFeatureType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFeatureType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFeatureType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFeatureTypeIdentifier(productFeatureType: Pick<IProductFeatureType, 'id'>): number {
    return productFeatureType.id;
  }

  compareProductFeatureType(o1: Pick<IProductFeatureType, 'id'> | null, o2: Pick<IProductFeatureType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFeatureTypeIdentifier(o1) === this.getProductFeatureTypeIdentifier(o2) : o1 === o2;
  }

  addProductFeatureTypeToCollectionIfMissing<Type extends Pick<IProductFeatureType, 'id'>>(
    productFeatureTypeCollection: Type[],
    ...productFeatureTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFeatureTypes: Type[] = productFeatureTypesToCheck.filter(
      productFeatureTypeItem => productFeatureTypeItem !== null && productFeatureTypeItem !== undefined,
    );
    if (productFeatureTypes.length > 0) {
      const productFeatureTypeCollectionIdentifiers = productFeatureTypeCollection.map(productFeatureTypeItem =>
        this.getProductFeatureTypeIdentifier(productFeatureTypeItem),
      );
      const productFeatureTypesToAdd = productFeatureTypes.filter(productFeatureTypeItem => {
        const productFeatureTypeIdentifier = this.getProductFeatureTypeIdentifier(productFeatureTypeItem);
        if (productFeatureTypeCollectionIdentifiers.includes(productFeatureTypeIdentifier)) {
          return false;
        }
        productFeatureTypeCollectionIdentifiers.push(productFeatureTypeIdentifier);
        return true;
      });
      return [...productFeatureTypesToAdd, ...productFeatureTypeCollection];
    }
    return productFeatureTypeCollection;
  }
}
