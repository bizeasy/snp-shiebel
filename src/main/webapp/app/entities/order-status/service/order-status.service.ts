import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderStatus, NewOrderStatus } from '../order-status.model';

export type PartialUpdateOrderStatus = Partial<IOrderStatus> & Pick<IOrderStatus, 'id'>;

type RestOf<T extends IOrderStatus | NewOrderStatus> = Omit<T, 'statusDateTime'> & {
  statusDateTime?: string | null;
};

export type RestOrderStatus = RestOf<IOrderStatus>;

export type NewRestOrderStatus = RestOf<NewOrderStatus>;

export type PartialUpdateRestOrderStatus = RestOf<PartialUpdateOrderStatus>;

@Service()
export class OrderStatusesService {
  readonly orderStatusesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderStatusesResource = httpResource<RestOrderStatus[]>(() => {
    const params = this.orderStatusesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderStatus that have been fetched. It is updated when the orderStatusesResource emits a new value.
   * In case of error while fetching the orderStatuses, the signal is set to an empty array.
   */
  readonly orderStatuses = computed(() =>
    (this.orderStatusesResource.hasValue() ? this.orderStatusesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/order-statuses`;

  protected convertValueFromServer(restOrderStatus: RestOrderStatus): IOrderStatus {
    return {
      ...restOrderStatus,
      statusDateTime: restOrderStatus.statusDateTime ? dayjs(restOrderStatus.statusDateTime) : undefined,
    };
  }
}

@Service()
export class OrderStatusService extends OrderStatusesService {
  protected readonly http = inject(HttpClient);

  create(orderStatus: NewOrderStatus): Observable<IOrderStatus> {
    const copy = this.convertValueFromClient(orderStatus);
    return this.http.post<RestOrderStatus>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orderStatus: IOrderStatus): Observable<IOrderStatus> {
    const copy = this.convertValueFromClient(orderStatus);
    return this.http
      .put<RestOrderStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderStatusIdentifier(orderStatus))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orderStatus: PartialUpdateOrderStatus): Observable<IOrderStatus> {
    const copy = this.convertValueFromClient(orderStatus);
    return this.http
      .patch<RestOrderStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderStatusIdentifier(orderStatus))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IOrderStatus> {
    return this.http
      .get<RestOrderStatus>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IOrderStatus[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrderStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderStatusIdentifier(orderStatus: Pick<IOrderStatus, 'id'>): number {
    return orderStatus.id;
  }

  compareOrderStatus(o1: Pick<IOrderStatus, 'id'> | null, o2: Pick<IOrderStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderStatusIdentifier(o1) === this.getOrderStatusIdentifier(o2) : o1 === o2;
  }

  addOrderStatusToCollectionIfMissing<Type extends Pick<IOrderStatus, 'id'>>(
    orderStatusCollection: Type[],
    ...orderStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderStatuses: Type[] = orderStatusesToCheck.filter(orderStatusItem => orderStatusItem !== null && orderStatusItem !== undefined);
    if (orderStatuses.length > 0) {
      const orderStatusCollectionIdentifiers = orderStatusCollection.map(orderStatusItem => this.getOrderStatusIdentifier(orderStatusItem));
      const orderStatusesToAdd = orderStatuses.filter(orderStatusItem => {
        const orderStatusIdentifier = this.getOrderStatusIdentifier(orderStatusItem);
        if (orderStatusCollectionIdentifiers.includes(orderStatusIdentifier)) {
          return false;
        }
        orderStatusCollectionIdentifiers.push(orderStatusIdentifier);
        return true;
      });
      return [...orderStatusesToAdd, ...orderStatusCollection];
    }
    return orderStatusCollection;
  }

  protected convertValueFromClient<T extends IOrderStatus | NewOrderStatus | PartialUpdateOrderStatus>(orderStatus: T): RestOf<T> {
    return {
      ...orderStatus,
      statusDateTime: orderStatus.statusDateTime?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestOrderStatus): IOrderStatus {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestOrderStatus[]): IOrderStatus[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
