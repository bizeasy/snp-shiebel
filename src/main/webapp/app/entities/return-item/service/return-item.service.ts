import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IReturnItem, NewReturnItem } from '../return-item.model';

export type PartialUpdateReturnItem = Partial<IReturnItem> & Pick<IReturnItem, 'id'>;

@Service()
export class ReturnItemsService {
  readonly returnItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly returnItemsResource = httpResource<IReturnItem[]>(() => {
    const params = this.returnItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of returnItem that have been fetched. It is updated when the returnItemsResource emits a new value.
   * In case of error while fetching the returnItems, the signal is set to an empty array.
   */
  readonly returnItems = computed(() => (this.returnItemsResource.hasValue() ? this.returnItemsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/return-items`;
}

@Service()
export class ReturnItemService extends ReturnItemsService {
  protected readonly http = inject(HttpClient);

  create(returnItem: NewReturnItem): Observable<IReturnItem> {
    return this.http.post<IReturnItem>(this.resourceUrl, returnItem);
  }

  update(returnItem: IReturnItem): Observable<IReturnItem> {
    return this.http.put<IReturnItem>(`${this.resourceUrl}/${encodeURIComponent(this.getReturnItemIdentifier(returnItem))}`, returnItem);
  }

  partialUpdate(returnItem: PartialUpdateReturnItem): Observable<IReturnItem> {
    return this.http.patch<IReturnItem>(`${this.resourceUrl}/${encodeURIComponent(this.getReturnItemIdentifier(returnItem))}`, returnItem);
  }

  find(id: number): Observable<IReturnItem> {
    return this.http.get<IReturnItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IReturnItem[]>> {
    const options = createRequestOption(req);
    return this.http.get<IReturnItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getReturnItemIdentifier(returnItem: Pick<IReturnItem, 'id'>): number {
    return returnItem.id;
  }

  compareReturnItem(o1: Pick<IReturnItem, 'id'> | null, o2: Pick<IReturnItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getReturnItemIdentifier(o1) === this.getReturnItemIdentifier(o2) : o1 === o2;
  }

  addReturnItemToCollectionIfMissing<Type extends Pick<IReturnItem, 'id'>>(
    returnItemCollection: Type[],
    ...returnItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const returnItems: Type[] = returnItemsToCheck.filter(returnItemItem => returnItemItem !== null && returnItemItem !== undefined);
    if (returnItems.length > 0) {
      const returnItemCollectionIdentifiers = returnItemCollection.map(returnItemItem => this.getReturnItemIdentifier(returnItemItem));
      const returnItemsToAdd = returnItems.filter(returnItemItem => {
        const returnItemIdentifier = this.getReturnItemIdentifier(returnItemItem);
        if (returnItemCollectionIdentifiers.includes(returnItemIdentifier)) {
          return false;
        }
        returnItemCollectionIdentifiers.push(returnItemIdentifier);
        return true;
      });
      return [...returnItemsToAdd, ...returnItemCollection];
    }
    return returnItemCollection;
  }
}
