import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipmentType, NewShipmentType } from '../shipment-type.model';

export type PartialUpdateShipmentType = Partial<IShipmentType> & Pick<IShipmentType, 'id'>;

@Service()
export class ShipmentTypesService {
  readonly shipmentTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shipmentTypesResource = httpResource<IShipmentType[]>(() => {
    const params = this.shipmentTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipmentType that have been fetched. It is updated when the shipmentTypesResource emits a new value.
   * In case of error while fetching the shipmentTypes, the signal is set to an empty array.
   */
  readonly shipmentTypes = computed(() => (this.shipmentTypesResource.hasValue() ? this.shipmentTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/shipment-types`;
}

@Service()
export class ShipmentTypeService extends ShipmentTypesService {
  protected readonly http = inject(HttpClient);

  create(shipmentType: NewShipmentType): Observable<IShipmentType> {
    return this.http.post<IShipmentType>(this.resourceUrl, shipmentType);
  }

  update(shipmentType: IShipmentType): Observable<IShipmentType> {
    return this.http.put<IShipmentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentTypeIdentifier(shipmentType))}`,
      shipmentType,
    );
  }

  partialUpdate(shipmentType: PartialUpdateShipmentType): Observable<IShipmentType> {
    return this.http.patch<IShipmentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentTypeIdentifier(shipmentType))}`,
      shipmentType,
    );
  }

  find(id: number): Observable<IShipmentType> {
    return this.http.get<IShipmentType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IShipmentType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentTypeIdentifier(shipmentType: Pick<IShipmentType, 'id'>): number {
    return shipmentType.id;
  }

  compareShipmentType(o1: Pick<IShipmentType, 'id'> | null, o2: Pick<IShipmentType, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentTypeIdentifier(o1) === this.getShipmentTypeIdentifier(o2) : o1 === o2;
  }

  addShipmentTypeToCollectionIfMissing<Type extends Pick<IShipmentType, 'id'>>(
    shipmentTypeCollection: Type[],
    ...shipmentTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipmentTypes: Type[] = shipmentTypesToCheck.filter(
      shipmentTypeItem => shipmentTypeItem !== null && shipmentTypeItem !== undefined,
    );
    if (shipmentTypes.length > 0) {
      const shipmentTypeCollectionIdentifiers = shipmentTypeCollection.map(shipmentTypeItem =>
        this.getShipmentTypeIdentifier(shipmentTypeItem),
      );
      const shipmentTypesToAdd = shipmentTypes.filter(shipmentTypeItem => {
        const shipmentTypeIdentifier = this.getShipmentTypeIdentifier(shipmentTypeItem);
        if (shipmentTypeCollectionIdentifiers.includes(shipmentTypeIdentifier)) {
          return false;
        }
        shipmentTypeCollectionIdentifiers.push(shipmentTypeIdentifier);
        return true;
      });
      return [...shipmentTypesToAdd, ...shipmentTypeCollection];
    }
    return shipmentTypeCollection;
  }
}
