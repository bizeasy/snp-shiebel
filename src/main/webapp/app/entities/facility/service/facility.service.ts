import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacility, NewFacility } from '../facility.model';

export type PartialUpdateFacility = Partial<IFacility> & Pick<IFacility, 'id'>;

@Service()
export class FacilitiesService {
  readonly facilitiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facilitiesResource = httpResource<IFacility[]>(() => {
    const params = this.facilitiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facility that have been fetched. It is updated when the facilitiesResource emits a new value.
   * In case of error while fetching the facilities, the signal is set to an empty array.
   */
  readonly facilities = computed(() => (this.facilitiesResource.hasValue() ? this.facilitiesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/facilities`;
}

@Service()
export class FacilityService extends FacilitiesService {
  protected readonly http = inject(HttpClient);

  create(facility: NewFacility): Observable<IFacility> {
    return this.http.post<IFacility>(this.resourceUrl, facility);
  }

  update(facility: IFacility): Observable<IFacility> {
    return this.http.put<IFacility>(`${this.resourceUrl}/${encodeURIComponent(this.getFacilityIdentifier(facility))}`, facility);
  }

  partialUpdate(facility: PartialUpdateFacility): Observable<IFacility> {
    return this.http.patch<IFacility>(`${this.resourceUrl}/${encodeURIComponent(this.getFacilityIdentifier(facility))}`, facility);
  }

  find(id: number): Observable<IFacility> {
    return this.http.get<IFacility>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFacility[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFacility[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityIdentifier(facility: Pick<IFacility, 'id'>): number {
    return facility.id;
  }

  compareFacility(o1: Pick<IFacility, 'id'> | null, o2: Pick<IFacility, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityIdentifier(o1) === this.getFacilityIdentifier(o2) : o1 === o2;
  }

  addFacilityToCollectionIfMissing<Type extends Pick<IFacility, 'id'>>(
    facilityCollection: Type[],
    ...facilitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilities: Type[] = facilitiesToCheck.filter(facilityItem => facilityItem !== null && facilityItem !== undefined);
    if (facilities.length > 0) {
      const facilityCollectionIdentifiers = facilityCollection.map(facilityItem => this.getFacilityIdentifier(facilityItem));
      const facilitiesToAdd = facilities.filter(facilityItem => {
        const facilityIdentifier = this.getFacilityIdentifier(facilityItem);
        if (facilityCollectionIdentifiers.includes(facilityIdentifier)) {
          return false;
        }
        facilityCollectionIdentifiers.push(facilityIdentifier);
        return true;
      });
      return [...facilitiesToAdd, ...facilityCollection];
    }
    return facilityCollection;
  }
}
