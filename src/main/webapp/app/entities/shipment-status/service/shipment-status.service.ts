import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipmentStatus, NewShipmentStatus } from '../shipment-status.model';

export type PartialUpdateShipmentStatus = Partial<IShipmentStatus> & Pick<IShipmentStatus, 'id'>;

type RestOf<T extends IShipmentStatus | NewShipmentStatus> = Omit<T, 'statusDateTime'> & {
  statusDateTime?: string | null;
};

export type RestShipmentStatus = RestOf<IShipmentStatus>;

export type NewRestShipmentStatus = RestOf<NewShipmentStatus>;

export type PartialUpdateRestShipmentStatus = RestOf<PartialUpdateShipmentStatus>;

@Service()
export class ShipmentStatusesService {
  readonly shipmentStatusesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly shipmentStatusesResource = httpResource<RestShipmentStatus[]>(() => {
    const params = this.shipmentStatusesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipmentStatus that have been fetched. It is updated when the shipmentStatusesResource emits a new value.
   * In case of error while fetching the shipmentStatuses, the signal is set to an empty array.
   */
  readonly shipmentStatuses = computed(() =>
    (this.shipmentStatusesResource.hasValue() ? this.shipmentStatusesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/shipment-statuses`;

  protected convertValueFromServer(restShipmentStatus: RestShipmentStatus): IShipmentStatus {
    return {
      ...restShipmentStatus,
      statusDateTime: restShipmentStatus.statusDateTime ? dayjs(restShipmentStatus.statusDateTime) : undefined,
    };
  }
}

@Service()
export class ShipmentStatusService extends ShipmentStatusesService {
  protected readonly http = inject(HttpClient);

  create(shipmentStatus: NewShipmentStatus): Observable<IShipmentStatus> {
    const copy = this.convertValueFromClient(shipmentStatus);
    return this.http.post<RestShipmentStatus>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(shipmentStatus: IShipmentStatus): Observable<IShipmentStatus> {
    const copy = this.convertValueFromClient(shipmentStatus);
    return this.http
      .put<RestShipmentStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getShipmentStatusIdentifier(shipmentStatus))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(shipmentStatus: PartialUpdateShipmentStatus): Observable<IShipmentStatus> {
    const copy = this.convertValueFromClient(shipmentStatus);
    return this.http
      .patch<RestShipmentStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getShipmentStatusIdentifier(shipmentStatus))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IShipmentStatus> {
    return this.http
      .get<RestShipmentStatus>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IShipmentStatus[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestShipmentStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentStatusIdentifier(shipmentStatus: Pick<IShipmentStatus, 'id'>): number {
    return shipmentStatus.id;
  }

  compareShipmentStatus(o1: Pick<IShipmentStatus, 'id'> | null, o2: Pick<IShipmentStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentStatusIdentifier(o1) === this.getShipmentStatusIdentifier(o2) : o1 === o2;
  }

  addShipmentStatusToCollectionIfMissing<Type extends Pick<IShipmentStatus, 'id'>>(
    shipmentStatusCollection: Type[],
    ...shipmentStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipmentStatuses: Type[] = shipmentStatusesToCheck.filter(
      shipmentStatusItem => shipmentStatusItem !== null && shipmentStatusItem !== undefined,
    );
    if (shipmentStatuses.length > 0) {
      const shipmentStatusCollectionIdentifiers = shipmentStatusCollection.map(shipmentStatusItem =>
        this.getShipmentStatusIdentifier(shipmentStatusItem),
      );
      const shipmentStatusesToAdd = shipmentStatuses.filter(shipmentStatusItem => {
        const shipmentStatusIdentifier = this.getShipmentStatusIdentifier(shipmentStatusItem);
        if (shipmentStatusCollectionIdentifiers.includes(shipmentStatusIdentifier)) {
          return false;
        }
        shipmentStatusCollectionIdentifiers.push(shipmentStatusIdentifier);
        return true;
      });
      return [...shipmentStatusesToAdd, ...shipmentStatusCollection];
    }
    return shipmentStatusCollection;
  }

  protected convertValueFromClient<T extends IShipmentStatus | NewShipmentStatus | PartialUpdateShipmentStatus>(
    shipmentStatus: T,
  ): RestOf<T> {
    return {
      ...shipmentStatus,
      statusDateTime: shipmentStatus.statusDateTime?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestShipmentStatus): IShipmentStatus {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestShipmentStatus[]): IShipmentStatus[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
