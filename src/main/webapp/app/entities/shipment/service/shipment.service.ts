import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipment, NewShipment } from '../shipment.model';

export type PartialUpdateShipment = Partial<IShipment> & Pick<IShipment, 'id'>;

type RestOf<T extends IShipment | NewShipment> = Omit<T, 'estimatedShipDate' | 'estimatedReadyDate' | 'estimatedArrivalDate'> & {
  estimatedShipDate?: string | null;
  estimatedReadyDate?: string | null;
  estimatedArrivalDate?: string | null;
};

export type RestShipment = RestOf<IShipment>;

export type NewRestShipment = RestOf<NewShipment>;

export type PartialUpdateRestShipment = RestOf<PartialUpdateShipment>;

@Service()
export class ShipmentsService {
  readonly shipmentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shipmentsResource = httpResource<RestShipment[]>(() => {
    const params = this.shipmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipment that have been fetched. It is updated when the shipmentsResource emits a new value.
   * In case of error while fetching the shipments, the signal is set to an empty array.
   */
  readonly shipments = computed(() =>
    (this.shipmentsResource.hasValue() ? this.shipmentsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/shipments`;

  protected convertValueFromServer(restShipment: RestShipment): IShipment {
    return {
      ...restShipment,
      estimatedShipDate: restShipment.estimatedShipDate ? dayjs(restShipment.estimatedShipDate) : undefined,
      estimatedReadyDate: restShipment.estimatedReadyDate ? dayjs(restShipment.estimatedReadyDate) : undefined,
      estimatedArrivalDate: restShipment.estimatedArrivalDate ? dayjs(restShipment.estimatedArrivalDate) : undefined,
    };
  }
}

@Service()
export class ShipmentService extends ShipmentsService {
  protected readonly http = inject(HttpClient);

  create(shipment: NewShipment): Observable<IShipment> {
    const copy = this.convertValueFromClient(shipment);
    return this.http.post<RestShipment>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(shipment: IShipment): Observable<IShipment> {
    const copy = this.convertValueFromClient(shipment);
    return this.http
      .put<RestShipment>(`${this.resourceUrl}/${encodeURIComponent(this.getShipmentIdentifier(shipment))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(shipment: PartialUpdateShipment): Observable<IShipment> {
    const copy = this.convertValueFromClient(shipment);
    return this.http
      .patch<RestShipment>(`${this.resourceUrl}/${encodeURIComponent(this.getShipmentIdentifier(shipment))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IShipment> {
    return this.http
      .get<RestShipment>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IShipment[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestShipment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentIdentifier(shipment: Pick<IShipment, 'id'>): number {
    return shipment.id;
  }

  compareShipment(o1: Pick<IShipment, 'id'> | null, o2: Pick<IShipment, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentIdentifier(o1) === this.getShipmentIdentifier(o2) : o1 === o2;
  }

  addShipmentToCollectionIfMissing<Type extends Pick<IShipment, 'id'>>(
    shipmentCollection: Type[],
    ...shipmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipments: Type[] = shipmentsToCheck.filter(shipmentItem => shipmentItem !== null && shipmentItem !== undefined);
    if (shipments.length > 0) {
      const shipmentCollectionIdentifiers = shipmentCollection.map(shipmentItem => this.getShipmentIdentifier(shipmentItem));
      const shipmentsToAdd = shipments.filter(shipmentItem => {
        const shipmentIdentifier = this.getShipmentIdentifier(shipmentItem);
        if (shipmentCollectionIdentifiers.includes(shipmentIdentifier)) {
          return false;
        }
        shipmentCollectionIdentifiers.push(shipmentIdentifier);
        return true;
      });
      return [...shipmentsToAdd, ...shipmentCollection];
    }
    return shipmentCollection;
  }

  protected convertValueFromClient<T extends IShipment | NewShipment | PartialUpdateShipment>(shipment: T): RestOf<T> {
    return {
      ...shipment,
      estimatedShipDate: shipment.estimatedShipDate?.toJSON() ?? null,
      estimatedReadyDate: shipment.estimatedReadyDate?.toJSON() ?? null,
      estimatedArrivalDate: shipment.estimatedArrivalDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestShipment): IShipment {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestShipment[]): IShipment[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
