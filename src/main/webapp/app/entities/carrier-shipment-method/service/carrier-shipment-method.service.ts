import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICarrierShipmentMethod, NewCarrierShipmentMethod } from '../carrier-shipment-method.model';

export type PartialUpdateCarrierShipmentMethod = Partial<ICarrierShipmentMethod> & Pick<ICarrierShipmentMethod, 'id'>;

@Service()
export class CarrierShipmentMethodsService {
  readonly carrierShipmentMethodsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly carrierShipmentMethodsResource = httpResource<ICarrierShipmentMethod[]>(() => {
    const params = this.carrierShipmentMethodsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of carrierShipmentMethod that have been fetched. It is updated when the carrierShipmentMethodsResource emits a new value.
   * In case of error while fetching the carrierShipmentMethods, the signal is set to an empty array.
   */
  readonly carrierShipmentMethods = computed(() =>
    this.carrierShipmentMethodsResource.hasValue() ? this.carrierShipmentMethodsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/carrier-shipment-methods`;
}

@Service()
export class CarrierShipmentMethodService extends CarrierShipmentMethodsService {
  protected readonly http = inject(HttpClient);

  create(carrierShipmentMethod: NewCarrierShipmentMethod): Observable<ICarrierShipmentMethod> {
    return this.http.post<ICarrierShipmentMethod>(this.resourceUrl, carrierShipmentMethod);
  }

  update(carrierShipmentMethod: ICarrierShipmentMethod): Observable<ICarrierShipmentMethod> {
    return this.http.put<ICarrierShipmentMethod>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCarrierShipmentMethodIdentifier(carrierShipmentMethod))}`,
      carrierShipmentMethod,
    );
  }

  partialUpdate(carrierShipmentMethod: PartialUpdateCarrierShipmentMethod): Observable<ICarrierShipmentMethod> {
    return this.http.patch<ICarrierShipmentMethod>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCarrierShipmentMethodIdentifier(carrierShipmentMethod))}`,
      carrierShipmentMethod,
    );
  }

  find(id: number): Observable<ICarrierShipmentMethod> {
    return this.http.get<ICarrierShipmentMethod>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICarrierShipmentMethod[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICarrierShipmentMethod[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCarrierShipmentMethodIdentifier(carrierShipmentMethod: Pick<ICarrierShipmentMethod, 'id'>): number {
    return carrierShipmentMethod.id;
  }

  compareCarrierShipmentMethod(o1: Pick<ICarrierShipmentMethod, 'id'> | null, o2: Pick<ICarrierShipmentMethod, 'id'> | null): boolean {
    return o1 && o2 ? this.getCarrierShipmentMethodIdentifier(o1) === this.getCarrierShipmentMethodIdentifier(o2) : o1 === o2;
  }

  addCarrierShipmentMethodToCollectionIfMissing<Type extends Pick<ICarrierShipmentMethod, 'id'>>(
    carrierShipmentMethodCollection: Type[],
    ...carrierShipmentMethodsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const carrierShipmentMethods: Type[] = carrierShipmentMethodsToCheck.filter(
      carrierShipmentMethodItem => carrierShipmentMethodItem !== null && carrierShipmentMethodItem !== undefined,
    );
    if (carrierShipmentMethods.length > 0) {
      const carrierShipmentMethodCollectionIdentifiers = carrierShipmentMethodCollection.map(carrierShipmentMethodItem =>
        this.getCarrierShipmentMethodIdentifier(carrierShipmentMethodItem),
      );
      const carrierShipmentMethodsToAdd = carrierShipmentMethods.filter(carrierShipmentMethodItem => {
        const carrierShipmentMethodIdentifier = this.getCarrierShipmentMethodIdentifier(carrierShipmentMethodItem);
        if (carrierShipmentMethodCollectionIdentifiers.includes(carrierShipmentMethodIdentifier)) {
          return false;
        }
        carrierShipmentMethodCollectionIdentifiers.push(carrierShipmentMethodIdentifier);
        return true;
      });
      return [...carrierShipmentMethodsToAdd, ...carrierShipmentMethodCollection];
    }
    return carrierShipmentMethodCollection;
  }
}
