import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShoppingListType, NewShoppingListType } from '../shopping-list-type.model';

export type PartialUpdateShoppingListType = Partial<IShoppingListType> & Pick<IShoppingListType, 'id'>;

@Service()
export class ShoppingListTypesService {
  readonly shoppingListTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shoppingListTypesResource = httpResource<IShoppingListType[]>(() => {
    const params = this.shoppingListTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shoppingListType that have been fetched. It is updated when the shoppingListTypesResource emits a new value.
   * In case of error while fetching the shoppingListTypes, the signal is set to an empty array.
   */
  readonly shoppingListTypes = computed(() => (this.shoppingListTypesResource.hasValue() ? this.shoppingListTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/shopping-list-types`;
}

@Service()
export class ShoppingListTypeService extends ShoppingListTypesService {
  protected readonly http = inject(HttpClient);

  create(shoppingListType: NewShoppingListType): Observable<IShoppingListType> {
    return this.http.post<IShoppingListType>(this.resourceUrl, shoppingListType);
  }

  update(shoppingListType: IShoppingListType): Observable<IShoppingListType> {
    return this.http.put<IShoppingListType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShoppingListTypeIdentifier(shoppingListType))}`,
      shoppingListType,
    );
  }

  partialUpdate(shoppingListType: PartialUpdateShoppingListType): Observable<IShoppingListType> {
    return this.http.patch<IShoppingListType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShoppingListTypeIdentifier(shoppingListType))}`,
      shoppingListType,
    );
  }

  find(id: number): Observable<IShoppingListType> {
    return this.http.get<IShoppingListType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IShoppingListType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IShoppingListType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShoppingListTypeIdentifier(shoppingListType: Pick<IShoppingListType, 'id'>): number {
    return shoppingListType.id;
  }

  compareShoppingListType(o1: Pick<IShoppingListType, 'id'> | null, o2: Pick<IShoppingListType, 'id'> | null): boolean {
    return o1 && o2 ? this.getShoppingListTypeIdentifier(o1) === this.getShoppingListTypeIdentifier(o2) : o1 === o2;
  }

  addShoppingListTypeToCollectionIfMissing<Type extends Pick<IShoppingListType, 'id'>>(
    shoppingListTypeCollection: Type[],
    ...shoppingListTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shoppingListTypes: Type[] = shoppingListTypesToCheck.filter(
      shoppingListTypeItem => shoppingListTypeItem !== null && shoppingListTypeItem !== undefined,
    );
    if (shoppingListTypes.length > 0) {
      const shoppingListTypeCollectionIdentifiers = shoppingListTypeCollection.map(shoppingListTypeItem =>
        this.getShoppingListTypeIdentifier(shoppingListTypeItem),
      );
      const shoppingListTypesToAdd = shoppingListTypes.filter(shoppingListTypeItem => {
        const shoppingListTypeIdentifier = this.getShoppingListTypeIdentifier(shoppingListTypeItem);
        if (shoppingListTypeCollectionIdentifiers.includes(shoppingListTypeIdentifier)) {
          return false;
        }
        shoppingListTypeCollectionIdentifiers.push(shoppingListTypeIdentifier);
        return true;
      });
      return [...shoppingListTypesToAdd, ...shoppingListTypeCollection];
    }
    return shoppingListTypeCollection;
  }
}
