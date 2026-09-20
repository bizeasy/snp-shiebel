import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyClassificationGroup, NewPartyClassificationGroup } from '../party-classification-group.model';

export type PartialUpdatePartyClassificationGroup = Partial<IPartyClassificationGroup> & Pick<IPartyClassificationGroup, 'id'>;

@Service()
export class PartyClassificationGroupsService {
  readonly partyClassificationGroupsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly partyClassificationGroupsResource = httpResource<IPartyClassificationGroup[]>(() => {
    const params = this.partyClassificationGroupsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyClassificationGroup that have been fetched. It is updated when the partyClassificationGroupsResource emits a new value.
   * In case of error while fetching the partyClassificationGroups, the signal is set to an empty array.
   */
  readonly partyClassificationGroups = computed(() =>
    this.partyClassificationGroupsResource.hasValue() ? this.partyClassificationGroupsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/party-classification-groups`;
}

@Service()
export class PartyClassificationGroupService extends PartyClassificationGroupsService {
  protected readonly http = inject(HttpClient);

  create(partyClassificationGroup: NewPartyClassificationGroup): Observable<IPartyClassificationGroup> {
    return this.http.post<IPartyClassificationGroup>(this.resourceUrl, partyClassificationGroup);
  }

  update(partyClassificationGroup: IPartyClassificationGroup): Observable<IPartyClassificationGroup> {
    return this.http.put<IPartyClassificationGroup>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyClassificationGroupIdentifier(partyClassificationGroup))}`,
      partyClassificationGroup,
    );
  }

  partialUpdate(partyClassificationGroup: PartialUpdatePartyClassificationGroup): Observable<IPartyClassificationGroup> {
    return this.http.patch<IPartyClassificationGroup>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartyClassificationGroupIdentifier(partyClassificationGroup))}`,
      partyClassificationGroup,
    );
  }

  find(id: number): Observable<IPartyClassificationGroup> {
    return this.http.get<IPartyClassificationGroup>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyClassificationGroup[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyClassificationGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyClassificationGroupIdentifier(partyClassificationGroup: Pick<IPartyClassificationGroup, 'id'>): number {
    return partyClassificationGroup.id;
  }

  comparePartyClassificationGroup(
    o1: Pick<IPartyClassificationGroup, 'id'> | null,
    o2: Pick<IPartyClassificationGroup, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getPartyClassificationGroupIdentifier(o1) === this.getPartyClassificationGroupIdentifier(o2) : o1 === o2;
  }

  addPartyClassificationGroupToCollectionIfMissing<Type extends Pick<IPartyClassificationGroup, 'id'>>(
    partyClassificationGroupCollection: Type[],
    ...partyClassificationGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyClassificationGroups: Type[] = partyClassificationGroupsToCheck.filter(
      partyClassificationGroupItem => partyClassificationGroupItem !== null && partyClassificationGroupItem !== undefined,
    );
    if (partyClassificationGroups.length > 0) {
      const partyClassificationGroupCollectionIdentifiers = partyClassificationGroupCollection.map(partyClassificationGroupItem =>
        this.getPartyClassificationGroupIdentifier(partyClassificationGroupItem),
      );
      const partyClassificationGroupsToAdd = partyClassificationGroups.filter(partyClassificationGroupItem => {
        const partyClassificationGroupIdentifier = this.getPartyClassificationGroupIdentifier(partyClassificationGroupItem);
        if (partyClassificationGroupCollectionIdentifiers.includes(partyClassificationGroupIdentifier)) {
          return false;
        }
        partyClassificationGroupCollectionIdentifiers.push(partyClassificationGroupIdentifier);
        return true;
      });
      return [...partyClassificationGroupsToAdd, ...partyClassificationGroupCollection];
    }
    return partyClassificationGroupCollection;
  }
}
