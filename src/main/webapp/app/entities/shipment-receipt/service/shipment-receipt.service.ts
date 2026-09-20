import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipmentReceipt, NewShipmentReceipt } from '../shipment-receipt.model';

export type PartialUpdateShipmentReceipt = Partial<IShipmentReceipt> & Pick<IShipmentReceipt, 'id'>;

type RestOf<T extends IShipmentReceipt | NewShipmentReceipt> = Omit<T, 'receivedDate'> & {
  receivedDate?: string | null;
};

export type RestShipmentReceipt = RestOf<IShipmentReceipt>;

export type NewRestShipmentReceipt = RestOf<NewShipmentReceipt>;

export type PartialUpdateRestShipmentReceipt = RestOf<PartialUpdateShipmentReceipt>;

@Service()
export class ShipmentReceiptsService {
  readonly shipmentReceiptsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shipmentReceiptsResource = httpResource<RestShipmentReceipt[]>(() => {
    const params = this.shipmentReceiptsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipmentReceipt that have been fetched. It is updated when the shipmentReceiptsResource emits a new value.
   * In case of error while fetching the shipmentReceipts, the signal is set to an empty array.
   */
  readonly shipmentReceipts = computed(() =>
    (this.shipmentReceiptsResource.hasValue() ? this.shipmentReceiptsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/shipment-receipts`;

  protected convertValueFromServer(restShipmentReceipt: RestShipmentReceipt): IShipmentReceipt {
    return {
      ...restShipmentReceipt,
      receivedDate: restShipmentReceipt.receivedDate ? dayjs(restShipmentReceipt.receivedDate) : undefined,
    };
  }
}

@Service()
export class ShipmentReceiptService extends ShipmentReceiptsService {
  protected readonly http = inject(HttpClient);

  create(shipmentReceipt: NewShipmentReceipt): Observable<IShipmentReceipt> {
    const copy = this.convertValueFromClient(shipmentReceipt);
    return this.http.post<RestShipmentReceipt>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(shipmentReceipt: IShipmentReceipt): Observable<IShipmentReceipt> {
    const copy = this.convertValueFromClient(shipmentReceipt);
    return this.http
      .put<RestShipmentReceipt>(`${this.resourceUrl}/${encodeURIComponent(this.getShipmentReceiptIdentifier(shipmentReceipt))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(shipmentReceipt: PartialUpdateShipmentReceipt): Observable<IShipmentReceipt> {
    const copy = this.convertValueFromClient(shipmentReceipt);
    return this.http
      .patch<RestShipmentReceipt>(`${this.resourceUrl}/${encodeURIComponent(this.getShipmentReceiptIdentifier(shipmentReceipt))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IShipmentReceipt> {
    return this.http
      .get<RestShipmentReceipt>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IShipmentReceipt[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestShipmentReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentReceiptIdentifier(shipmentReceipt: Pick<IShipmentReceipt, 'id'>): number {
    return shipmentReceipt.id;
  }

  compareShipmentReceipt(o1: Pick<IShipmentReceipt, 'id'> | null, o2: Pick<IShipmentReceipt, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentReceiptIdentifier(o1) === this.getShipmentReceiptIdentifier(o2) : o1 === o2;
  }

  addShipmentReceiptToCollectionIfMissing<Type extends Pick<IShipmentReceipt, 'id'>>(
    shipmentReceiptCollection: Type[],
    ...shipmentReceiptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipmentReceipts: Type[] = shipmentReceiptsToCheck.filter(
      shipmentReceiptItem => shipmentReceiptItem !== null && shipmentReceiptItem !== undefined,
    );
    if (shipmentReceipts.length > 0) {
      const shipmentReceiptCollectionIdentifiers = shipmentReceiptCollection.map(shipmentReceiptItem =>
        this.getShipmentReceiptIdentifier(shipmentReceiptItem),
      );
      const shipmentReceiptsToAdd = shipmentReceipts.filter(shipmentReceiptItem => {
        const shipmentReceiptIdentifier = this.getShipmentReceiptIdentifier(shipmentReceiptItem);
        if (shipmentReceiptCollectionIdentifiers.includes(shipmentReceiptIdentifier)) {
          return false;
        }
        shipmentReceiptCollectionIdentifiers.push(shipmentReceiptIdentifier);
        return true;
      });
      return [...shipmentReceiptsToAdd, ...shipmentReceiptCollection];
    }
    return shipmentReceiptCollection;
  }

  protected convertValueFromClient<T extends IShipmentReceipt | NewShipmentReceipt | PartialUpdateShipmentReceipt>(
    shipmentReceipt: T,
  ): RestOf<T> {
    return {
      ...shipmentReceipt,
      receivedDate: shipmentReceipt.receivedDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestShipmentReceipt): IShipmentReceipt {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestShipmentReceipt[]): IShipmentReceipt[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
