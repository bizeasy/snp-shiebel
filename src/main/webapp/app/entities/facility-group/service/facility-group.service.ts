import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacilityGroup, NewFacilityGroup } from '../facility-group.model';

export type PartialUpdateFacilityGroup = Partial<IFacilityGroup> & Pick<IFacilityGroup, 'id'>;

@Service()
export class FacilityGroupsService {
  readonly facilityGroupsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facilityGroupsResource = httpResource<IFacilityGroup[]>(() => {
    const params = this.facilityGroupsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facilityGroup that have been fetched. It is updated when the facilityGroupsResource emits a new value.
   * In case of error while fetching the facilityGroups, the signal is set to an empty array.
   */
  readonly facilityGroups = computed(() => (this.facilityGroupsResource.hasValue() ? this.facilityGroupsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/facility-groups`;
}

@Service()
export class FacilityGroupService extends FacilityGroupsService {
  protected readonly http = inject(HttpClient);

  create(facilityGroup: NewFacilityGroup): Observable<IFacilityGroup> {
    return this.http.post<IFacilityGroup>(this.resourceUrl, facilityGroup);
  }

  update(facilityGroup: IFacilityGroup): Observable<IFacilityGroup> {
    return this.http.put<IFacilityGroup>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityGroupIdentifier(facilityGroup))}`,
      facilityGroup,
    );
  }

  partialUpdate(facilityGroup: PartialUpdateFacilityGroup): Observable<IFacilityGroup> {
    return this.http.patch<IFacilityGroup>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFacilityGroupIdentifier(facilityGroup))}`,
      facilityGroup,
    );
  }

  find(id: number): Observable<IFacilityGroup> {
    return this.http.get<IFacilityGroup>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFacilityGroup[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFacilityGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacilityGroupIdentifier(facilityGroup: Pick<IFacilityGroup, 'id'>): number {
    return facilityGroup.id;
  }

  compareFacilityGroup(o1: Pick<IFacilityGroup, 'id'> | null, o2: Pick<IFacilityGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacilityGroupIdentifier(o1) === this.getFacilityGroupIdentifier(o2) : o1 === o2;
  }

  addFacilityGroupToCollectionIfMissing<Type extends Pick<IFacilityGroup, 'id'>>(
    facilityGroupCollection: Type[],
    ...facilityGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facilityGroups: Type[] = facilityGroupsToCheck.filter(
      facilityGroupItem => facilityGroupItem !== null && facilityGroupItem !== undefined,
    );
    if (facilityGroups.length > 0) {
      const facilityGroupCollectionIdentifiers = facilityGroupCollection.map(facilityGroupItem =>
        this.getFacilityGroupIdentifier(facilityGroupItem),
      );
      const facilityGroupsToAdd = facilityGroups.filter(facilityGroupItem => {
        const facilityGroupIdentifier = this.getFacilityGroupIdentifier(facilityGroupItem);
        if (facilityGroupCollectionIdentifiers.includes(facilityGroupIdentifier)) {
          return false;
        }
        facilityGroupCollectionIdentifiers.push(facilityGroupIdentifier);
        return true;
      });
      return [...facilityGroupsToAdd, ...facilityGroupCollection];
    }
    return facilityGroupCollection;
  }
}
