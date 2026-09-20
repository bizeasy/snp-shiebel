import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInventoryItem, NewInventoryItem } from '../inventory-item.model';

export type PartialUpdateInventoryItem = Partial<IInventoryItem> & Pick<IInventoryItem, 'id'>;

type RestOf<T extends IInventoryItem | NewInventoryItem> = Omit<
  T,
  'receivedDate' | 'manufacturedDate' | 'expiryDate' | 'retestDate' | 'activationValidThru'
> & {
  receivedDate?: string | null;
  manufacturedDate?: string | null;
  expiryDate?: string | null;
  retestDate?: string | null;
  activationValidThru?: string | null;
};

export type RestInventoryItem = RestOf<IInventoryItem>;

export type NewRestInventoryItem = RestOf<NewInventoryItem>;

export type PartialUpdateRestInventoryItem = RestOf<PartialUpdateInventoryItem>;

@Service()
export class InventoryItemsService {
  readonly inventoryItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly inventoryItemsResource = httpResource<RestInventoryItem[]>(() => {
    const params = this.inventoryItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of inventoryItem that have been fetched. It is updated when the inventoryItemsResource emits a new value.
   * In case of error while fetching the inventoryItems, the signal is set to an empty array.
   */
  readonly inventoryItems = computed(() =>
    (this.inventoryItemsResource.hasValue() ? this.inventoryItemsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/inventory-items`;

  protected convertValueFromServer(restInventoryItem: RestInventoryItem): IInventoryItem {
    return {
      ...restInventoryItem,
      receivedDate: restInventoryItem.receivedDate ? dayjs(restInventoryItem.receivedDate) : undefined,
      manufacturedDate: restInventoryItem.manufacturedDate ? dayjs(restInventoryItem.manufacturedDate) : undefined,
      expiryDate: restInventoryItem.expiryDate ? dayjs(restInventoryItem.expiryDate) : undefined,
      retestDate: restInventoryItem.retestDate ? dayjs(restInventoryItem.retestDate) : undefined,
      activationValidThru: restInventoryItem.activationValidThru ? dayjs(restInventoryItem.activationValidThru) : undefined,
    };
  }
}

@Service()
export class InventoryItemService extends InventoryItemsService {
  protected readonly http = inject(HttpClient);

  create(inventoryItem: NewInventoryItem): Observable<IInventoryItem> {
    const copy = this.convertValueFromClient(inventoryItem);
    return this.http.post<RestInventoryItem>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inventoryItem: IInventoryItem): Observable<IInventoryItem> {
    const copy = this.convertValueFromClient(inventoryItem);
    return this.http
      .put<RestInventoryItem>(`${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemIdentifier(inventoryItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inventoryItem: PartialUpdateInventoryItem): Observable<IInventoryItem> {
    const copy = this.convertValueFromClient(inventoryItem);
    return this.http
      .patch<RestInventoryItem>(`${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemIdentifier(inventoryItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IInventoryItem> {
    return this.http
      .get<RestInventoryItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IInventoryItem[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInventoryItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInventoryItemIdentifier(inventoryItem: Pick<IInventoryItem, 'id'>): number {
    return inventoryItem.id;
  }

  compareInventoryItem(o1: Pick<IInventoryItem, 'id'> | null, o2: Pick<IInventoryItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventoryItemIdentifier(o1) === this.getInventoryItemIdentifier(o2) : o1 === o2;
  }

  addInventoryItemToCollectionIfMissing<Type extends Pick<IInventoryItem, 'id'>>(
    inventoryItemCollection: Type[],
    ...inventoryItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventoryItems: Type[] = inventoryItemsToCheck.filter(
      inventoryItemItem => inventoryItemItem !== null && inventoryItemItem !== undefined,
    );
    if (inventoryItems.length > 0) {
      const inventoryItemCollectionIdentifiers = inventoryItemCollection.map(inventoryItemItem =>
        this.getInventoryItemIdentifier(inventoryItemItem),
      );
      const inventoryItemsToAdd = inventoryItems.filter(inventoryItemItem => {
        const inventoryItemIdentifier = this.getInventoryItemIdentifier(inventoryItemItem);
        if (inventoryItemCollectionIdentifiers.includes(inventoryItemIdentifier)) {
          return false;
        }
        inventoryItemCollectionIdentifiers.push(inventoryItemIdentifier);
        return true;
      });
      return [...inventoryItemsToAdd, ...inventoryItemCollection];
    }
    return inventoryItemCollection;
  }

  protected convertValueFromClient<T extends IInventoryItem | NewInventoryItem | PartialUpdateInventoryItem>(inventoryItem: T): RestOf<T> {
    return {
      ...inventoryItem,
      receivedDate: inventoryItem.receivedDate?.toJSON() ?? null,
      manufacturedDate: inventoryItem.manufacturedDate?.toJSON() ?? null,
      expiryDate: inventoryItem.expiryDate?.toJSON() ?? null,
      retestDate: inventoryItem.retestDate?.toJSON() ?? null,
      activationValidThru: inventoryItem.activationValidThru?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestInventoryItem): IInventoryItem {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestInventoryItem[]): IInventoryItem[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
