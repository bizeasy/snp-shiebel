import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaymentType, NewPaymentType } from '../payment-type.model';

export type PartialUpdatePaymentType = Partial<IPaymentType> & Pick<IPaymentType, 'id'>;

@Service()
export class PaymentTypesService {
  readonly paymentTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly paymentTypesResource = httpResource<IPaymentType[]>(() => {
    const params = this.paymentTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paymentType that have been fetched. It is updated when the paymentTypesResource emits a new value.
   * In case of error while fetching the paymentTypes, the signal is set to an empty array.
   */
  readonly paymentTypes = computed(() => (this.paymentTypesResource.hasValue() ? this.paymentTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/payment-types`;
}

@Service()
export class PaymentTypeService extends PaymentTypesService {
  protected readonly http = inject(HttpClient);

  create(paymentType: NewPaymentType): Observable<IPaymentType> {
    return this.http.post<IPaymentType>(this.resourceUrl, paymentType);
  }

  update(paymentType: IPaymentType): Observable<IPaymentType> {
    return this.http.put<IPaymentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentTypeIdentifier(paymentType))}`,
      paymentType,
    );
  }

  partialUpdate(paymentType: PartialUpdatePaymentType): Observable<IPaymentType> {
    return this.http.patch<IPaymentType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentTypeIdentifier(paymentType))}`,
      paymentType,
    );
  }

  find(id: number): Observable<IPaymentType> {
    return this.http.get<IPaymentType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPaymentType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaymentTypeIdentifier(paymentType: Pick<IPaymentType, 'id'>): number {
    return paymentType.id;
  }

  comparePaymentType(o1: Pick<IPaymentType, 'id'> | null, o2: Pick<IPaymentType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentTypeIdentifier(o1) === this.getPaymentTypeIdentifier(o2) : o1 === o2;
  }

  addPaymentTypeToCollectionIfMissing<Type extends Pick<IPaymentType, 'id'>>(
    paymentTypeCollection: Type[],
    ...paymentTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentTypes: Type[] = paymentTypesToCheck.filter(paymentTypeItem => paymentTypeItem !== null && paymentTypeItem !== undefined);
    if (paymentTypes.length > 0) {
      const paymentTypeCollectionIdentifiers = paymentTypeCollection.map(paymentTypeItem => this.getPaymentTypeIdentifier(paymentTypeItem));
      const paymentTypesToAdd = paymentTypes.filter(paymentTypeItem => {
        const paymentTypeIdentifier = this.getPaymentTypeIdentifier(paymentTypeItem);
        if (paymentTypeCollectionIdentifiers.includes(paymentTypeIdentifier)) {
          return false;
        }
        paymentTypeCollectionIdentifiers.push(paymentTypeIdentifier);
        return true;
      });
      return [...paymentTypesToAdd, ...paymentTypeCollection];
    }
    return paymentTypeCollection;
  }
}
