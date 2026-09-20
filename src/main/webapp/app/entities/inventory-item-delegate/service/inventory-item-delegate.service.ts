import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInventoryItemDelegate, NewInventoryItemDelegate } from '../inventory-item-delegate.model';

export type PartialUpdateInventoryItemDelegate = Partial<IInventoryItemDelegate> & Pick<IInventoryItemDelegate, 'id'>;

type RestOf<T extends IInventoryItemDelegate | NewInventoryItemDelegate> = Omit<T, 'effectiveDate'> & {
  effectiveDate?: string | null;
};

export type RestInventoryItemDelegate = RestOf<IInventoryItemDelegate>;

export type NewRestInventoryItemDelegate = RestOf<NewInventoryItemDelegate>;

export type PartialUpdateRestInventoryItemDelegate = RestOf<PartialUpdateInventoryItemDelegate>;

@Service()
export class InventoryItemDelegatesService {
  readonly inventoryItemDelegatesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly inventoryItemDelegatesResource = httpResource<RestInventoryItemDelegate[]>(() => {
    const params = this.inventoryItemDelegatesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of inventoryItemDelegate that have been fetched. It is updated when the inventoryItemDelegatesResource emits a new value.
   * In case of error while fetching the inventoryItemDelegates, the signal is set to an empty array.
   */
  readonly inventoryItemDelegates = computed(() =>
    (this.inventoryItemDelegatesResource.hasValue() ? this.inventoryItemDelegatesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/inventory-item-delegates`;

  protected convertValueFromServer(restInventoryItemDelegate: RestInventoryItemDelegate): IInventoryItemDelegate {
    return {
      ...restInventoryItemDelegate,
      effectiveDate: restInventoryItemDelegate.effectiveDate ? dayjs(restInventoryItemDelegate.effectiveDate) : undefined,
    };
  }
}

@Service()
export class InventoryItemDelegateService extends InventoryItemDelegatesService {
  protected readonly http = inject(HttpClient);

  create(inventoryItemDelegate: NewInventoryItemDelegate): Observable<IInventoryItemDelegate> {
    const copy = this.convertValueFromClient(inventoryItemDelegate);
    return this.http.post<RestInventoryItemDelegate>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inventoryItemDelegate: IInventoryItemDelegate): Observable<IInventoryItemDelegate> {
    const copy = this.convertValueFromClient(inventoryItemDelegate);
    return this.http
      .put<RestInventoryItemDelegate>(
        `${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemDelegateIdentifier(inventoryItemDelegate))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inventoryItemDelegate: PartialUpdateInventoryItemDelegate): Observable<IInventoryItemDelegate> {
    const copy = this.convertValueFromClient(inventoryItemDelegate);
    return this.http
      .patch<RestInventoryItemDelegate>(
        `${this.resourceUrl}/${encodeURIComponent(this.getInventoryItemDelegateIdentifier(inventoryItemDelegate))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IInventoryItemDelegate> {
    return this.http
      .get<RestInventoryItemDelegate>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IInventoryItemDelegate[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInventoryItemDelegate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInventoryItemDelegateIdentifier(inventoryItemDelegate: Pick<IInventoryItemDelegate, 'id'>): number {
    return inventoryItemDelegate.id;
  }

  compareInventoryItemDelegate(o1: Pick<IInventoryItemDelegate, 'id'> | null, o2: Pick<IInventoryItemDelegate, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventoryItemDelegateIdentifier(o1) === this.getInventoryItemDelegateIdentifier(o2) : o1 === o2;
  }

  addInventoryItemDelegateToCollectionIfMissing<Type extends Pick<IInventoryItemDelegate, 'id'>>(
    inventoryItemDelegateCollection: Type[],
    ...inventoryItemDelegatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventoryItemDelegates: Type[] = inventoryItemDelegatesToCheck.filter(
      inventoryItemDelegateItem => inventoryItemDelegateItem !== null && inventoryItemDelegateItem !== undefined,
    );
    if (inventoryItemDelegates.length > 0) {
      const inventoryItemDelegateCollectionIdentifiers = inventoryItemDelegateCollection.map(inventoryItemDelegateItem =>
        this.getInventoryItemDelegateIdentifier(inventoryItemDelegateItem),
      );
      const inventoryItemDelegatesToAdd = inventoryItemDelegates.filter(inventoryItemDelegateItem => {
        const inventoryItemDelegateIdentifier = this.getInventoryItemDelegateIdentifier(inventoryItemDelegateItem);
        if (inventoryItemDelegateCollectionIdentifiers.includes(inventoryItemDelegateIdentifier)) {
          return false;
        }
        inventoryItemDelegateCollectionIdentifiers.push(inventoryItemDelegateIdentifier);
        return true;
      });
      return [...inventoryItemDelegatesToAdd, ...inventoryItemDelegateCollection];
    }
    return inventoryItemDelegateCollection;
  }

  protected convertValueFromClient<T extends IInventoryItemDelegate | NewInventoryItemDelegate | PartialUpdateInventoryItemDelegate>(
    inventoryItemDelegate: T,
  ): RestOf<T> {
    return {
      ...inventoryItemDelegate,
      effectiveDate: inventoryItemDelegate.effectiveDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestInventoryItemDelegate): IInventoryItemDelegate {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestInventoryItemDelegate[]): IInventoryItemDelegate[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
