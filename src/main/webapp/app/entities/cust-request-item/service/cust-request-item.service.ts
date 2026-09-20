import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequestItem, NewCustRequestItem } from '../cust-request-item.model';

export type PartialUpdateCustRequestItem = Partial<ICustRequestItem> & Pick<ICustRequestItem, 'id'>;

type RestOf<T extends ICustRequestItem | NewCustRequestItem> = Omit<T, 'requiredByDate'> & {
  requiredByDate?: string | null;
};

export type RestCustRequestItem = RestOf<ICustRequestItem>;

export type NewRestCustRequestItem = RestOf<NewCustRequestItem>;

export type PartialUpdateRestCustRequestItem = RestOf<PartialUpdateCustRequestItem>;

@Service()
export class CustRequestItemsService {
  readonly custRequestItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly custRequestItemsResource = httpResource<RestCustRequestItem[]>(() => {
    const params = this.custRequestItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequestItem that have been fetched. It is updated when the custRequestItemsResource emits a new value.
   * In case of error while fetching the custRequestItems, the signal is set to an empty array.
   */
  readonly custRequestItems = computed(() =>
    (this.custRequestItemsResource.hasValue() ? this.custRequestItemsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cust-request-items`;

  protected convertValueFromServer(restCustRequestItem: RestCustRequestItem): ICustRequestItem {
    return {
      ...restCustRequestItem,
      requiredByDate: restCustRequestItem.requiredByDate ? dayjs(restCustRequestItem.requiredByDate) : undefined,
    };
  }
}

@Service()
export class CustRequestItemService extends CustRequestItemsService {
  protected readonly http = inject(HttpClient);

  create(custRequestItem: NewCustRequestItem): Observable<ICustRequestItem> {
    const copy = this.convertValueFromClient(custRequestItem);
    return this.http.post<RestCustRequestItem>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(custRequestItem: ICustRequestItem): Observable<ICustRequestItem> {
    const copy = this.convertValueFromClient(custRequestItem);
    return this.http
      .put<RestCustRequestItem>(`${this.resourceUrl}/${encodeURIComponent(this.getCustRequestItemIdentifier(custRequestItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(custRequestItem: PartialUpdateCustRequestItem): Observable<ICustRequestItem> {
    const copy = this.convertValueFromClient(custRequestItem);
    return this.http
      .patch<RestCustRequestItem>(`${this.resourceUrl}/${encodeURIComponent(this.getCustRequestItemIdentifier(custRequestItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICustRequestItem> {
    return this.http
      .get<RestCustRequestItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICustRequestItem[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCustRequestItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestItemIdentifier(custRequestItem: Pick<ICustRequestItem, 'id'>): number {
    return custRequestItem.id;
  }

  compareCustRequestItem(o1: Pick<ICustRequestItem, 'id'> | null, o2: Pick<ICustRequestItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustRequestItemIdentifier(o1) === this.getCustRequestItemIdentifier(o2) : o1 === o2;
  }

  addCustRequestItemToCollectionIfMissing<Type extends Pick<ICustRequestItem, 'id'>>(
    custRequestItemCollection: Type[],
    ...custRequestItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequestItems: Type[] = custRequestItemsToCheck.filter(
      custRequestItemItem => custRequestItemItem !== null && custRequestItemItem !== undefined,
    );
    if (custRequestItems.length > 0) {
      const custRequestItemCollectionIdentifiers = custRequestItemCollection.map(custRequestItemItem =>
        this.getCustRequestItemIdentifier(custRequestItemItem),
      );
      const custRequestItemsToAdd = custRequestItems.filter(custRequestItemItem => {
        const custRequestItemIdentifier = this.getCustRequestItemIdentifier(custRequestItemItem);
        if (custRequestItemCollectionIdentifiers.includes(custRequestItemIdentifier)) {
          return false;
        }
        custRequestItemCollectionIdentifiers.push(custRequestItemIdentifier);
        return true;
      });
      return [...custRequestItemsToAdd, ...custRequestItemCollection];
    }
    return custRequestItemCollection;
  }

  protected convertValueFromClient<T extends ICustRequestItem | NewCustRequestItem | PartialUpdateCustRequestItem>(
    custRequestItem: T,
  ): RestOf<T> {
    return {
      ...custRequestItem,
      requiredByDate: custRequestItem.requiredByDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCustRequestItem): ICustRequestItem {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCustRequestItem[]): ICustRequestItem[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
