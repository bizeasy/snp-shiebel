import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderContactMech, NewOrderContactMech } from '../order-contact-mech.model';

export type PartialUpdateOrderContactMech = Partial<IOrderContactMech> & Pick<IOrderContactMech, 'id'>;

type RestOf<T extends IOrderContactMech | NewOrderContactMech> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestOrderContactMech = RestOf<IOrderContactMech>;

export type NewRestOrderContactMech = RestOf<NewOrderContactMech>;

export type PartialUpdateRestOrderContactMech = RestOf<PartialUpdateOrderContactMech>;

@Service()
export class OrderContactMechesService {
  readonly orderContactMechesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly orderContactMechesResource = httpResource<RestOrderContactMech[]>(() => {
    const params = this.orderContactMechesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderContactMech that have been fetched. It is updated when the orderContactMechesResource emits a new value.
   * In case of error while fetching the orderContactMeches, the signal is set to an empty array.
   */
  readonly orderContactMeches = computed(() =>
    (this.orderContactMechesResource.hasValue() ? this.orderContactMechesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/order-contact-meches`;

  protected convertValueFromServer(restOrderContactMech: RestOrderContactMech): IOrderContactMech {
    return {
      ...restOrderContactMech,
      fromDate: restOrderContactMech.fromDate ? dayjs(restOrderContactMech.fromDate) : undefined,
      thruDate: restOrderContactMech.thruDate ? dayjs(restOrderContactMech.thruDate) : undefined,
    };
  }
}

@Service()
export class OrderContactMechService extends OrderContactMechesService {
  protected readonly http = inject(HttpClient);

  create(orderContactMech: NewOrderContactMech): Observable<IOrderContactMech> {
    const copy = this.convertValueFromClient(orderContactMech);
    return this.http.post<RestOrderContactMech>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orderContactMech: IOrderContactMech): Observable<IOrderContactMech> {
    const copy = this.convertValueFromClient(orderContactMech);
    return this.http
      .put<RestOrderContactMech>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderContactMechIdentifier(orderContactMech))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orderContactMech: PartialUpdateOrderContactMech): Observable<IOrderContactMech> {
    const copy = this.convertValueFromClient(orderContactMech);
    return this.http
      .patch<RestOrderContactMech>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderContactMechIdentifier(orderContactMech))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IOrderContactMech> {
    return this.http
      .get<RestOrderContactMech>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IOrderContactMech[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrderContactMech[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderContactMechIdentifier(orderContactMech: Pick<IOrderContactMech, 'id'>): number {
    return orderContactMech.id;
  }

  compareOrderContactMech(o1: Pick<IOrderContactMech, 'id'> | null, o2: Pick<IOrderContactMech, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderContactMechIdentifier(o1) === this.getOrderContactMechIdentifier(o2) : o1 === o2;
  }

  addOrderContactMechToCollectionIfMissing<Type extends Pick<IOrderContactMech, 'id'>>(
    orderContactMechCollection: Type[],
    ...orderContactMechesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderContactMeches: Type[] = orderContactMechesToCheck.filter(
      orderContactMechItem => orderContactMechItem !== null && orderContactMechItem !== undefined,
    );
    if (orderContactMeches.length > 0) {
      const orderContactMechCollectionIdentifiers = orderContactMechCollection.map(orderContactMechItem =>
        this.getOrderContactMechIdentifier(orderContactMechItem),
      );
      const orderContactMechesToAdd = orderContactMeches.filter(orderContactMechItem => {
        const orderContactMechIdentifier = this.getOrderContactMechIdentifier(orderContactMechItem);
        if (orderContactMechCollectionIdentifiers.includes(orderContactMechIdentifier)) {
          return false;
        }
        orderContactMechCollectionIdentifiers.push(orderContactMechIdentifier);
        return true;
      });
      return [...orderContactMechesToAdd, ...orderContactMechCollection];
    }
    return orderContactMechCollection;
  }

  protected convertValueFromClient<T extends IOrderContactMech | NewOrderContactMech | PartialUpdateOrderContactMech>(
    orderContactMech: T,
  ): RestOf<T> {
    return {
      ...orderContactMech,
      fromDate: orderContactMech.fromDate?.toJSON() ?? null,
      thruDate: orderContactMech.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestOrderContactMech): IOrderContactMech {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestOrderContactMech[]): IOrderContactMech[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
