import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequestType, NewCustRequestType } from '../cust-request-type.model';

export type PartialUpdateCustRequestType = Partial<ICustRequestType> & Pick<ICustRequestType, 'id'>;

@Service()
export class CustRequestTypesService {
  readonly custRequestTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly custRequestTypesResource = httpResource<ICustRequestType[]>(() => {
    const params = this.custRequestTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequestType that have been fetched. It is updated when the custRequestTypesResource emits a new value.
   * In case of error while fetching the custRequestTypes, the signal is set to an empty array.
   */
  readonly custRequestTypes = computed(() => (this.custRequestTypesResource.hasValue() ? this.custRequestTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/cust-request-types`;
}

@Service()
export class CustRequestTypeService extends CustRequestTypesService {
  protected readonly http = inject(HttpClient);

  create(custRequestType: NewCustRequestType): Observable<ICustRequestType> {
    return this.http.post<ICustRequestType>(this.resourceUrl, custRequestType);
  }

  update(custRequestType: ICustRequestType): Observable<ICustRequestType> {
    return this.http.put<ICustRequestType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestTypeIdentifier(custRequestType))}`,
      custRequestType,
    );
  }

  partialUpdate(custRequestType: PartialUpdateCustRequestType): Observable<ICustRequestType> {
    return this.http.patch<ICustRequestType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestTypeIdentifier(custRequestType))}`,
      custRequestType,
    );
  }

  find(id: number): Observable<ICustRequestType> {
    return this.http.get<ICustRequestType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustRequestType[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustRequestType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestTypeIdentifier(custRequestType: Pick<ICustRequestType, 'id'>): number {
    return custRequestType.id;
  }

  compareCustRequestType(o1: Pick<ICustRequestType, 'id'> | null, o2: Pick<ICustRequestType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustRequestTypeIdentifier(o1) === this.getCustRequestTypeIdentifier(o2) : o1 === o2;
  }

  addCustRequestTypeToCollectionIfMissing<Type extends Pick<ICustRequestType, 'id'>>(
    custRequestTypeCollection: Type[],
    ...custRequestTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequestTypes: Type[] = custRequestTypesToCheck.filter(
      custRequestTypeItem => custRequestTypeItem !== null && custRequestTypeItem !== undefined,
    );
    if (custRequestTypes.length > 0) {
      const custRequestTypeCollectionIdentifiers = custRequestTypeCollection.map(custRequestTypeItem =>
        this.getCustRequestTypeIdentifier(custRequestTypeItem),
      );
      const custRequestTypesToAdd = custRequestTypes.filter(custRequestTypeItem => {
        const custRequestTypeIdentifier = this.getCustRequestTypeIdentifier(custRequestTypeItem);
        if (custRequestTypeCollectionIdentifiers.includes(custRequestTypeIdentifier)) {
          return false;
        }
        custRequestTypeCollectionIdentifiers.push(custRequestTypeIdentifier);
        return true;
      });
      return [...custRequestTypesToAdd, ...custRequestTypeCollection];
    }
    return custRequestTypeCollection;
  }
}
