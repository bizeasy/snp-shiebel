import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaymentGatewayConfig, NewPaymentGatewayConfig } from '../payment-gateway-config.model';

export type PartialUpdatePaymentGatewayConfig = Partial<IPaymentGatewayConfig> & Pick<IPaymentGatewayConfig, 'id'>;

@Service()
export class PaymentGatewayConfigsService {
  readonly paymentGatewayConfigsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly paymentGatewayConfigsResource = httpResource<IPaymentGatewayConfig[]>(() => {
    const params = this.paymentGatewayConfigsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paymentGatewayConfig that have been fetched. It is updated when the paymentGatewayConfigsResource emits a new value.
   * In case of error while fetching the paymentGatewayConfigs, the signal is set to an empty array.
   */
  readonly paymentGatewayConfigs = computed(() =>
    this.paymentGatewayConfigsResource.hasValue() ? this.paymentGatewayConfigsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/payment-gateway-configs`;
}

@Service()
export class PaymentGatewayConfigService extends PaymentGatewayConfigsService {
  protected readonly http = inject(HttpClient);

  create(paymentGatewayConfig: NewPaymentGatewayConfig): Observable<IPaymentGatewayConfig> {
    return this.http.post<IPaymentGatewayConfig>(this.resourceUrl, paymentGatewayConfig);
  }

  update(paymentGatewayConfig: IPaymentGatewayConfig): Observable<IPaymentGatewayConfig> {
    return this.http.put<IPaymentGatewayConfig>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentGatewayConfigIdentifier(paymentGatewayConfig))}`,
      paymentGatewayConfig,
    );
  }

  partialUpdate(paymentGatewayConfig: PartialUpdatePaymentGatewayConfig): Observable<IPaymentGatewayConfig> {
    return this.http.patch<IPaymentGatewayConfig>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPaymentGatewayConfigIdentifier(paymentGatewayConfig))}`,
      paymentGatewayConfig,
    );
  }

  find(id: number): Observable<IPaymentGatewayConfig> {
    return this.http.get<IPaymentGatewayConfig>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPaymentGatewayConfig[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentGatewayConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaymentGatewayConfigIdentifier(paymentGatewayConfig: Pick<IPaymentGatewayConfig, 'id'>): number {
    return paymentGatewayConfig.id;
  }

  comparePaymentGatewayConfig(o1: Pick<IPaymentGatewayConfig, 'id'> | null, o2: Pick<IPaymentGatewayConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentGatewayConfigIdentifier(o1) === this.getPaymentGatewayConfigIdentifier(o2) : o1 === o2;
  }

  addPaymentGatewayConfigToCollectionIfMissing<Type extends Pick<IPaymentGatewayConfig, 'id'>>(
    paymentGatewayConfigCollection: Type[],
    ...paymentGatewayConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentGatewayConfigs: Type[] = paymentGatewayConfigsToCheck.filter(
      paymentGatewayConfigItem => paymentGatewayConfigItem !== null && paymentGatewayConfigItem !== undefined,
    );
    if (paymentGatewayConfigs.length > 0) {
      const paymentGatewayConfigCollectionIdentifiers = paymentGatewayConfigCollection.map(paymentGatewayConfigItem =>
        this.getPaymentGatewayConfigIdentifier(paymentGatewayConfigItem),
      );
      const paymentGatewayConfigsToAdd = paymentGatewayConfigs.filter(paymentGatewayConfigItem => {
        const paymentGatewayConfigIdentifier = this.getPaymentGatewayConfigIdentifier(paymentGatewayConfigItem);
        if (paymentGatewayConfigCollectionIdentifiers.includes(paymentGatewayConfigIdentifier)) {
          return false;
        }
        paymentGatewayConfigCollectionIdentifiers.push(paymentGatewayConfigIdentifier);
        return true;
      });
      return [...paymentGatewayConfigsToAdd, ...paymentGatewayConfigCollection];
    }
    return paymentGatewayConfigCollection;
  }
}
