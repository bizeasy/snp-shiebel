import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityType, NewFacilityType } from '../facility-type.model';

export type PartialUpdateFacilityType = Partial<IFacilityType> & Pick<IFacilityType, 'id'>;

@Service()
export class FacilityTypesService {
  readonly facilityTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facilityTypesResource = httpResource<IFacilityType[]>(() => {
    const params = this.facilityTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityType that have been fetched. It is updated when the facilityTypesResource emits a new value.
   * In case of error while fetching the facilityTypes, the signal is set to an empty array.
   */
  readonly facilityTypes = computed(() => (this.facilityTypesResource.hasValue() ? this.facilityTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/facility-types`;
}

@Service()
export class FacilityTypeService extends FacilityTypesService {
  protected readonly http = inject(HttpClient);

  create(facilityType: NewFacilityType): Observable<IFacilityType> {
    return this.http.post<IFacilityType>(this.resourceUrl, facilityType);
  }

  update(facilityType: IFacilityType): Observable<IFacilityType> {
    return this.http.put<IFacilityType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityTypeIdentifier(facilityType))}`,
      facilityType,
    );
  }

  partialUpdate(facilityType: PartialUpdateFacilityType): Observable<IFacilityType> {
    return this.http.patch<IFacilityType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityTypeIdentifier(facilityType))}`,
      facilityType,
    );
  }

  find(id: number): Observable<IFacilityType> {
    return this.http.get<IFacilityType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFacilityType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFacilityType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityTypeIdentifier(facilityType: Pick<IFacilityType, 'id'>): number {
    return facilityType.id;
  }

  compareFacilityType(o1: Pick<IFacilityType, 'id'> | null, o2: Pick<IFacilityType, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityTypeIdentifier(o1) === this.getFacilityTypeIdentifier(o2) : o1 === o2;
  }

  addFacilityTypeToCollectionIfMissing<Type extends Pick<IFacilityType, 'id'>>(
    facilityTypeCollection: Type[],
    ...facilityTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityTypes: Type[] = facilityTypesToCheck.filter(
      facilityTypeItem => facilityTypeItem !== null && facilityTypeItem !== undefined,
    );
    if (facilityTypes.length > 0) {
      const facilityTypeCollectionIdentifiers = facilityTypeCollection.map(facilityTypeItem =>
        this.getFacilityTypeIdentifier(facilityTypeItem),
      );
      const facilityTypesToAdd = facilityTypes.filter(facilityTypeItem => {
        const facilityTypeIdentifier = this.getFacilityTypeIdentifier(facilityTypeItem);
        if (facilityTypeCollectionIdentifiers.includes(facilityTypeIdentifier)) {
          return false;
        }
        facilityTypeCollectionIdentifiers.push(facilityTypeIdentifier);
        return true;
      });
      return [...facilityTypesToAdd, ...facilityTypeCollection];
    }
    return facilityTypeCollection;
  }
}
