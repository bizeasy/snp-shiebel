import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductFacilityLocation, NewProductFacilityLocation } from '../product-facility-location.model';

export type PartialUpdateProductFacilityLocation = Partial<IProductFacilityLocation> & Pick<IProductFacilityLocation, 'id'>;

@Service()
export class ProductFacilityLocationsService {
  readonly productFacilityLocationsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productFacilityLocationsResource = httpResource<IProductFacilityLocation[]>(() => {
    const params = this.productFacilityLocationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productFacilityLocation that have been fetched. It is updated when the productFacilityLocationsResource emits a new value.
   * In case of error while fetching the productFacilityLocations, the signal is set to an empty array.
   */
  readonly productFacilityLocations = computed(() =>
    this.productFacilityLocationsResource.hasValue() ? this.productFacilityLocationsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/product-facility-locations`;
}

@Service()
export class ProductFacilityLocationService extends ProductFacilityLocationsService {
  protected readonly http = inject(HttpClient);

  create(productFacilityLocation: NewProductFacilityLocation): Observable<IProductFacilityLocation> {
    return this.http.post<IProductFacilityLocation>(this.resourceUrl, productFacilityLocation);
  }

  update(productFacilityLocation: IProductFacilityLocation): Observable<IProductFacilityLocation> {
    return this.http.put<IProductFacilityLocation>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFacilityLocationIdentifier(productFacilityLocation))}`,
      productFacilityLocation,
    );
  }

  partialUpdate(productFacilityLocation: PartialUpdateProductFacilityLocation): Observable<IProductFacilityLocation> {
    return this.http.patch<IProductFacilityLocation>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductFacilityLocationIdentifier(productFacilityLocation))}`,
      productFacilityLocation,
    );
  }

  find(id: number): Observable<IProductFacilityLocation> {
    return this.http.get<IProductFacilityLocation>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductFacilityLocation[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductFacilityLocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductFacilityLocationIdentifier(productFacilityLocation: Pick<IProductFacilityLocation, 'id'>): number {
    return productFacilityLocation.id;
  }

  compareProductFacilityLocation(
    o1: Pick<IProductFacilityLocation, 'id'> | null,
    o2: Pick<IProductFacilityLocation, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getProductFacilityLocationIdentifier(o1) === this.getProductFacilityLocationIdentifier(o2) : o1 === o2;
  }

  addProductFacilityLocationToCollectionIfMissing<Type extends Pick<IProductFacilityLocation, 'id'>>(
    productFacilityLocationCollection: Type[],
    ...productFacilityLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productFacilityLocations: Type[] = productFacilityLocationsToCheck.filter(
      productFacilityLocationItem => productFacilityLocationItem !== null && productFacilityLocationItem !== undefined,
    );
    if (productFacilityLocations.length > 0) {
      const productFacilityLocationCollectionIdentifiers = productFacilityLocationCollection.map(productFacilityLocationItem =>
        this.getProductFacilityLocationIdentifier(productFacilityLocationItem),
      );
      const productFacilityLocationsToAdd = productFacilityLocations.filter(productFacilityLocationItem => {
        const productFacilityLocationIdentifier = this.getProductFacilityLocationIdentifier(productFacilityLocationItem);
        if (productFacilityLocationCollectionIdentifiers.includes(productFacilityLocationIdentifier)) {
          return false;
        }
        productFacilityLocationCollectionIdentifiers.push(productFacilityLocationIdentifier);
        return true;
      });
      return [...productFacilityLocationsToAdd, ...productFacilityLocationCollection];
    }
    return productFacilityLocationCollection;
  }
}
