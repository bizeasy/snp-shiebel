import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartyRole, NewPartyRole } from '../party-role.model';

export type PartialUpdatePartyRole = Partial<IPartyRole> & Pick<IPartyRole, 'id'>;

@Service()
export class PartyRolesService {
  readonly partyRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly partyRolesResource = httpResource<IPartyRole[]>(() => {
    const params = this.partyRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partyRole that have been fetched. It is updated when the partyRolesResource emits a new value.
   * In case of error while fetching the partyRoles, the signal is set to an empty array.
   */
  readonly partyRoles = computed(() => (this.partyRolesResource.hasValue() ? this.partyRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/party-roles`;
}

@Service()
export class PartyRoleService extends PartyRolesService {
  protected readonly http = inject(HttpClient);

  create(partyRole: NewPartyRole): Observable<IPartyRole> {
    return this.http.post<IPartyRole>(this.resourceUrl, partyRole);
  }

  update(partyRole: IPartyRole): Observable<IPartyRole> {
    return this.http.put<IPartyRole>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyRoleIdentifier(partyRole))}`, partyRole);
  }

  partialUpdate(partyRole: PartialUpdatePartyRole): Observable<IPartyRole> {
    return this.http.patch<IPartyRole>(`${this.resourceUrl}/${encodeURIComponent(this.getPartyRoleIdentifier(partyRole))}`, partyRole);
  }

  find(id: number): Observable<IPartyRole> {
    return this.http.get<IPartyRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartyRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartyRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartyRoleIdentifier(partyRole: Pick<IPartyRole, 'id'>): number {
    return partyRole.id;
  }

  comparePartyRole(o1: Pick<IPartyRole, 'id'> | null, o2: Pick<IPartyRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartyRoleIdentifier(o1) === this.getPartyRoleIdentifier(o2) : o1 === o2;
  }

  addPartyRoleToCollectionIfMissing<Type extends Pick<IPartyRole, 'id'>>(
    partyRoleCollection: Type[],
    ...partyRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partyRoles: Type[] = partyRolesToCheck.filter(partyRoleItem => partyRoleItem !== null && partyRoleItem !== undefined);
    if (partyRoles.length > 0) {
      const partyRoleCollectionIdentifiers = partyRoleCollection.map(partyRoleItem => this.getPartyRoleIdentifier(partyRoleItem));
      const partyRolesToAdd = partyRoles.filter(partyRoleItem => {
        const partyRoleIdentifier = this.getPartyRoleIdentifier(partyRoleItem);
        if (partyRoleCollectionIdentifiers.includes(partyRoleIdentifier)) {
          return false;
        }
        partyRoleCollectionIdentifiers.push(partyRoleIdentifier);
        return true;
      });
      return [...partyRolesToAdd, ...partyRoleCollection];
    }
    return partyRoleCollection;
  }
}
