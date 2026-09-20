import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaymentGatewayResponse, NewPaymentGatewayResponse } from '../payment-gateway-response.model';

export type PartialUpdatePaymentGatewayResponse = Partial<IPaymentGatewayResponse> & Pick<IPaymentGatewayResponse, 'id'>;

@Service()
export class PaymentGatewayResponsesService {
  readonly paymentGatewayResponsesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly paymentGatewayResponsesResource = httpResource<IPaymentGatewayResponse[]>(() => {
    const params = this.paymentGatewayResponsesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paymentGatewayResponse that have been fetched. It is updated when the paymentGatewayResponsesResource emits a new value.
   * In case of error while fetching the paymentGatewayResponses, the signal is set to an empty array.
   */
  readonly paymentGatewayResponses = computed(() =>
    this.paymentGatewayResponsesResource.hasValue() ? this.paymentGatewayResponsesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/payment-gateway-responses`;
}

@Service()
export class PaymentGatewayResponseService extends PaymentGatewayResponsesService {
  protected readonly http = inject(HttpClient);

  create(paymentGatewayResponse: NewPaymentGatewayResponse): Observable<IPaymentGatewayResponse> {
    return this.http.post<IPaymentGatewayResponse>(this.resourceUrl, paymentGatewayResponse);
  }

  update(paymentGatewayResponse: IPaymentGatewayResponse): Observable<IPaymentGatewayResponse> {
    return this.http.put<IPaymentGatewayResponse>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentGatewayResponseIdentifier(paymentGatewayResponse))}`,
      paymentGatewayResponse,
    );
  }

  partialUpdate(paymentGatewayResponse: PartialUpdatePaymentGatewayResponse): Observable<IPaymentGatewayResponse> {
    return this.http.patch<IPaymentGatewayResponse>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentGatewayResponseIdentifier(paymentGatewayResponse))}`,
      paymentGatewayResponse,
    );
  }

  find(id: number): Observable<IPaymentGatewayResponse> {
    return this.http.get<IPaymentGatewayResponse>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPaymentGatewayResponse[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentGatewayResponse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaymentGatewayResponseIdentifier(paymentGatewayResponse: Pick<IPaymentGatewayResponse, 'id'>): number {
    return paymentGatewayResponse.id;
  }

  comparePaymentGatewayResponse(o1: Pick<IPaymentGatewayResponse, 'id'> | null, o2: Pick<IPaymentGatewayResponse, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentGatewayResponseIdentifier(o1) === this.getPaymentGatewayResponseIdentifier(o2) : o1 === o2;
  }

  addPaymentGatewayResponseToCollectionIfMissing<Type extends Pick<IPaymentGatewayResponse, 'id'>>(
    paymentGatewayResponseCollection: Type[],
    ...paymentGatewayResponsesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentGatewayResponses: Type[] = paymentGatewayResponsesToCheck.filter(
      paymentGatewayResponseItem => paymentGatewayResponseItem !== null && paymentGatewayResponseItem !== undefined,
    );
    if (paymentGatewayResponses.length > 0) {
      const paymentGatewayResponseCollectionIdentifiers = paymentGatewayResponseCollection.map(paymentGatewayResponseItem =>
        this.getPaymentGatewayResponseIdentifier(paymentGatewayResponseItem),
      );
      const paymentGatewayResponsesToAdd = paymentGatewayResponses.filter(paymentGatewayResponseItem => {
        const paymentGatewayResponseIdentifier = this.getPaymentGatewayResponseIdentifier(paymentGatewayResponseItem);
        if (paymentGatewayResponseCollectionIdentifiers.includes(paymentGatewayResponseIdentifier)) {
          return false;
        }
        paymentGatewayResponseCollectionIdentifiers.push(paymentGatewayResponseIdentifier);
        return true;
      });
      return [...paymentGatewayResponsesToAdd, ...paymentGatewayResponseCollection];
    }
    return paymentGatewayResponseCollection;
  }
}
