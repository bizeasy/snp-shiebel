import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderAdjustment, NewOrderAdjustment } from '../order-adjustment.model';

export type PartialUpdateOrderAdjustment = Partial<IOrderAdjustment> & Pick<IOrderAdjustment, 'id'>;

type RestOf<T extends IOrderAdjustment | NewOrderAdjustment> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

export type RestOrderAdjustment = RestOf<IOrderAdjustment>;

export type NewRestOrderAdjustment = RestOf<NewOrderAdjustment>;

export type PartialUpdateRestOrderAdjustment = RestOf<PartialUpdateOrderAdjustment>;

@Service()
export class OrderAdjustmentsService {
  readonly orderAdjustmentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderAdjustmentsResource = httpResource<RestOrderAdjustment[]>(() => {
    const params = this.orderAdjustmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderAdjustment that have been fetched. It is updated when the orderAdjustmentsResource emits a new value.
   * In case of error while fetching the orderAdjustments, the signal is set to an empty array.
   */
  readonly orderAdjustments = computed(() =>
    (this.orderAdjustmentsResource.hasValue() ? this.orderAdjustmentsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/order-adjustments`;

  protected convertValueFromServer(restOrderAdjustment: RestOrderAdjustment): IOrderAdjustment {
    return {
      ...restOrderAdjustment,
      entryDate: restOrderAdjustment.entryDate ? dayjs(restOrderAdjustment.entryDate) : undefined,
    };
  }
}

@Service()
export class OrderAdjustmentService extends OrderAdjustmentsService {
  protected readonly http = inject(HttpClient);

  create(orderAdjustment: NewOrderAdjustment): Observable<IOrderAdjustment> {
    const copy = this.convertValueFromClient(orderAdjustment);
    return this.http.post<RestOrderAdjustment>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orderAdjustment: IOrderAdjustment): Observable<IOrderAdjustment> {
    const copy = this.convertValueFromClient(orderAdjustment);
    return this.http
      .put<RestOrderAdjustment>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderAdjustmentIdentifier(orderAdjustment))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orderAdjustment: PartialUpdateOrderAdjustment): Observable<IOrderAdjustment> {
    const copy = this.convertValueFromClient(orderAdjustment);
    return this.http
      .patch<RestOrderAdjustment>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderAdjustmentIdentifier(orderAdjustment))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IOrderAdjustment> {
    return this.http
      .get<RestOrderAdjustment>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IOrderAdjustment[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrderAdjustment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderAdjustmentIdentifier(orderAdjustment: Pick<IOrderAdjustment, 'id'>): number {
    return orderAdjustment.id;
  }

  compareOrderAdjustment(o1: Pick<IOrderAdjustment, 'id'> | null, o2: Pick<IOrderAdjustment, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderAdjustmentIdentifier(o1) === this.getOrderAdjustmentIdentifier(o2) : o1 === o2;
  }

  addOrderAdjustmentToCollectionIfMissing<Type extends Pick<IOrderAdjustment, 'id'>>(
    orderAdjustmentCollection: Type[],
    ...orderAdjustmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderAdjustments: Type[] = orderAdjustmentsToCheck.filter(
      orderAdjustmentItem => orderAdjustmentItem !== null && orderAdjustmentItem !== undefined,
    );
    if (orderAdjustments.length > 0) {
      const orderAdjustmentCollectionIdentifiers = orderAdjustmentCollection.map(orderAdjustmentItem =>
        this.getOrderAdjustmentIdentifier(orderAdjustmentItem),
      );
      const orderAdjustmentsToAdd = orderAdjustments.filter(orderAdjustmentItem => {
        const orderAdjustmentIdentifier = this.getOrderAdjustmentIdentifier(orderAdjustmentItem);
        if (orderAdjustmentCollectionIdentifiers.includes(orderAdjustmentIdentifier)) {
          return false;
        }
        orderAdjustmentCollectionIdentifiers.push(orderAdjustmentIdentifier);
        return true;
      });
      return [...orderAdjustmentsToAdd, ...orderAdjustmentCollection];
    }
    return orderAdjustmentCollection;
  }

  protected convertValueFromClient<T extends IOrderAdjustment | NewOrderAdjustment | PartialUpdateOrderAdjustment>(
    orderAdjustment: T,
  ): RestOf<T> {
    return {
      ...orderAdjustment,
      entryDate: orderAdjustment.entryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestOrderAdjustment): IOrderAdjustment {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestOrderAdjustment[]): IOrderAdjustment[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
