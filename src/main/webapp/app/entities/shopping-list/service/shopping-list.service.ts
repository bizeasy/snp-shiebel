import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShoppingList, NewShoppingList } from '../shopping-list.model';

export type PartialUpdateShoppingList = Partial<IShoppingList> & Pick<IShoppingList, 'id'>;

@Service()
export class ShoppingListsService {
  readonly shoppingListsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shoppingListsResource = httpResource<IShoppingList[]>(() => {
    const params = this.shoppingListsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shoppingList that have been fetched. It is updated when the shoppingListsResource emits a new value.
   * In case of error while fetching the shoppingLists, the signal is set to an empty array.
   */
  readonly shoppingLists = computed(() => (this.shoppingListsResource.hasValue() ? this.shoppingListsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/shopping-lists`;
}

@Service()
export class ShoppingListService extends ShoppingListsService {
  protected readonly http = inject(HttpClient);

  create(shoppingList: NewShoppingList): Observable<IShoppingList> {
    return this.http.post<IShoppingList>(this.resourceUrl, shoppingList);
  }

  update(shoppingList: IShoppingList): Observable<IShoppingList> {
    return this.http.put<IShoppingList>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShoppingListIdentifier(shoppingList))}`,
      shoppingList,
    );
  }

  partialUpdate(shoppingList: PartialUpdateShoppingList): Observable<IShoppingList> {
    return this.http.patch<IShoppingList>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShoppingListIdentifier(shoppingList))}`,
      shoppingList,
    );
  }

  find(id: number): Observable<IShoppingList> {
    return this.http.get<IShoppingList>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IShoppingList[]>> {
    const options = createRequestOption(req);
    return this.http.get<IShoppingList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShoppingListIdentifier(shoppingList: Pick<IShoppingList, 'id'>): number {
    return shoppingList.id;
  }

  compareShoppingList(o1: Pick<IShoppingList, 'id'> | null, o2: Pick<IShoppingList, 'id'> | null): boolean {
    return o1 && o2 ? this.getShoppingListIdentifier(o1) === this.getShoppingListIdentifier(o2) : o1 === o2;
  }

  addShoppingListToCollectionIfMissing<Type extends Pick<IShoppingList, 'id'>>(
    shoppingListCollection: Type[],
    ...shoppingListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shoppingLists: Type[] = shoppingListsToCheck.filter(
      shoppingListItem => shoppingListItem !== null && shoppingListItem !== undefined,
    );
    if (shoppingLists.length > 0) {
      const shoppingListCollectionIdentifiers = shoppingListCollection.map(shoppingListItem =>
        this.getShoppingListIdentifier(shoppingListItem),
      );
      const shoppingListsToAdd = shoppingLists.filter(shoppingListItem => {
        const shoppingListIdentifier = this.getShoppingListIdentifier(shoppingListItem);
        if (shoppingListCollectionIdentifiers.includes(shoppingListIdentifier)) {
          return false;
        }
        shoppingListCollectionIdentifiers.push(shoppingListIdentifier);
        return true;
      });
      return [...shoppingListsToAdd, ...shoppingListCollection];
    }
    return shoppingListCollection;
  }
}
