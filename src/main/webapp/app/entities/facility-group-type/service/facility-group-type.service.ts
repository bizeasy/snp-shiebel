import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityGroupType, NewFacilityGroupType } from '../facility-group-type.model';

export type PartialUpdateFacilityGroupType = Partial<IFacilityGroupType> & Pick<IFacilityGroupType, 'id'>;

@Service()
export class FacilityGroupTypesService {
  readonly facilityGroupTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly facilityGroupTypesResource = httpResource<IFacilityGroupType[]>(() => {
    const params = this.facilityGroupTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityGroupType that have been fetched. It is updated when the facilityGroupTypesResource emits a new value.
   * In case of error while fetching the facilityGroupTypes, the signal is set to an empty array.
   */
  readonly facilityGroupTypes = computed(() => (this.facilityGroupTypesResource.hasValue() ? this.facilityGroupTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/facility-group-types`;
}

@Service()
export class FacilityGroupTypeService extends FacilityGroupTypesService {
  protected readonly http = inject(HttpClient);

  create(facilityGroupType: NewFacilityGroupType): Observable<IFacilityGroupType> {
    return this.http.post<IFacilityGroupType>(this.resourceUrl, facilityGroupType);
  }

  update(facilityGroupType: IFacilityGroupType): Observable<IFacilityGroupType> {
    return this.http.put<IFacilityGroupType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityGroupTypeIdentifier(facilityGroupType))}`,
      facilityGroupType,
    );
  }

  partialUpdate(facilityGroupType: PartialUpdateFacilityGroupType): Observable<IFacilityGroupType> {
    return this.http.patch<IFacilityGroupType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityGroupTypeIdentifier(facilityGroupType))}`,
      facilityGroupType,
    );
  }

  find(id: number): Observable<IFacilityGroupType> {
    return this.http.get<IFacilityGroupType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFacilityGroupType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFacilityGroupType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityGroupTypeIdentifier(facilityGroupType: Pick<IFacilityGroupType, 'id'>): number {
    return facilityGroupType.id;
  }

  compareFacilityGroupType(o1: Pick<IFacilityGroupType, 'id'> | null, o2: Pick<IFacilityGroupType, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityGroupTypeIdentifier(o1) === this.getFacilityGroupTypeIdentifier(o2) : o1 === o2;
  }

  addFacilityGroupTypeToCollectionIfMissing<Type extends Pick<IFacilityGroupType, 'id'>>(
    facilityGroupTypeCollection: Type[],
    ...facilityGroupTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityGroupTypes: Type[] = facilityGroupTypesToCheck.filter(
      facilityGroupTypeItem => facilityGroupTypeItem !== null && facilityGroupTypeItem !== undefined,
    );
    if (facilityGroupTypes.length > 0) {
      const facilityGroupTypeCollectionIdentifiers = facilityGroupTypeCollection.map(facilityGroupTypeItem =>
        this.getFacilityGroupTypeIdentifier(facilityGroupTypeItem),
      );
      const facilityGroupTypesToAdd = facilityGroupTypes.filter(facilityGroupTypeItem => {
        const facilityGroupTypeIdentifier = this.getFacilityGroupTypeIdentifier(facilityGroupTypeItem);
        if (facilityGroupTypeCollectionIdentifiers.includes(facilityGroupTypeIdentifier)) {
          return false;
        }
        facilityGroupTypeCollectionIdentifiers.push(facilityGroupTypeIdentifier);
        return true;
      });
      return [...facilityGroupTypesToAdd, ...facilityGroupTypeCollection];
    }
    return facilityGroupTypeCollection;
  }
}
