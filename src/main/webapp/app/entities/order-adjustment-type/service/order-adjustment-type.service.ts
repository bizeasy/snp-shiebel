import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderAdjustmentType, NewOrderAdjustmentType } from '../order-adjustment-type.model';

export type PartialUpdateOrderAdjustmentType = Partial<IOrderAdjustmentType> & Pick<IOrderAdjustmentType, 'id'>;

@Service()
export class OrderAdjustmentTypesService {
  readonly orderAdjustmentTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly orderAdjustmentTypesResource = httpResource<IOrderAdjustmentType[]>(() => {
    const params = this.orderAdjustmentTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderAdjustmentType that have been fetched. It is updated when the orderAdjustmentTypesResource emits a new value.
   * In case of error while fetching the orderAdjustmentTypes, the signal is set to an empty array.
   */
  readonly orderAdjustmentTypes = computed(() =>
    this.orderAdjustmentTypesResource.hasValue() ? this.orderAdjustmentTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/order-adjustment-types`;
}

@Service()
export class OrderAdjustmentTypeService extends OrderAdjustmentTypesService {
  protected readonly http = inject(HttpClient);

  create(orderAdjustmentType: NewOrderAdjustmentType): Observable<IOrderAdjustmentType> {
    return this.http.post<IOrderAdjustmentType>(this.resourceUrl, orderAdjustmentType);
  }

  update(orderAdjustmentType: IOrderAdjustmentType): Observable<IOrderAdjustmentType> {
    return this.http.put<IOrderAdjustmentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderAdjustmentTypeIdentifier(orderAdjustmentType))}`,
      orderAdjustmentType,
    );
  }

  partialUpdate(orderAdjustmentType: PartialUpdateOrderAdjustmentType): Observable<IOrderAdjustmentType> {
    return this.http.patch<IOrderAdjustmentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderAdjustmentTypeIdentifier(orderAdjustmentType))}`,
      orderAdjustmentType,
    );
  }

  find(id: number): Observable<IOrderAdjustmentType> {
    return this.http.get<IOrderAdjustmentType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderAdjustmentType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderAdjustmentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderAdjustmentTypeIdentifier(orderAdjustmentType: Pick<IOrderAdjustmentType, 'id'>): number {
    return orderAdjustmentType.id;
  }

  compareOrderAdjustmentType(o1: Pick<IOrderAdjustmentType, 'id'> | null, o2: Pick<IOrderAdjustmentType, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderAdjustmentTypeIdentifier(o1) === this.getOrderAdjustmentTypeIdentifier(o2) : o1 === o2;
  }

  addOrderAdjustmentTypeToCollectionIfMissing<Type extends Pick<IOrderAdjustmentType, 'id'>>(
    orderAdjustmentTypeCollection: Type[],
    ...orderAdjustmentTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderAdjustmentTypes: Type[] = orderAdjustmentTypesToCheck.filter(
      orderAdjustmentTypeItem => orderAdjustmentTypeItem !== null && orderAdjustmentTypeItem !== undefined,
    );
    if (orderAdjustmentTypes.length > 0) {
      const orderAdjustmentTypeCollectionIdentifiers = orderAdjustmentTypeCollection.map(orderAdjustmentTypeItem =>
        this.getOrderAdjustmentTypeIdentifier(orderAdjustmentTypeItem),
      );
      const orderAdjustmentTypesToAdd = orderAdjustmentTypes.filter(orderAdjustmentTypeItem => {
        const orderAdjustmentTypeIdentifier = this.getOrderAdjustmentTypeIdentifier(orderAdjustmentTypeItem);
        if (orderAdjustmentTypeCollectionIdentifiers.includes(orderAdjustmentTypeIdentifier)) {
          return false;
        }
        orderAdjustmentTypeCollectionIdentifiers.push(orderAdjustmentTypeIdentifier);
        return true;
      });
      return [...orderAdjustmentTypesToAdd, ...orderAdjustmentTypeCollection];
    }
    return orderAdjustmentTypeCollection;
  }
}
