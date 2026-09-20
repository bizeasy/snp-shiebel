import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequest, NewCustRequest } from '../cust-request.model';

export type PartialUpdateCustRequest = Partial<ICustRequest> & Pick<ICustRequest, 'id'>;

type RestOf<T extends ICustRequest | NewCustRequest> = Omit<
  T,
  'custRequestDate' | 'responseRequiredDate' | 'openDateTime' | 'closedDateTime'
> & {
  custRequestDate?: string | null;
  responseRequiredDate?: string | null;
  openDateTime?: string | null;
  closedDateTime?: string | null;
};

export type RestCustRequest = RestOf<ICustRequest>;

export type NewRestCustRequest = RestOf<NewCustRequest>;

export type PartialUpdateRestCustRequest = RestOf<PartialUpdateCustRequest>;

@Service()
export class CustRequestsService {
  readonly custRequestsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly custRequestsResource = httpResource<RestCustRequest[]>(() => {
    const params = this.custRequestsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequest that have been fetched. It is updated when the custRequestsResource emits a new value.
   * In case of error while fetching the custRequests, the signal is set to an empty array.
   */
  readonly custRequests = computed(() =>
    (this.custRequestsResource.hasValue() ? this.custRequestsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cust-requests`;

  protected convertValueFromServer(restCustRequest: RestCustRequest): ICustRequest {
    return {
      ...restCustRequest,
      custRequestDate: restCustRequest.custRequestDate ? dayjs(restCustRequest.custRequestDate) : undefined,
      responseRequiredDate: restCustRequest.responseRequiredDate ? dayjs(restCustRequest.responseRequiredDate) : undefined,
      openDateTime: restCustRequest.openDateTime ? dayjs(restCustRequest.openDateTime) : undefined,
      closedDateTime: restCustRequest.closedDateTime ? dayjs(restCustRequest.closedDateTime) : undefined,
    };
  }
}

@Service()
export class CustRequestService extends CustRequestsService {
  protected readonly http = inject(HttpClient);

  create(custRequest: NewCustRequest): Observable<ICustRequest> {
    const copy = this.convertValueFromClient(custRequest);
    return this.http.post<RestCustRequest>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(custRequest: ICustRequest): Observable<ICustRequest> {
    const copy = this.convertValueFromClient(custRequest);
    return this.http
      .put<RestCustRequest>(`${this.resourceUrl}/${encodeURIComponent(this.getCustRequestIdentifier(custRequest))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(custRequest: PartialUpdateCustRequest): Observable<ICustRequest> {
    const copy = this.convertValueFromClient(custRequest);
    return this.http
      .patch<RestCustRequest>(`${this.resourceUrl}/${encodeURIComponent(this.getCustRequestIdentifier(custRequest))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICustRequest> {
    return this.http
      .get<RestCustRequest>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICustRequest[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCustRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestIdentifier(custRequest: Pick<ICustRequest, 'id'>): number {
    return custRequest.id;
  }

  compareCustRequest(o1: Pick<ICustRequest, 'id'> | null, o2: Pick<ICustRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustRequestIdentifier(o1) === this.getCustRequestIdentifier(o2) : o1 === o2;
  }

  addCustRequestToCollectionIfMissing<Type extends Pick<ICustRequest, 'id'>>(
    custRequestCollection: Type[],
    ...custRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequests: Type[] = custRequestsToCheck.filter(custRequestItem => custRequestItem !== null && custRequestItem !== undefined);
    if (custRequests.length > 0) {
      const custRequestCollectionIdentifiers = custRequestCollection.map(custRequestItem => this.getCustRequestIdentifier(custRequestItem));
      const custRequestsToAdd = custRequests.filter(custRequestItem => {
        const custRequestIdentifier = this.getCustRequestIdentifier(custRequestItem);
        if (custRequestCollectionIdentifiers.includes(custRequestIdentifier)) {
          return false;
        }
        custRequestCollectionIdentifiers.push(custRequestIdentifier);
        return true;
      });
      return [...custRequestsToAdd, ...custRequestCollection];
    }
    return custRequestCollection;
  }

  protected convertValueFromClient<T extends ICustRequest | NewCustRequest | PartialUpdateCustRequest>(custRequest: T): RestOf<T> {
    return {
      ...custRequest,
      custRequestDate: custRequest.custRequestDate?.toJSON() ?? null,
      responseRequiredDate: custRequest.responseRequiredDate?.toJSON() ?? null,
      openDateTime: custRequest.openDateTime?.toJSON() ?? null,
      closedDateTime: custRequest.closedDateTime?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCustRequest): ICustRequest {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCustRequest[]): ICustRequest[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
