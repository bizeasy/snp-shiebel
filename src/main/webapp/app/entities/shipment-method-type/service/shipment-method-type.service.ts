import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipmentMethodType, NewShipmentMethodType } from '../shipment-method-type.model';

export type PartialUpdateShipmentMethodType = Partial<IShipmentMethodType> & Pick<IShipmentMethodType, 'id'>;

@Service()
export class ShipmentMethodTypesService {
  readonly shipmentMethodTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly shipmentMethodTypesResource = httpResource<IShipmentMethodType[]>(() => {
    const params = this.shipmentMethodTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipmentMethodType that have been fetched. It is updated when the shipmentMethodTypesResource emits a new value.
   * In case of error while fetching the shipmentMethodTypes, the signal is set to an empty array.
   */
  readonly shipmentMethodTypes = computed(() =>
    this.shipmentMethodTypesResource.hasValue() ? this.shipmentMethodTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/shipment-method-types`;
}

@Service()
export class ShipmentMethodTypeService extends ShipmentMethodTypesService {
  protected readonly http = inject(HttpClient);

  create(shipmentMethodType: NewShipmentMethodType): Observable<IShipmentMethodType> {
    return this.http.post<IShipmentMethodType>(this.resourceUrl, shipmentMethodType);
  }

  update(shipmentMethodType: IShipmentMethodType): Observable<IShipmentMethodType> {
    return this.http.put<IShipmentMethodType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentMethodTypeIdentifier(shipmentMethodType))}`,
      shipmentMethodType,
    );
  }

  partialUpdate(shipmentMethodType: PartialUpdateShipmentMethodType): Observable<IShipmentMethodType> {
    return this.http.patch<IShipmentMethodType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentMethodTypeIdentifier(shipmentMethodType))}`,
      shipmentMethodType,
    );
  }

  find(id: number): Observable<IShipmentMethodType> {
    return this.http.get<IShipmentMethodType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IShipmentMethodType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentMethodType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentMethodTypeIdentifier(shipmentMethodType: Pick<IShipmentMethodType, 'id'>): number {
    return shipmentMethodType.id;
  }

  compareShipmentMethodType(o1: Pick<IShipmentMethodType, 'id'> | null, o2: Pick<IShipmentMethodType, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentMethodTypeIdentifier(o1) === this.getShipmentMethodTypeIdentifier(o2) : o1 === o2;
  }

  addShipmentMethodTypeToCollectionIfMissing<Type extends Pick<IShipmentMethodType, 'id'>>(
    shipmentMethodTypeCollection: Type[],
    ...shipmentMethodTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipmentMethodTypes: Type[] = shipmentMethodTypesToCheck.filter(
      shipmentMethodTypeItem => shipmentMethodTypeItem !== null && shipmentMethodTypeItem !== undefined,
    );
    if (shipmentMethodTypes.length > 0) {
      const shipmentMethodTypeCollectionIdentifiers = shipmentMethodTypeCollection.map(shipmentMethodTypeItem =>
        this.getShipmentMethodTypeIdentifier(shipmentMethodTypeItem),
      );
      const shipmentMethodTypesToAdd = shipmentMethodTypes.filter(shipmentMethodTypeItem => {
        const shipmentMethodTypeIdentifier = this.getShipmentMethodTypeIdentifier(shipmentMethodTypeItem);
        if (shipmentMethodTypeCollectionIdentifiers.includes(shipmentMethodTypeIdentifier)) {
          return false;
        }
        shipmentMethodTypeCollectionIdentifiers.push(shipmentMethodTypeIdentifier);
        return true;
      });
      return [...shipmentMethodTypesToAdd, ...shipmentMethodTypeCollection];
    }
    return shipmentMethodTypeCollection;
  }
}
