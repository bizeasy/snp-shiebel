import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaymentMethod, NewPaymentMethod } from '../payment-method.model';

export type PartialUpdatePaymentMethod = Partial<IPaymentMethod> & Pick<IPaymentMethod, 'id'>;

type RestOf<T extends IPaymentMethod | NewPaymentMethod> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

export type RestPaymentMethod = RestOf<IPaymentMethod>;

export type NewRestPaymentMethod = RestOf<NewPaymentMethod>;

export type PartialUpdateRestPaymentMethod = RestOf<PartialUpdatePaymentMethod>;

@Service()
export class PaymentMethodsService {
  readonly paymentMethodsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly paymentMethodsResource = httpResource<RestPaymentMethod[]>(() => {
    const params = this.paymentMethodsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paymentMethod that have been fetched. It is updated when the paymentMethodsResource emits a new value.
   * In case of error while fetching the paymentMethods, the signal is set to an empty array.
   */
  readonly paymentMethods = computed(() =>
    (this.paymentMethodsResource.hasValue() ? this.paymentMethodsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/payment-methods`;

  protected convertValueFromServer(restPaymentMethod: RestPaymentMethod): IPaymentMethod {
    return {
      ...restPaymentMethod,
      fromDate: restPaymentMethod.fromDate ? dayjs(restPaymentMethod.fromDate) : undefined,
      thruDate: restPaymentMethod.thruDate ? dayjs(restPaymentMethod.thruDate) : undefined,
    };
  }
}

@Service()
export class PaymentMethodService extends PaymentMethodsService {
  protected readonly http = inject(HttpClient);

  create(paymentMethod: NewPaymentMethod): Observable<IPaymentMethod> {
    const copy = this.convertValueFromClient(paymentMethod);
    return this.http.post<RestPaymentMethod>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(paymentMethod: IPaymentMethod): Observable<IPaymentMethod> {
    const copy = this.convertValueFromClient(paymentMethod);
    return this.http
      .put<RestPaymentMethod>(`${this.resourceUrl}/${encodeURIComponent(this.getPaymentMethodIdentifier(paymentMethod))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(paymentMethod: PartialUpdatePaymentMethod): Observable<IPaymentMethod> {
    const copy = this.convertValueFromClient(paymentMethod);
    return this.http
      .patch<RestPaymentMethod>(`${this.resourceUrl}/${encodeURIComponent(this.getPaymentMethodIdentifier(paymentMethod))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPaymentMethod> {
    return this.http
      .get<RestPaymentMethod>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPaymentMethod[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPaymentMethod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaymentMethodIdentifier(paymentMethod: Pick<IPaymentMethod, 'id'>): number {
    return paymentMethod.id;
  }

  comparePaymentMethod(o1: Pick<IPaymentMethod, 'id'> | null, o2: Pick<IPaymentMethod, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentMethodIdentifier(o1) === this.getPaymentMethodIdentifier(o2) : o1 === o2;
  }

  addPaymentMethodToCollectionIfMissing<Type extends Pick<IPaymentMethod, 'id'>>(
    paymentMethodCollection: Type[],
    ...paymentMethodsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentMethods: Type[] = paymentMethodsToCheck.filter(
      paymentMethodItem => paymentMethodItem !== null && paymentMethodItem !== undefined,
    );
    if (paymentMethods.length > 0) {
      const paymentMethodCollectionIdentifiers = paymentMethodCollection.map(paymentMethodItem =>
        this.getPaymentMethodIdentifier(paymentMethodItem),
      );
      const paymentMethodsToAdd = paymentMethods.filter(paymentMethodItem => {
        const paymentMethodIdentifier = this.getPaymentMethodIdentifier(paymentMethodItem);
        if (paymentMethodCollectionIdentifiers.includes(paymentMethodIdentifier)) {
          return false;
        }
        paymentMethodCollectionIdentifiers.push(paymentMethodIdentifier);
        return true;
      });
      return [...paymentMethodsToAdd, ...paymentMethodCollection];
    }
    return paymentMethodCollection;
  }

  protected convertValueFromClient<T extends IPaymentMethod | NewPaymentMethod | PartialUpdatePaymentMethod>(paymentMethod: T): RestOf<T> {
    return {
      ...paymentMethod,
      fromDate: paymentMethod.fromDate?.toJSON() ?? null,
      thruDate: paymentMethod.thruDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPaymentMethod): IPaymentMethod {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPaymentMethod[]): IPaymentMethod[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
