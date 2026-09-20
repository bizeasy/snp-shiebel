import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductPricePurpose, NewProductPricePurpose } from '../product-price-purpose.model';

export type PartialUpdateProductPricePurpose = Partial<IProductPricePurpose> & Pick<IProductPricePurpose, 'id'>;

@Service()
export class ProductPricePurposesService {
  readonly productPricePurposesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productPricePurposesResource = httpResource<IProductPricePurpose[]>(() => {
    const params = this.productPricePurposesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productPricePurpose that have been fetched. It is updated when the productPricePurposesResource emits a new value.
   * In case of error while fetching the productPricePurposes, the signal is set to an empty array.
   */
  readonly productPricePurposes = computed(() =>
    this.productPricePurposesResource.hasValue() ? this.productPricePurposesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-price-purposes`;
}

@Service()
export class ProductPricePurposeService extends ProductPricePurposesService {
  protected readonly http = inject(HttpClient);

  create(productPricePurpose: NewProductPricePurpose): Observable<IProductPricePurpose> {
    return this.http.post<IProductPricePurpose>(this.resourceUrl, productPricePurpose);
  }

  update(productPricePurpose: IProductPricePurpose): Observable<IProductPricePurpose> {
    return this.http.put<IProductPricePurpose>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductPricePurposeIdentifier(productPricePurpose))}`,
      productPricePurpose,
    );
  }

  partialUpdate(productPricePurpose: PartialUpdateProductPricePurpose): Observable<IProductPricePurpose> {
    return this.http.patch<IProductPricePurpose>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductPricePurposeIdentifier(productPricePurpose))}`,
      productPricePurpose,
    );
  }

  find(id: number): Observable<IProductPricePurpose> {
    return this.http.get<IProductPricePurpose>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductPricePurpose[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductPricePurpose[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductPricePurposeIdentifier(productPricePurpose: Pick<IProductPricePurpose, 'id'>): number {
    return productPricePurpose.id;
  }

  compareProductPricePurpose(o1: Pick<IProductPricePurpose, 'id'> | null, o2: Pick<IProductPricePurpose, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductPricePurposeIdentifier(o1) === this.getProductPricePurposeIdentifier(o2) : o1 === o2;
  }

  addProductPricePurposeToCollectionIfMissing<Type extends Pick<IProductPricePurpose, 'id'>>(
    productPricePurposeCollection: Type[],
    ...productPricePurposesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productPricePurposes: Type[] = productPricePurposesToCheck.filter(
      productPricePurposeItem => productPricePurposeItem !== null && productPricePurposeItem !== undefined,
    );
    if (productPricePurposes.length > 0) {
      const productPricePurposeCollectionIdentifiers = productPricePurposeCollection.map(productPricePurposeItem =>
        this.getProductPricePurposeIdentifier(productPricePurposeItem),
      );
      const productPricePurposesToAdd = productPricePurposes.filter(productPricePurposeItem => {
        const productPricePurposeIdentifier = this.getProductPricePurposeIdentifier(productPricePurposeItem);
        if (productPricePurposeCollectionIdentifiers.includes(productPricePurposeIdentifier)) {
          return false;
        }
        productPricePurposeCollectionIdentifiers.push(productPricePurposeIdentifier);
        return true;
      });
      return [...productPricePurposesToAdd, ...productPricePurposeCollection];
    }
    return productPricePurposeCollection;
  }
}
