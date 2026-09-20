import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipmentItem, NewShipmentItem } from '../shipment-item.model';

export type PartialUpdateShipmentItem = Partial<IShipmentItem> & Pick<IShipmentItem, 'id'>;

@Service()
export class ShipmentItemsService {
  readonly shipmentItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shipmentItemsResource = httpResource<IShipmentItem[]>(() => {
    const params = this.shipmentItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipmentItem that have been fetched. It is updated when the shipmentItemsResource emits a new value.
   * In case of error while fetching the shipmentItems, the signal is set to an empty array.
   */
  readonly shipmentItems = computed(() => (this.shipmentItemsResource.hasValue() ? this.shipmentItemsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/shipment-items`;
}

@Service()
export class ShipmentItemService extends ShipmentItemsService {
  protected readonly http = inject(HttpClient);

  create(shipmentItem: NewShipmentItem): Observable<IShipmentItem> {
    return this.http.post<IShipmentItem>(this.resourceUrl, shipmentItem);
  }

  update(shipmentItem: IShipmentItem): Observable<IShipmentItem> {
    return this.http.put<IShipmentItem>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentItemIdentifier(shipmentItem))}`,
      shipmentItem,
    );
  }

  partialUpdate(shipmentItem: PartialUpdateShipmentItem): Observable<IShipmentItem> {
    return this.http.patch<IShipmentItem>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentItemIdentifier(shipmentItem))}`,
      shipmentItem,
    );
  }

  find(id: number): Observable<IShipmentItem> {
    return this.http.get<IShipmentItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IShipmentItem[]>> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentItemIdentifier(shipmentItem: Pick<IShipmentItem, 'id'>): number {
    return shipmentItem.id;
  }

  compareShipmentItem(o1: Pick<IShipmentItem, 'id'> | null, o2: Pick<IShipmentItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentItemIdentifier(o1) === this.getShipmentItemIdentifier(o2) : o1 === o2;
  }

  addShipmentItemToCollectionIfMissing<Type extends Pick<IShipmentItem, 'id'>>(
    shipmentItemCollection: Type[],
    ...shipmentItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipmentItems: Type[] = shipmentItemsToCheck.filter(
      shipmentItemItem => shipmentItemItem !== null && shipmentItemItem !== undefined,
    );
    if (shipmentItems.length > 0) {
      const shipmentItemCollectionIdentifiers = shipmentItemCollection.map(shipmentItemItem =>
        this.getShipmentItemIdentifier(shipmentItemItem),
      );
      const shipmentItemsToAdd = shipmentItems.filter(shipmentItemItem => {
        const shipmentItemIdentifier = this.getShipmentItemIdentifier(shipmentItemItem);
        if (shipmentItemCollectionIdentifiers.includes(shipmentItemIdentifier)) {
          return false;
        }
        shipmentItemCollectionIdentifiers.push(shipmentItemIdentifier);
        return true;
      });
      return [...shipmentItemsToAdd, ...shipmentItemCollection];
    }
    return shipmentItemCollection;
  }
}
