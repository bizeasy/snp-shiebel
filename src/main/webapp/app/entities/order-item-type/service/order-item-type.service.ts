import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderItemType, NewOrderItemType } from '../order-item-type.model';

export type PartialUpdateOrderItemType = Partial<IOrderItemType> & Pick<IOrderItemType, 'id'>;

@Service()
export class OrderItemTypesService {
  readonly orderItemTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderItemTypesResource = httpResource<IOrderItemType[]>(() => {
    const params = this.orderItemTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderItemType that have been fetched. It is updated when the orderItemTypesResource emits a new value.
   * In case of error while fetching the orderItemTypes, the signal is set to an empty array.
   */
  readonly orderItemTypes = computed(() => (this.orderItemTypesResource.hasValue() ? this.orderItemTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/order-item-types`;
}

@Service()
export class OrderItemTypeService extends OrderItemTypesService {
  protected readonly http = inject(HttpClient);

  create(orderItemType: NewOrderItemType): Observable<IOrderItemType> {
    return this.http.post<IOrderItemType>(this.resourceUrl, orderItemType);
  }

  update(orderItemType: IOrderItemType): Observable<IOrderItemType> {
    return this.http.put<IOrderItemType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderItemTypeIdentifier(orderItemType))}`,
      orderItemType,
    );
  }

  partialUpdate(orderItemType: PartialUpdateOrderItemType): Observable<IOrderItemType> {
    return this.http.patch<IOrderItemType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderItemTypeIdentifier(orderItemType))}`,
      orderItemType,
    );
  }

  find(id: number): Observable<IOrderItemType> {
    return this.http.get<IOrderItemType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderItemType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderItemType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderItemTypeIdentifier(orderItemType: Pick<IOrderItemType, 'id'>): number {
    return orderItemType.id;
  }

  compareOrderItemType(o1: Pick<IOrderItemType, 'id'> | null, o2: Pick<IOrderItemType, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderItemTypeIdentifier(o1) === this.getOrderItemTypeIdentifier(o2) : o1 === o2;
  }

  addOrderItemTypeToCollectionIfMissing<Type extends Pick<IOrderItemType, 'id'>>(
    orderItemTypeCollection: Type[],
    ...orderItemTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderItemTypes: Type[] = orderItemTypesToCheck.filter(
      orderItemTypeItem => orderItemTypeItem !== null && orderItemTypeItem !== undefined,
    );
    if (orderItemTypes.length > 0) {
      const orderItemTypeCollectionIdentifiers = orderItemTypeCollection.map(orderItemTypeItem =>
        this.getOrderItemTypeIdentifier(orderItemTypeItem),
      );
      const orderItemTypesToAdd = orderItemTypes.filter(orderItemTypeItem => {
        const orderItemTypeIdentifier = this.getOrderItemTypeIdentifier(orderItemTypeItem);
        if (orderItemTypeCollectionIdentifiers.includes(orderItemTypeIdentifier)) {
          return false;
        }
        orderItemTypeCollectionIdentifiers.push(orderItemTypeIdentifier);
        return true;
      });
      return [...orderItemTypesToAdd, ...orderItemTypeCollection];
    }
    return orderItemTypeCollection;
  }
}
