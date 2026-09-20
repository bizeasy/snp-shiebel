import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductPriceType, NewProductPriceType } from '../product-price-type.model';

export type PartialUpdateProductPriceType = Partial<IProductPriceType> & Pick<IProductPriceType, 'id'>;

@Service()
export class ProductPriceTypesService {
  readonly productPriceTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productPriceTypesResource = httpResource<IProductPriceType[]>(() => {
    const params = this.productPriceTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productPriceType that have been fetched. It is updated when the productPriceTypesResource emits a new value.
   * In case of error while fetching the productPriceTypes, the signal is set to an empty array.
   */
  readonly productPriceTypes = computed(() => (this.productPriceTypesResource.hasValue() ? this.productPriceTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-price-types`;
}

@Service()
export class ProductPriceTypeService extends ProductPriceTypesService {
  protected readonly http = inject(HttpClient);

  create(productPriceType: NewProductPriceType): Observable<IProductPriceType> {
    return this.http.post<IProductPriceType>(this.resourceUrl, productPriceType);
  }

  update(productPriceType: IProductPriceType): Observable<IProductPriceType> {
    return this.http.put<IProductPriceType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductPriceTypeIdentifier(productPriceType))}`,
      productPriceType,
    );
  }

  partialUpdate(productPriceType: PartialUpdateProductPriceType): Observable<IProductPriceType> {
    return this.http.patch<IProductPriceType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductPriceTypeIdentifier(productPriceType))}`,
      productPriceType,
    );
  }

  find(id: number): Observable<IProductPriceType> {
    return this.http.get<IProductPriceType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductPriceType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductPriceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductPriceTypeIdentifier(productPriceType: Pick<IProductPriceType, 'id'>): number {
    return productPriceType.id;
  }

  compareProductPriceType(o1: Pick<IProductPriceType, 'id'> | null, o2: Pick<IProductPriceType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductPriceTypeIdentifier(o1) === this.getProductPriceTypeIdentifier(o2) : o1 === o2;
  }

  addProductPriceTypeToCollectionIfMissing<Type extends Pick<IProductPriceType, 'id'>>(
    productPriceTypeCollection: Type[],
    ...productPriceTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productPriceTypes: Type[] = productPriceTypesToCheck.filter(
      productPriceTypeItem => productPriceTypeItem !== null && productPriceTypeItem !== undefined,
    );
    if (productPriceTypes.length > 0) {
      const productPriceTypeCollectionIdentifiers = productPriceTypeCollection.map(productPriceTypeItem =>
        this.getProductPriceTypeIdentifier(productPriceTypeItem),
      );
      const productPriceTypesToAdd = productPriceTypes.filter(productPriceTypeItem => {
        const productPriceTypeIdentifier = this.getProductPriceTypeIdentifier(productPriceTypeItem);
        if (productPriceTypeCollectionIdentifiers.includes(productPriceTypeIdentifier)) {
          return false;
        }
        productPriceTypeCollectionIdentifiers.push(productPriceTypeIdentifier);
        return true;
      });
      return [...productPriceTypesToAdd, ...productPriceTypeCollection];
    }
    return productPriceTypeCollection;
  }
}
