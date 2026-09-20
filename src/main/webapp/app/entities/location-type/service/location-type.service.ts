import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ILocationType, NewLocationType } from '../location-type.model';

export type PartialUpdateLocationType = Partial<ILocationType> & Pick<ILocationType, 'id'>;

@Service()
export class LocationTypesService {
  readonly locationTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly locationTypesResource = httpResource<ILocationType[]>(() => {
    const params = this.locationTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of locationType that have been fetched. It is updated when the locationTypesResource emits a new value.
   * In case of error while fetching the locationTypes, the signal is set to an empty array.
   */
  readonly locationTypes = computed(() => (this.locationTypesResource.hasValue() ? this.locationTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/location-types`;
}

@Service()
export class LocationTypeService extends LocationTypesService {
  protected readonly http = inject(HttpClient);

  create(locationType: NewLocationType): Observable<ILocationType> {
    return this.http.post<ILocationType>(this.resourceUrl, locationType);
  }

  update(locationType: ILocationType): Observable<ILocationType> {
    return this.http.put<ILocationType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getLocationTypeIdentifier(locationType))}`,
      locationType,
    );
  }

  partialUpdate(locationType: PartialUpdateLocationType): Observable<ILocationType> {
    return this.http.patch<ILocationType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getLocationTypeIdentifier(locationType))}`,
      locationType,
    );
  }

  find(id: number): Observable<ILocationType> {
    return this.http.get<ILocationType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ILocationType[]>> {
    const options = createRequestOption(req);
    return this.http.get<ILocationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getLocationTypeIdentifier(locationType: Pick<ILocationType, 'id'>): number {
    return locationType.id;
  }

  compareLocationType(o1: Pick<ILocationType, 'id'> | null, o2: Pick<ILocationType, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocationTypeIdentifier(o1) === this.getLocationTypeIdentifier(o2) : o1 === o2;
  }

  addLocationTypeToCollectionIfMissing<Type extends Pick<ILocationType, 'id'>>(
    locationTypeCollection: Type[],
    ...locationTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const locationTypes: Type[] = locationTypesToCheck.filter(
      locationTypeItem => locationTypeItem !== null && locationTypeItem !== undefined,
    );
    if (locationTypes.length > 0) {
      const locationTypeCollectionIdentifiers = locationTypeCollection.map(locationTypeItem =>
        this.getLocationTypeIdentifier(locationTypeItem),
      );
      const locationTypesToAdd = locationTypes.filter(locationTypeItem => {
        const locationTypeIdentifier = this.getLocationTypeIdentifier(locationTypeItem);
        if (locationTypeCollectionIdentifiers.includes(locationTypeIdentifier)) {
          return false;
        }
        locationTypeCollectionIdentifiers.push(locationTypeIdentifier);
        return true;
      });
      return [...locationTypesToAdd, ...locationTypeCollection];
    }
    return locationTypeCollection;
  }
}
