import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IReturnHeader, NewReturnHeader } from '../return-header.model';

export type PartialUpdateReturnHeader = Partial<IReturnHeader> & Pick<IReturnHeader, 'id'>;

@Service()
export class ReturnHeadersService {
  readonly returnHeadersParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly returnHeadersResource = httpResource<IReturnHeader[]>(() => {
    const params = this.returnHeadersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of returnHeader that have been fetched. It is updated when the returnHeadersResource emits a new value.
   * In case of error while fetching the returnHeaders, the signal is set to an empty array.
   */
  readonly returnHeaders = computed(() => (this.returnHeadersResource.hasValue() ? this.returnHeadersResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/return-headers`;
}

@Service()
export class ReturnHeaderService extends ReturnHeadersService {
  protected readonly http = inject(HttpClient);

  create(returnHeader: NewReturnHeader): Observable<IReturnHeader> {
    return this.http.post<IReturnHeader>(this.resourceUrl, returnHeader);
  }

  update(returnHeader: IReturnHeader): Observable<IReturnHeader> {
    return this.http.put<IReturnHeader>(
      `${this.resourceUrl}/${encodeURIComponent(this.getReturnHeaderIdentifier(returnHeader))}`,
      returnHeader,
    );
  }

  partialUpdate(returnHeader: PartialUpdateReturnHeader): Observable<IReturnHeader> {
    return this.http.patch<IReturnHeader>(
      `${this.resourceUrl}/${encodeURIComponent(this.getReturnHeaderIdentifier(returnHeader))}`,
      returnHeader,
    );
  }

  find(id: number): Observable<IReturnHeader> {
    return this.http.get<IReturnHeader>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IReturnHeader[]>> {
    const options = createRequestOption(req);
    return this.http.get<IReturnHeader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getReturnHeaderIdentifier(returnHeader: Pick<IReturnHeader, 'id'>): number {
    return returnHeader.id;
  }

  compareReturnHeader(o1: Pick<IReturnHeader, 'id'> | null, o2: Pick<IReturnHeader, 'id'> | null): boolean {
    return o1 && o2 ? this.getReturnHeaderIdentifier(o1) === this.getReturnHeaderIdentifier(o2) : o1 === o2;
  }

  addReturnHeaderToCollectionIfMissing<Type extends Pick<IReturnHeader, 'id'>>(
    returnHeaderCollection: Type[],
    ...returnHeadersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const returnHeaders: Type[] = returnHeadersToCheck.filter(
      returnHeaderItem => returnHeaderItem !== null && returnHeaderItem !== undefined,
    );
    if (returnHeaders.length > 0) {
      const returnHeaderCollectionIdentifiers = returnHeaderCollection.map(returnHeaderItem =>
        this.getReturnHeaderIdentifier(returnHeaderItem),
      );
      const returnHeadersToAdd = returnHeaders.filter(returnHeaderItem => {
        const returnHeaderIdentifier = this.getReturnHeaderIdentifier(returnHeaderItem);
        if (returnHeaderCollectionIdentifiers.includes(returnHeaderIdentifier)) {
          return false;
        }
        returnHeaderCollectionIdentifiers.push(returnHeaderIdentifier);
        return true;
      });
      return [...returnHeadersToAdd, ...returnHeaderCollection];
    }
    return returnHeaderCollection;
  }
}
