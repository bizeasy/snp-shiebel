import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityCarrierShipment, NewFacilityCarrierShipment } from '../facility-carrier-shipment.model';

export type PartialUpdateFacilityCarrierShipment = Partial<IFacilityCarrierShipment> & Pick<IFacilityCarrierShipment, 'id'>;

@Service()
export class FacilityCarrierShipmentsService {
  readonly facilityCarrierShipmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly facilityCarrierShipmentsResource = httpResource<IFacilityCarrierShipment[]>(() => {
    const params = this.facilityCarrierShipmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityCarrierShipment that have been fetched. It is updated when the facilityCarrierShipmentsResource emits a new value.
   * In case of error while fetching the facilityCarrierShipments, the signal is set to an empty array.
   */
  readonly facilityCarrierShipments = computed(() =>
    this.facilityCarrierShipmentsResource.hasValue() ? this.facilityCarrierShipmentsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/facility-carrier-shipments`;
}

@Service()
export class FacilityCarrierShipmentService extends FacilityCarrierShipmentsService {
  protected readonly http = inject(HttpClient);

  create(facilityCarrierShipment: NewFacilityCarrierShipment): Observable<IFacilityCarrierShipment> {
    return this.http.post<IFacilityCarrierShipment>(this.resourceUrl, facilityCarrierShipment);
  }

  update(facilityCarrierShipment: IFacilityCarrierShipment): Observable<IFacilityCarrierShipment> {
    return this.http.put<IFacilityCarrierShipment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityCarrierShipmentIdentifier(facilityCarrierShipment))}`,
      facilityCarrierShipment,
    );
  }

  partialUpdate(facilityCarrierShipment: PartialUpdateFacilityCarrierShipment): Observable<IFacilityCarrierShipment> {
    return this.http.patch<IFacilityCarrierShipment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityCarrierShipmentIdentifier(facilityCarrierShipment))}`,
      facilityCarrierShipment,
    );
  }

  find(id: number): Observable<IFacilityCarrierShipment> {
    return this.http.get<IFacilityCarrierShipment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFacilityCarrierShipment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFacilityCarrierShipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityCarrierShipmentIdentifier(facilityCarrierShipment: Pick<IFacilityCarrierShipment, 'id'>): number {
    return facilityCarrierShipment.id;
  }

  compareFacilityCarrierShipment(
    o1: Pick<IFacilityCarrierShipment, 'id'> | null,
    o2: Pick<IFacilityCarrierShipment, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getFacilityCarrierShipmentIdentifier(o1) === this.getFacilityCarrierShipmentIdentifier(o2) : o1 === o2;
  }

  addFacilityCarrierShipmentToCollectionIfMissing<Type extends Pick<IFacilityCarrierShipment, 'id'>>(
    facilityCarrierShipmentCollection: Type[],
    ...facilityCarrierShipmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityCarrierShipments: Type[] = facilityCarrierShipmentsToCheck.filter(
      facilityCarrierShipmentItem => facilityCarrierShipmentItem !== null && facilityCarrierShipmentItem !== undefined,
    );
    if (facilityCarrierShipments.length > 0) {
      const facilityCarrierShipmentCollectionIdentifiers = facilityCarrierShipmentCollection.map(facilityCarrierShipmentItem =>
        this.getFacilityCarrierShipmentIdentifier(facilityCarrierShipmentItem),
      );
      const facilityCarrierShipmentsToAdd = facilityCarrierShipments.filter(facilityCarrierShipmentItem => {
        const facilityCarrierShipmentIdentifier = this.getFacilityCarrierShipmentIdentifier(facilityCarrierShipmentItem);
        if (facilityCarrierShipmentCollectionIdentifiers.includes(facilityCarrierShipmentIdentifier)) {
          return false;
        }
        facilityCarrierShipmentCollectionIdentifiers.push(facilityCarrierShipmentIdentifier);
        return true;
      });
      return [...facilityCarrierShipmentsToAdd, ...facilityCarrierShipmentCollection];
    }
    return facilityCarrierShipmentCollection;
  }
}
