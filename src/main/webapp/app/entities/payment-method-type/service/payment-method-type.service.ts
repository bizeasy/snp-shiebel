import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaymentMethodType, NewPaymentMethodType } from '../payment-method-type.model';

export type PartialUpdatePaymentMethodType = Partial<IPaymentMethodType> & Pick<IPaymentMethodType, 'id'>;

@Service()
export class PaymentMethodTypesService {
  readonly paymentMethodTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly paymentMethodTypesResource = httpResource<IPaymentMethodType[]>(() => {
    const params = this.paymentMethodTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paymentMethodType that have been fetched. It is updated when the paymentMethodTypesResource emits a new value.
   * In case of error while fetching the paymentMethodTypes, the signal is set to an empty array.
   */
  readonly paymentMethodTypes = computed(() => (this.paymentMethodTypesResource.hasValue() ? this.paymentMethodTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/payment-method-types`;
}

@Service()
export class PaymentMethodTypeService extends PaymentMethodTypesService {
  protected readonly http = inject(HttpClient);

  create(paymentMethodType: NewPaymentMethodType): Observable<IPaymentMethodType> {
    return this.http.post<IPaymentMethodType>(this.resourceUrl, paymentMethodType);
  }

  update(paymentMethodType: IPaymentMethodType): Observable<IPaymentMethodType> {
    return this.http.put<IPaymentMethodType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentMethodTypeIdentifier(paymentMethodType))}`,
      paymentMethodType,
    );
  }

  partialUpdate(paymentMethodType: PartialUpdatePaymentMethodType): Observable<IPaymentMethodType> {
    return this.http.patch<IPaymentMethodType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentMethodTypeIdentifier(paymentMethodType))}`,
      paymentMethodType,
    );
  }

  find(id: number): Observable<IPaymentMethodType> {
    return this.http.get<IPaymentMethodType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPaymentMethodType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentMethodType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaymentMethodTypeIdentifier(paymentMethodType: Pick<IPaymentMethodType, 'id'>): number {
    return paymentMethodType.id;
  }

  comparePaymentMethodType(o1: Pick<IPaymentMethodType, 'id'> | null, o2: Pick<IPaymentMethodType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentMethodTypeIdentifier(o1) === this.getPaymentMethodTypeIdentifier(o2) : o1 === o2;
  }

  addPaymentMethodTypeToCollectionIfMissing<Type extends Pick<IPaymentMethodType, 'id'>>(
    paymentMethodTypeCollection: Type[],
    ...paymentMethodTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentMethodTypes: Type[] = paymentMethodTypesToCheck.filter(
      paymentMethodTypeItem => paymentMethodTypeItem !== null && paymentMethodTypeItem !== undefined,
    );
    if (paymentMethodTypes.length > 0) {
      const paymentMethodTypeCollectionIdentifiers = paymentMethodTypeCollection.map(paymentMethodTypeItem =>
        this.getPaymentMethodTypeIdentifier(paymentMethodTypeItem),
      );
      const paymentMethodTypesToAdd = paymentMethodTypes.filter(paymentMethodTypeItem => {
        const paymentMethodTypeIdentifier = this.getPaymentMethodTypeIdentifier(paymentMethodTypeItem);
        if (paymentMethodTypeCollectionIdentifiers.includes(paymentMethodTypeIdentifier)) {
          return false;
        }
        paymentMethodTypeCollectionIdentifiers.push(paymentMethodTypeIdentifier);
        return true;
      });
      return [...paymentMethodTypesToAdd, ...paymentMethodTypeCollection];
    }
    return paymentMethodTypeCollection;
  }
}
