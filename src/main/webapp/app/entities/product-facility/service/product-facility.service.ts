import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFacility, NewProductFacility } from '../product-facility.model';

export type PartialUpdateProductFacility = Partial<IProductFacility> & Pick<IProductFacility, 'id'>;

@Service()
export class ProductFacilitiesService {
  readonly productFacilitiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productFacilitiesResource = httpResource<IProductFacility[]>(() => {
    const params = this.productFacilitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFacility that have been fetched. It is updated when the productFacilitiesResource emits a new value.
   * In case of error while fetching the productFacilities, the signal is set to an empty array.
   */
  readonly productFacilities = computed(() => (this.productFacilitiesResource.hasValue() ? this.productFacilitiesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-facilities`;
}

@Service()
export class ProductFacilityService extends ProductFacilitiesService {
  protected readonly http = inject(HttpClient);

  create(productFacility: NewProductFacility): Observable<IProductFacility> {
    return this.http.post<IProductFacility>(this.resourceUrl, productFacility);
  }

  update(productFacility: IProductFacility): Observable<IProductFacility> {
    return this.http.put<IProductFacility>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFacilityIdentifier(productFacility))}`,
      productFacility,
    );
  }

  partialUpdate(productFacility: PartialUpdateProductFacility): Observable<IProductFacility> {
    return this.http.patch<IProductFacility>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFacilityIdentifier(productFacility))}`,
      productFacility,
    );
  }

  find(id: number): Observable<IProductFacility> {
    return this.http.get<IProductFacility>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFacility[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFacility[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFacilityIdentifier(productFacility: Pick<IProductFacility, 'id'>): number {
    return productFacility.id;
  }

  compareProductFacility(o1: Pick<IProductFacility, 'id'> | null, o2: Pick<IProductFacility, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductFacilityIdentifier(o1) === this.getProductFacilityIdentifier(o2) : o1 === o2;
  }

  addProductFacilityToCollectionIfMissing<Type extends Pick<IProductFacility, 'id'>>(
    productFacilityCollection: Type[],
    ...productFacilitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFacilities: Type[] = productFacilitiesToCheck.filter(
      productFacilityItem => productFacilityItem !== null && productFacilityItem !== undefined,
    );
    if (productFacilities.length > 0) {
      const productFacilityCollectionIdentifiers = productFacilityCollection.map(productFacilityItem =>
        this.getProductFacilityIdentifier(productFacilityItem),
      );
      const productFacilitiesToAdd = productFacilities.filter(productFacilityItem => {
        const productFacilityIdentifier = this.getProductFacilityIdentifier(productFacilityItem);
        if (productFacilityCollectionIdentifiers.includes(productFacilityIdentifier)) {
          return false;
        }
        productFacilityCollectionIdentifiers.push(productFacilityIdentifier);
        return true;
      });
      return [...productFacilitiesToAdd, ...productFacilityCollection];
    }
    return productFacilityCollection;
  }
}
