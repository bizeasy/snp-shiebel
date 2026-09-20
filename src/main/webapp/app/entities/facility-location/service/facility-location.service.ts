import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityLocation, NewFacilityLocation } from '../facility-location.model';

export type PartialUpdateFacilityLocation = Partial<IFacilityLocation> & Pick<IFacilityLocation, 'id'>;

@Service()
export class FacilityLocationsService {
  readonly facilityLocationsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facilityLocationsResource = httpResource<IFacilityLocation[]>(() => {
    const params = this.facilityLocationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityLocation that have been fetched. It is updated when the facilityLocationsResource emits a new value.
   * In case of error while fetching the facilityLocations, the signal is set to an empty array.
   */
  readonly facilityLocations = computed(() => (this.facilityLocationsResource.hasValue() ? this.facilityLocationsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/facility-locations`;
}

@Service()
export class FacilityLocationService extends FacilityLocationsService {
  protected readonly http = inject(HttpClient);

  create(facilityLocation: NewFacilityLocation): Observable<IFacilityLocation> {
    return this.http.post<IFacilityLocation>(this.resourceUrl, facilityLocation);
  }

  update(facilityLocation: IFacilityLocation): Observable<IFacilityLocation> {
    return this.http.put<IFacilityLocation>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityLocationIdentifier(facilityLocation))}`,
      facilityLocation,
    );
  }

  partialUpdate(facilityLocation: PartialUpdateFacilityLocation): Observable<IFacilityLocation> {
    return this.http.patch<IFacilityLocation>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityLocationIdentifier(facilityLocation))}`,
      facilityLocation,
    );
  }

  find(id: number): Observable<IFacilityLocation> {
    return this.http.get<IFacilityLocation>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFacilityLocation[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFacilityLocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityLocationIdentifier(facilityLocation: Pick<IFacilityLocation, 'id'>): number {
    return facilityLocation.id;
  }

  compareFacilityLocation(o1: Pick<IFacilityLocation, 'id'> | null, o2: Pick<IFacilityLocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityLocationIdentifier(o1) === this.getFacilityLocationIdentifier(o2) : o1 === o2;
  }

  addFacilityLocationToCollectionIfMissing<Type extends Pick<IFacilityLocation, 'id'>>(
    facilityLocationCollection: Type[],
    ...facilityLocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityLocations: Type[] = facilityLocationsToCheck.filter(
      facilityLocationItem => facilityLocationItem !== null && facilityLocationItem !== undefined,
    );
    if (facilityLocations.length > 0) {
      const facilityLocationCollectionIdentifiers = facilityLocationCollection.map(facilityLocationItem =>
        this.getFacilityLocationIdentifier(facilityLocationItem),
      );
      const facilityLocationsToAdd = facilityLocations.filter(facilityLocationItem => {
        const facilityLocationIdentifier = this.getFacilityLocationIdentifier(facilityLocationItem);
        if (facilityLocationCollectionIdentifiers.includes(facilityLocationIdentifier)) {
          return false;
        }
        facilityLocationCollectionIdentifiers.push(facilityLocationIdentifier);
        return true;
      });
      return [...facilityLocationsToAdd, ...facilityLocationCollection];
    }
    return facilityLocationCollection;
  }
}
