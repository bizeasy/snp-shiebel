import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaymentApplication, NewPaymentApplication } from '../payment-application.model';

export type PartialUpdatePaymentApplication = Partial<IPaymentApplication> & Pick<IPaymentApplication, 'id'>;

@Service()
export class PaymentApplicationsService {
  readonly paymentApplicationsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly paymentApplicationsResource = httpResource<IPaymentApplication[]>(() => {
    const params = this.paymentApplicationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paymentApplication that have been fetched. It is updated when the paymentApplicationsResource emits a new value.
   * In case of error while fetching the paymentApplications, the signal is set to an empty array.
   */
  readonly paymentApplications = computed(() =>
    this.paymentApplicationsResource.hasValue() ? this.paymentApplicationsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/payment-applications`;
}

@Service()
export class PaymentApplicationService extends PaymentApplicationsService {
  protected readonly http = inject(HttpClient);

  create(paymentApplication: NewPaymentApplication): Observable<IPaymentApplication> {
    return this.http.post<IPaymentApplication>(this.resourceUrl, paymentApplication);
  }

  update(paymentApplication: IPaymentApplication): Observable<IPaymentApplication> {
    return this.http.put<IPaymentApplication>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentApplicationIdentifier(paymentApplication))}`,
      paymentApplication,
    );
  }

  partialUpdate(paymentApplication: PartialUpdatePaymentApplication): Observable<IPaymentApplication> {
    return this.http.patch<IPaymentApplication>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentApplicationIdentifier(paymentApplication))}`,
      paymentApplication,
    );
  }

  find(id: number): Observable<IPaymentApplication> {
    return this.http.get<IPaymentApplication>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPaymentApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentApplication[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaymentApplicationIdentifier(paymentApplication: Pick<IPaymentApplication, 'id'>): number {
    return paymentApplication.id;
  }

  comparePaymentApplication(o1: Pick<IPaymentApplication, 'id'> | null, o2: Pick<IPaymentApplication, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentApplicationIdentifier(o1) === this.getPaymentApplicationIdentifier(o2) : o1 === o2;
  }

  addPaymentApplicationToCollectionIfMissing<Type extends Pick<IPaymentApplication, 'id'>>(
    paymentApplicationCollection: Type[],
    ...paymentApplicationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentApplications: Type[] = paymentApplicationsToCheck.filter(
      paymentApplicationItem => paymentApplicationItem !== null && paymentApplicationItem !== undefined,
    );
    if (paymentApplications.length > 0) {
      const paymentApplicationCollectionIdentifiers = paymentApplicationCollection.map(paymentApplicationItem =>
        this.getPaymentApplicationIdentifier(paymentApplicationItem),
      );
      const paymentApplicationsToAdd = paymentApplications.filter(paymentApplicationItem => {
        const paymentApplicationIdentifier = this.getPaymentApplicationIdentifier(paymentApplicationItem);
        if (paymentApplicationCollectionIdentifiers.includes(paymentApplicationIdentifier)) {
          return false;
        }
        paymentApplicationCollectionIdentifiers.push(paymentApplicationIdentifier);
        return true;
      });
      return [...paymentApplicationsToAdd, ...paymentApplicationCollection];
    }
    return paymentApplicationCollection;
  }
}
