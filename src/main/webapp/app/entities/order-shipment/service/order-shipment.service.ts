import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderShipment, NewOrderShipment } from '../order-shipment.model';

export type PartialUpdateOrderShipment = Partial<IOrderShipment> & Pick<IOrderShipment, 'id'>;

@Service()
export class OrderShipmentsService {
  readonly orderShipmentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderShipmentsResource = httpResource<IOrderShipment[]>(() => {
    const params = this.orderShipmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderShipment that have been fetched. It is updated when the orderShipmentsResource emits a new value.
   * In case of error while fetching the orderShipments, the signal is set to an empty array.
   */
  readonly orderShipments = computed(() => (this.orderShipmentsResource.hasValue() ? this.orderShipmentsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/order-shipments`;
}

@Service()
export class OrderShipmentService extends OrderShipmentsService {
  protected readonly http = inject(HttpClient);

  create(orderShipment: NewOrderShipment): Observable<IOrderShipment> {
    return this.http.post<IOrderShipment>(this.resourceUrl, orderShipment);
  }

  update(orderShipment: IOrderShipment): Observable<IOrderShipment> {
    return this.http.put<IOrderShipment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderShipmentIdentifier(orderShipment))}`,
      orderShipment,
    );
  }

  partialUpdate(orderShipment: PartialUpdateOrderShipment): Observable<IOrderShipment> {
    return this.http.patch<IOrderShipment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderShipmentIdentifier(orderShipment))}`,
      orderShipment,
    );
  }

  find(id: number): Observable<IOrderShipment> {
    return this.http.get<IOrderShipment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderShipment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderShipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderShipmentIdentifier(orderShipment: Pick<IOrderShipment, 'id'>): number {
    return orderShipment.id;
  }

  compareOrderShipment(o1: Pick<IOrderShipment, 'id'> | null, o2: Pick<IOrderShipment, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderShipmentIdentifier(o1) === this.getOrderShipmentIdentifier(o2) : o1 === o2;
  }

  addOrderShipmentToCollectionIfMissing<Type extends Pick<IOrderShipment, 'id'>>(
    orderShipmentCollection: Type[],
    ...orderShipmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderShipments: Type[] = orderShipmentsToCheck.filter(
      orderShipmentItem => orderShipmentItem !== null && orderShipmentItem !== undefined,
    );
    if (orderShipments.length > 0) {
      const orderShipmentCollectionIdentifiers = orderShipmentCollection.map(orderShipmentItem =>
        this.getOrderShipmentIdentifier(orderShipmentItem),
      );
      const orderShipmentsToAdd = orderShipments.filter(orderShipmentItem => {
        const orderShipmentIdentifier = this.getOrderShipmentIdentifier(orderShipmentItem);
        if (orderShipmentCollectionIdentifiers.includes(orderShipmentIdentifier)) {
          return false;
        }
        orderShipmentCollectionIdentifiers.push(orderShipmentIdentifier);
        return true;
      });
      return [...orderShipmentsToAdd, ...orderShipmentCollection];
    }
    return orderShipmentCollection;
  }
}
