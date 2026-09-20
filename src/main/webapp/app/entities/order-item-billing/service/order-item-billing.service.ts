import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderItemBilling, NewOrderItemBilling } from '../order-item-billing.model';

export type PartialUpdateOrderItemBilling = Partial<IOrderItemBilling> & Pick<IOrderItemBilling, 'id'>;

@Service()
export class OrderItemBillingsService {
  readonly orderItemBillingsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderItemBillingsResource = httpResource<IOrderItemBilling[]>(() => {
    const params = this.orderItemBillingsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderItemBilling that have been fetched. It is updated when the orderItemBillingsResource emits a new value.
   * In case of error while fetching the orderItemBillings, the signal is set to an empty array.
   */
  readonly orderItemBillings = computed(() => (this.orderItemBillingsResource.hasValue() ? this.orderItemBillingsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/order-item-billings`;
}

@Service()
export class OrderItemBillingService extends OrderItemBillingsService {
  protected readonly http = inject(HttpClient);

  create(orderItemBilling: NewOrderItemBilling): Observable<IOrderItemBilling> {
    return this.http.post<IOrderItemBilling>(this.resourceUrl, orderItemBilling);
  }

  update(orderItemBilling: IOrderItemBilling): Observable<IOrderItemBilling> {
    return this.http.put<IOrderItemBilling>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderItemBillingIdentifier(orderItemBilling))}`,
      orderItemBilling,
    );
  }

  partialUpdate(orderItemBilling: PartialUpdateOrderItemBilling): Observable<IOrderItemBilling> {
    return this.http.patch<IOrderItemBilling>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderItemBillingIdentifier(orderItemBilling))}`,
      orderItemBilling,
    );
  }

  find(id: number): Observable<IOrderItemBilling> {
    return this.http.get<IOrderItemBilling>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderItemBilling[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderItemBilling[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderItemBillingIdentifier(orderItemBilling: Pick<IOrderItemBilling, 'id'>): number {
    return orderItemBilling.id;
  }

  compareOrderItemBilling(o1: Pick<IOrderItemBilling, 'id'> | null, o2: Pick<IOrderItemBilling, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderItemBillingIdentifier(o1) === this.getOrderItemBillingIdentifier(o2) : o1 === o2;
  }

  addOrderItemBillingToCollectionIfMissing<Type extends Pick<IOrderItemBilling, 'id'>>(
    orderItemBillingCollection: Type[],
    ...orderItemBillingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderItemBillings: Type[] = orderItemBillingsToCheck.filter(
      orderItemBillingItem => orderItemBillingItem !== null && orderItemBillingItem !== undefined,
    );
    if (orderItemBillings.length > 0) {
      const orderItemBillingCollectionIdentifiers = orderItemBillingCollection.map(orderItemBillingItem =>
        this.getOrderItemBillingIdentifier(orderItemBillingItem),
      );
      const orderItemBillingsToAdd = orderItemBillings.filter(orderItemBillingItem => {
        const orderItemBillingIdentifier = this.getOrderItemBillingIdentifier(orderItemBillingItem);
        if (orderItemBillingCollectionIdentifiers.includes(orderItemBillingIdentifier)) {
          return false;
        }
        orderItemBillingCollectionIdentifiers.push(orderItemBillingIdentifier);
        return true;
      });
      return [...orderItemBillingsToAdd, ...orderItemBillingCollection];
    }
    return orderItemBillingCollection;
  }
}
