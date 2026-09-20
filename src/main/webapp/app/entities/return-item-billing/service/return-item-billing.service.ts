import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IReturnItemBilling, NewReturnItemBilling } from '../return-item-billing.model';

export type PartialUpdateReturnItemBilling = Partial<IReturnItemBilling> & Pick<IReturnItemBilling, 'id'>;

@Service()
export class ReturnItemBillingsService {
  readonly returnItemBillingsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly returnItemBillingsResource = httpResource<IReturnItemBilling[]>(() => {
    const params = this.returnItemBillingsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of returnItemBilling that have been fetched. It is updated when the returnItemBillingsResource emits a new value.
   * In case of error while fetching the returnItemBillings, the signal is set to an empty array.
   */
  readonly returnItemBillings = computed(() => (this.returnItemBillingsResource.hasValue() ? this.returnItemBillingsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/return-item-billings`;
}

@Service()
export class ReturnItemBillingService extends ReturnItemBillingsService {
  protected readonly http = inject(HttpClient);

  create(returnItemBilling: NewReturnItemBilling): Observable<IReturnItemBilling> {
    return this.http.post<IReturnItemBilling>(this.resourceUrl, returnItemBilling);
  }

  update(returnItemBilling: IReturnItemBilling): Observable<IReturnItemBilling> {
    return this.http.put<IReturnItemBilling>(
      `${this.resourceUrl}/${encodeURIComponent(this.getReturnItemBillingIdentifier(returnItemBilling))}`,
      returnItemBilling,
    );
  }

  partialUpdate(returnItemBilling: PartialUpdateReturnItemBilling): Observable<IReturnItemBilling> {
    return this.http.patch<IReturnItemBilling>(
      `${this.resourceUrl}/${encodeURIComponent(this.getReturnItemBillingIdentifier(returnItemBilling))}`,
      returnItemBilling,
    );
  }

  find(id: number): Observable<IReturnItemBilling> {
    return this.http.get<IReturnItemBilling>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IReturnItemBilling[]>> {
    const options = createRequestOption(req);
    return this.http.get<IReturnItemBilling[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getReturnItemBillingIdentifier(returnItemBilling: Pick<IReturnItemBilling, 'id'>): number {
    return returnItemBilling.id;
  }

  compareReturnItemBilling(o1: Pick<IReturnItemBilling, 'id'> | null, o2: Pick<IReturnItemBilling, 'id'> | null): boolean {
    return o1 && o2 ? this.getReturnItemBillingIdentifier(o1) === this.getReturnItemBillingIdentifier(o2) : o1 === o2;
  }

  addReturnItemBillingToCollectionIfMissing<Type extends Pick<IReturnItemBilling, 'id'>>(
    returnItemBillingCollection: Type[],
    ...returnItemBillingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const returnItemBillings: Type[] = returnItemBillingsToCheck.filter(
      returnItemBillingItem => returnItemBillingItem !== null && returnItemBillingItem !== undefined,
    );
    if (returnItemBillings.length > 0) {
      const returnItemBillingCollectionIdentifiers = returnItemBillingCollection.map(returnItemBillingItem =>
        this.getReturnItemBillingIdentifier(returnItemBillingItem),
      );
      const returnItemBillingsToAdd = returnItemBillings.filter(returnItemBillingItem => {
        const returnItemBillingIdentifier = this.getReturnItemBillingIdentifier(returnItemBillingItem);
        if (returnItemBillingCollectionIdentifiers.includes(returnItemBillingIdentifier)) {
          return false;
        }
        returnItemBillingCollectionIdentifiers.push(returnItemBillingIdentifier);
        return true;
      });
      return [...returnItemBillingsToAdd, ...returnItemBillingCollection];
    }
    return returnItemBillingCollection;
  }
}
