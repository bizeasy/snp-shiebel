import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderItem, NewOrderItem } from '../order-item.model';

export type PartialUpdateOrderItem = Partial<IOrderItem> & Pick<IOrderItem, 'id'>;

type RestOf<T extends IOrderItem | NewOrderItem> = Omit<
  T,
  'estimatedShipDate' | 'estimatedDeliveryDate' | 'shipBeforeDate' | 'shipAfterDate' | 'entryDate'
> & {
  estimatedShipDate?: string | null;
  estimatedDeliveryDate?: string | null;
  shipBeforeDate?: string | null;
  shipAfterDate?: string | null;
  entryDate?: string | null;
};

export type RestOrderItem = RestOf<IOrderItem>;

export type NewRestOrderItem = RestOf<NewOrderItem>;

export type PartialUpdateRestOrderItem = RestOf<PartialUpdateOrderItem>;

@Service()
export class OrderItemsService {
  readonly orderItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderItemsResource = httpResource<RestOrderItem[]>(() => {
    const params = this.orderItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderItem that have been fetched. It is updated when the orderItemsResource emits a new value.
   * In case of error while fetching the orderItems, the signal is set to an empty array.
   */
  readonly orderItems = computed(() =>
    (this.orderItemsResource.hasValue() ? this.orderItemsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/order-items`;

  protected convertValueFromServer(restOrderItem: RestOrderItem): IOrderItem {
    return {
      ...restOrderItem,
      estimatedShipDate: restOrderItem.estimatedShipDate ? dayjs(restOrderItem.estimatedShipDate) : undefined,
      estimatedDeliveryDate: restOrderItem.estimatedDeliveryDate ? dayjs(restOrderItem.estimatedDeliveryDate) : undefined,
      shipBeforeDate: restOrderItem.shipBeforeDate ? dayjs(restOrderItem.shipBeforeDate) : undefined,
      shipAfterDate: restOrderItem.shipAfterDate ? dayjs(restOrderItem.shipAfterDate) : undefined,
      entryDate: restOrderItem.entryDate ? dayjs(restOrderItem.entryDate) : undefined,
    };
  }
}

@Service()
export class OrderItemService extends OrderItemsService {
  protected readonly http = inject(HttpClient);

  create(orderItem: NewOrderItem): Observable<IOrderItem> {
    const copy = this.convertValueFromClient(orderItem);
    return this.http.post<RestOrderItem>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orderItem: IOrderItem): Observable<IOrderItem> {
    const copy = this.convertValueFromClient(orderItem);
    return this.http
      .put<RestOrderItem>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderItemIdentifier(orderItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orderItem: PartialUpdateOrderItem): Observable<IOrderItem> {
    const copy = this.convertValueFromClient(orderItem);
    return this.http
      .patch<RestOrderItem>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderItemIdentifier(orderItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IOrderItem> {
    return this.http
      .get<RestOrderItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IOrderItem[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderItemIdentifier(orderItem: Pick<IOrderItem, 'id'>): number {
    return orderItem.id;
  }

  compareOrderItem(o1: Pick<IOrderItem, 'id'> | null, o2: Pick<IOrderItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderItemIdentifier(o1) === this.getOrderItemIdentifier(o2) : o1 === o2;
  }

  addOrderItemToCollectionIfMissing<Type extends Pick<IOrderItem, 'id'>>(
    orderItemCollection: Type[],
    ...orderItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderItems: Type[] = orderItemsToCheck.filter(orderItemItem => orderItemItem !== null && orderItemItem !== undefined);
    if (orderItems.length > 0) {
      const orderItemCollectionIdentifiers = orderItemCollection.map(orderItemItem => this.getOrderItemIdentifier(orderItemItem));
      const orderItemsToAdd = orderItems.filter(orderItemItem => {
        const orderItemIdentifier = this.getOrderItemIdentifier(orderItemItem);
        if (orderItemCollectionIdentifiers.includes(orderItemIdentifier)) {
          return false;
        }
        orderItemCollectionIdentifiers.push(orderItemIdentifier);
        return true;
      });
      return [...orderItemsToAdd, ...orderItemCollection];
    }
    return orderItemCollection;
  }

  protected convertValueFromClient<T extends IOrderItem | NewOrderItem | PartialUpdateOrderItem>(orderItem: T): RestOf<T> {
    return {
      ...orderItem,
      estimatedShipDate: orderItem.estimatedShipDate?.toJSON() ?? null,
      estimatedDeliveryDate: orderItem.estimatedDeliveryDate?.toJSON() ?? null,
      shipBeforeDate: orderItem.shipBeforeDate?.toJSON() ?? null,
      shipAfterDate: orderItem.shipAfterDate?.toJSON() ?? null,
      entryDate: orderItem.entryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestOrderItem): IOrderItem {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestOrderItem[]): IOrderItem[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
