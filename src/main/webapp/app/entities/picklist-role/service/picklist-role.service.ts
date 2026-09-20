import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPicklistRole, NewPicklistRole } from '../picklist-role.model';

export type PartialUpdatePicklistRole = Partial<IPicklistRole> & Pick<IPicklistRole, 'id'>;

@Service()
export class PicklistRolesService {
  readonly picklistRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly picklistRolesResource = httpResource<IPicklistRole[]>(() => {
    const params = this.picklistRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of picklistRole that have been fetched. It is updated when the picklistRolesResource emits a new value.
   * In case of error while fetching the picklistRoles, the signal is set to an empty array.
   */
  readonly picklistRoles = computed(() => (this.picklistRolesResource.hasValue() ? this.picklistRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/picklist-roles`;
}

@Service()
export class PicklistRoleService extends PicklistRolesService {
  protected readonly http = inject(HttpClient);

  create(picklistRole: NewPicklistRole): Observable<IPicklistRole> {
    return this.http.post<IPicklistRole>(this.resourceUrl, picklistRole);
  }

  update(picklistRole: IPicklistRole): Observable<IPicklistRole> {
    return this.http.put<IPicklistRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPicklistRoleIdentifier(picklistRole))}`,
      picklistRole,
    );
  }

  partialUpdate(picklistRole: PartialUpdatePicklistRole): Observable<IPicklistRole> {
    return this.http.patch<IPicklistRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPicklistRoleIdentifier(picklistRole))}`,
      picklistRole,
    );
  }

  find(id: number): Observable<IPicklistRole> {
    return this.http.get<IPicklistRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPicklistRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPicklistRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPicklistRoleIdentifier(picklistRole: Pick<IPicklistRole, 'id'>): number {
    return picklistRole.id;
  }

  comparePicklistRole(o1: Pick<IPicklistRole, 'id'> | null, o2: Pick<IPicklistRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getPicklistRoleIdentifier(o1) === this.getPicklistRoleIdentifier(o2) : o1 === o2;
  }

  addPicklistRoleToCollectionIfMissing<Type extends Pick<IPicklistRole, 'id'>>(
    picklistRoleCollection: Type[],
    ...picklistRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const picklistRoles: Type[] = picklistRolesToCheck.filter(
      picklistRoleItem => picklistRoleItem !== null && picklistRoleItem !== undefined,
    );
    if (picklistRoles.length > 0) {
      const picklistRoleCollectionIdentifiers = picklistRoleCollection.map(picklistRoleItem =>
        this.getPicklistRoleIdentifier(picklistRoleItem),
      );
      const picklistRolesToAdd = picklistRoles.filter(picklistRoleItem => {
        const picklistRoleIdentifier = this.getPicklistRoleIdentifier(picklistRoleItem);
        if (picklistRoleCollectionIdentifiers.includes(picklistRoleIdentifier)) {
          return false;
        }
        picklistRoleCollectionIdentifiers.push(picklistRoleIdentifier);
        return true;
      });
      return [...picklistRolesToAdd, ...picklistRoleCollection];
    }
    return picklistRoleCollection;
  }
}
