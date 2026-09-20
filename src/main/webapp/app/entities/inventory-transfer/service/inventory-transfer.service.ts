import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInventoryTransfer, NewInventoryTransfer } from '../inventory-transfer.model';

export type PartialUpdateInventoryTransfer = Partial<IInventoryTransfer> & Pick<IInventoryTransfer, 'id'>;

type RestOf<T extends IInventoryTransfer | NewInventoryTransfer> = Omit<T, 'sentDate' | 'receivedDate'> & {
  sentDate?: string | null;
  receivedDate?: string | null;
};

export type RestInventoryTransfer = RestOf<IInventoryTransfer>;

export type NewRestInventoryTransfer = RestOf<NewInventoryTransfer>;

export type PartialUpdateRestInventoryTransfer = RestOf<PartialUpdateInventoryTransfer>;

@Service()
export class InventoryTransfersService {
  readonly inventoryTransfersParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly inventoryTransfersResource = httpResource<RestInventoryTransfer[]>(() => {
    const params = this.inventoryTransfersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of inventoryTransfer that have been fetched. It is updated when the inventoryTransfersResource emits a new value.
   * In case of error while fetching the inventoryTransfers, the signal is set to an empty array.
   */
  readonly inventoryTransfers = computed(() =>
    (this.inventoryTransfersResource.hasValue() ? this.inventoryTransfersResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/inventory-transfers`;

  protected convertValueFromServer(restInventoryTransfer: RestInventoryTransfer): IInventoryTransfer {
    return {
      ...restInventoryTransfer,
      sentDate: restInventoryTransfer.sentDate ? dayjs(restInventoryTransfer.sentDate) : undefined,
      receivedDate: restInventoryTransfer.receivedDate ? dayjs(restInventoryTransfer.receivedDate) : undefined,
    };
  }
}

@Service()
export class InventoryTransferService extends InventoryTransfersService {
  protected readonly http = inject(HttpClient);

  create(inventoryTransfer: NewInventoryTransfer): Observable<IInventoryTransfer> {
    const copy = this.convertValueFromClient(inventoryTransfer);
    return this.http.post<RestInventoryTransfer>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inventoryTransfer: IInventoryTransfer): Observable<IInventoryTransfer> {
    const copy = this.convertValueFromClient(inventoryTransfer);
    return this.http
      .put<RestInventoryTransfer>(`${this.resourceUrl}/${encodeURIComponent(this.getInventoryTransferIdentifier(inventoryTransfer))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inventoryTransfer: PartialUpdateInventoryTransfer): Observable<IInventoryTransfer> {
    const copy = this.convertValueFromClient(inventoryTransfer);
    return this.http
      .patch<RestInventoryTransfer>(
        `${this.resourceUrl}/${encodeURIComponent(this.getInventoryTransferIdentifier(inventoryTransfer))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IInventoryTransfer> {
    return this.http
      .get<RestInventoryTransfer>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IInventoryTransfer[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInventoryTransfer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInventoryTransferIdentifier(inventoryTransfer: Pick<IInventoryTransfer, 'id'>): number {
    return inventoryTransfer.id;
  }

  compareInventoryTransfer(o1: Pick<IInventoryTransfer, 'id'> | null, o2: Pick<IInventoryTransfer, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventoryTransferIdentifier(o1) === this.getInventoryTransferIdentifier(o2) : o1 === o2;
  }

  addInventoryTransferToCollectionIfMissing<Type extends Pick<IInventoryTransfer, 'id'>>(
    inventoryTransferCollection: Type[],
    ...inventoryTransfersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventoryTransfers: Type[] = inventoryTransfersToCheck.filter(
      inventoryTransferItem => inventoryTransferItem !== null && inventoryTransferItem !== undefined,
    );
    if (inventoryTransfers.length > 0) {
      const inventoryTransferCollectionIdentifiers = inventoryTransferCollection.map(inventoryTransferItem =>
        this.getInventoryTransferIdentifier(inventoryTransferItem),
      );
      const inventoryTransfersToAdd = inventoryTransfers.filter(inventoryTransferItem => {
        const inventoryTransferIdentifier = this.getInventoryTransferIdentifier(inventoryTransferItem);
        if (inventoryTransferCollectionIdentifiers.includes(inventoryTransferIdentifier)) {
          return false;
        }
        inventoryTransferCollectionIdentifiers.push(inventoryTransferIdentifier);
        return true;
      });
      return [...inventoryTransfersToAdd, ...inventoryTransferCollection];
    }
    return inventoryTransferCollection;
  }

  protected convertValueFromClient<T extends IInventoryTransfer | NewInventoryTransfer | PartialUpdateInventoryTransfer>(
    inventoryTransfer: T,
  ): RestOf<T> {
    return {
      ...inventoryTransfer,
      sentDate: inventoryTransfer.sentDate?.toJSON() ?? null,
      receivedDate: inventoryTransfer.receivedDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestInventoryTransfer): IInventoryTransfer {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestInventoryTransfer[]): IInventoryTransfer[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
