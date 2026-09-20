import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderType, NewOrderType } from '../order-type.model';

export type PartialUpdateOrderType = Partial<IOrderType> & Pick<IOrderType, 'id'>;

@Service()
export class OrderTypesService {
  readonly orderTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderTypesResource = httpResource<IOrderType[]>(() => {
    const params = this.orderTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderType that have been fetched. It is updated when the orderTypesResource emits a new value.
   * In case of error while fetching the orderTypes, the signal is set to an empty array.
   */
  readonly orderTypes = computed(() => (this.orderTypesResource.hasValue() ? this.orderTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/order-types`;
}

@Service()
export class OrderTypeService extends OrderTypesService {
  protected readonly http = inject(HttpClient);

  create(orderType: NewOrderType): Observable<IOrderType> {
    return this.http.post<IOrderType>(this.resourceUrl, orderType);
  }

  update(orderType: IOrderType): Observable<IOrderType> {
    return this.http.put<IOrderType>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderTypeIdentifier(orderType))}`, orderType);
  }

  partialUpdate(orderType: PartialUpdateOrderType): Observable<IOrderType> {
    return this.http.patch<IOrderType>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderTypeIdentifier(orderType))}`, orderType);
  }

  find(id: number): Observable<IOrderType> {
    return this.http.get<IOrderType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderTypeIdentifier(orderType: Pick<IOrderType, 'id'>): number {
    return orderType.id;
  }

  compareOrderType(o1: Pick<IOrderType, 'id'> | null, o2: Pick<IOrderType, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderTypeIdentifier(o1) === this.getOrderTypeIdentifier(o2) : o1 === o2;
  }

  addOrderTypeToCollectionIfMissing<Type extends Pick<IOrderType, 'id'>>(
    orderTypeCollection: Type[],
    ...orderTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderTypes: Type[] = orderTypesToCheck.filter(orderTypeItem => orderTypeItem !== null && orderTypeItem !== undefined);
    if (orderTypes.length > 0) {
      const orderTypeCollectionIdentifiers = orderTypeCollection.map(orderTypeItem => this.getOrderTypeIdentifier(orderTypeItem));
      const orderTypesToAdd = orderTypes.filter(orderTypeItem => {
        const orderTypeIdentifier = this.getOrderTypeIdentifier(orderTypeItem);
        if (orderTypeCollectionIdentifiers.includes(orderTypeIdentifier)) {
          return false;
        }
        orderTypeCollectionIdentifiers.push(orderTypeIdentifier);
        return true;
      });
      return [...orderTypesToAdd, ...orderTypeCollection];
    }
    return orderTypeCollection;
  }
}
