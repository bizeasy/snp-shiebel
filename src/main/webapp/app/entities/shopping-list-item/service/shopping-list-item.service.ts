import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShoppingListItem, NewShoppingListItem } from '../shopping-list-item.model';

export type PartialUpdateShoppingListItem = Partial<IShoppingListItem> & Pick<IShoppingListItem, 'id'>;

type RestOf<T extends IShoppingListItem | NewShoppingListItem> = Omit<T, 'requiredByDate'> & {
  requiredByDate?: string | null;
};

export type RestShoppingListItem = RestOf<IShoppingListItem>;

export type NewRestShoppingListItem = RestOf<NewShoppingListItem>;

export type PartialUpdateRestShoppingListItem = RestOf<PartialUpdateShoppingListItem>;

@Service()
export class ShoppingListItemsService {
  readonly shoppingListItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shoppingListItemsResource = httpResource<RestShoppingListItem[]>(() => {
    const params = this.shoppingListItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shoppingListItem that have been fetched. It is updated when the shoppingListItemsResource emits a new value.
   * In case of error while fetching the shoppingListItems, the signal is set to an empty array.
   */
  readonly shoppingListItems = computed(() =>
    (this.shoppingListItemsResource.hasValue() ? this.shoppingListItemsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/shopping-list-items`;

  protected convertValueFromServer(restShoppingListItem: RestShoppingListItem): IShoppingListItem {
    return {
      ...restShoppingListItem,
      requiredByDate: restShoppingListItem.requiredByDate ? dayjs(restShoppingListItem.requiredByDate) : undefined,
    };
  }
}

@Service()
export class ShoppingListItemService extends ShoppingListItemsService {
  protected readonly http = inject(HttpClient);

  create(shoppingListItem: NewShoppingListItem): Observable<IShoppingListItem> {
    const copy = this.convertValueFromClient(shoppingListItem);
    return this.http.post<RestShoppingListItem>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(shoppingListItem: IShoppingListItem): Observable<IShoppingListItem> {
    const copy = this.convertValueFromClient(shoppingListItem);
    return this.http
      .put<RestShoppingListItem>(`${this.resourceUrl}/${encodeURIComponent(this.getShoppingListItemIdentifier(shoppingListItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(shoppingListItem: PartialUpdateShoppingListItem): Observable<IShoppingListItem> {
    const copy = this.convertValueFromClient(shoppingListItem);
    return this.http
      .patch<RestShoppingListItem>(`${this.resourceUrl}/${encodeURIComponent(this.getShoppingListItemIdentifier(shoppingListItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IShoppingListItem> {
    return this.http
      .get<RestShoppingListItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IShoppingListItem[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestShoppingListItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShoppingListItemIdentifier(shoppingListItem: Pick<IShoppingListItem, 'id'>): number {
    return shoppingListItem.id;
  }

  compareShoppingListItem(o1: Pick<IShoppingListItem, 'id'> | null, o2: Pick<IShoppingListItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getShoppingListItemIdentifier(o1) === this.getShoppingListItemIdentifier(o2) : o1 === o2;
  }

  addShoppingListItemToCollectionIfMissing<Type extends Pick<IShoppingListItem, 'id'>>(
    shoppingListItemCollection: Type[],
    ...shoppingListItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shoppingListItems: Type[] = shoppingListItemsToCheck.filter(
      shoppingListItemItem => shoppingListItemItem !== null && shoppingListItemItem !== undefined,
    );
    if (shoppingListItems.length > 0) {
      const shoppingListItemCollectionIdentifiers = shoppingListItemCollection.map(shoppingListItemItem =>
        this.getShoppingListItemIdentifier(shoppingListItemItem),
      );
      const shoppingListItemsToAdd = shoppingListItems.filter(shoppingListItemItem => {
        const shoppingListItemIdentifier = this.getShoppingListItemIdentifier(shoppingListItemItem);
        if (shoppingListItemCollectionIdentifiers.includes(shoppingListItemIdentifier)) {
          return false;
        }
        shoppingListItemCollectionIdentifiers.push(shoppingListItemIdentifier);
        return true;
      });
      return [...shoppingListItemsToAdd, ...shoppingListItemCollection];
    }
    return shoppingListItemCollection;
  }

  protected convertValueFromClient<T extends IShoppingListItem | NewShoppingListItem | PartialUpdateShoppingListItem>(
    shoppingListItem: T,
  ): RestOf<T> {
    return {
      ...shoppingListItem,
      requiredByDate: shoppingListItem.requiredByDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestShoppingListItem): IShoppingListItem {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestShoppingListItem[]): IShoppingListItem[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
