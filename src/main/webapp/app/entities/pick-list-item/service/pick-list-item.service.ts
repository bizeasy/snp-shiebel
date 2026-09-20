import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPickListItem, NewPickListItem } from '../pick-list-item.model';

export type PartialUpdatePickListItem = Partial<IPickListItem> & Pick<IPickListItem, 'id'>;

@Service()
export class PickListItemsService {
  readonly pickListItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly pickListItemsResource = httpResource<IPickListItem[]>(() => {
    const params = this.pickListItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of pickListItem that have been fetched. It is updated when the pickListItemsResource emits a new value.
   * In case of error while fetching the pickListItems, the signal is set to an empty array.
   */
  readonly pickListItems = computed(() => (this.pickListItemsResource.hasValue() ? this.pickListItemsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/pick-list-items`;
}

@Service()
export class PickListItemService extends PickListItemsService {
  protected readonly http = inject(HttpClient);

  create(pickListItem: NewPickListItem): Observable<IPickListItem> {
    return this.http.post<IPickListItem>(this.resourceUrl, pickListItem);
  }

  update(pickListItem: IPickListItem): Observable<IPickListItem> {
    return this.http.put<IPickListItem>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPickListItemIdentifier(pickListItem))}`,
      pickListItem,
    );
  }

  partialUpdate(pickListItem: PartialUpdatePickListItem): Observable<IPickListItem> {
    return this.http.patch<IPickListItem>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPickListItemIdentifier(pickListItem))}`,
      pickListItem,
    );
  }

  find(id: number): Observable<IPickListItem> {
    return this.http.get<IPickListItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPickListItem[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPickListItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPickListItemIdentifier(pickListItem: Pick<IPickListItem, 'id'>): number {
    return pickListItem.id;
  }

  comparePickListItem(o1: Pick<IPickListItem, 'id'> | null, o2: Pick<IPickListItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getPickListItemIdentifier(o1) === this.getPickListItemIdentifier(o2) : o1 === o2;
  }

  addPickListItemToCollectionIfMissing<Type extends Pick<IPickListItem, 'id'>>(
    pickListItemCollection: Type[],
    ...pickListItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pickListItems: Type[] = pickListItemsToCheck.filter(
      pickListItemItem => pickListItemItem !== null && pickListItemItem !== undefined,
    );
    if (pickListItems.length > 0) {
      const pickListItemCollectionIdentifiers = pickListItemCollection.map(pickListItemItem =>
        this.getPickListItemIdentifier(pickListItemItem),
      );
      const pickListItemsToAdd = pickListItems.filter(pickListItemItem => {
        const pickListItemIdentifier = this.getPickListItemIdentifier(pickListItemItem);
        if (pickListItemCollectionIdentifiers.includes(pickListItemIdentifier)) {
          return false;
        }
        pickListItemCollectionIdentifiers.push(pickListItemIdentifier);
        return true;
      });
      return [...pickListItemsToAdd, ...pickListItemCollection];
    }
    return pickListItemCollection;
  }
}
