import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IRoleType, NewRoleType } from '../role-type.model';

export type PartialUpdateRoleType = Partial<IRoleType> & Pick<IRoleType, 'id'>;

@Service()
export class RoleTypesService {
  readonly roleTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly roleTypesResource = httpResource<IRoleType[]>(() => {
    const params = this.roleTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of roleType that have been fetched. It is updated when the roleTypesResource emits a new value.
   * In case of error while fetching the roleTypes, the signal is set to an empty array.
   */
  readonly roleTypes = computed(() => (this.roleTypesResource.hasValue() ? this.roleTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/role-types`;
}

@Service()
export class RoleTypeService extends RoleTypesService {
  protected readonly http = inject(HttpClient);

  create(roleType: NewRoleType): Observable<IRoleType> {
    return this.http.post<IRoleType>(this.resourceUrl, roleType);
  }

  update(roleType: IRoleType): Observable<IRoleType> {
    return this.http.put<IRoleType>(`${this.resourceUrl}/${encodeURIComponent(this.getRoleTypeIdentifier(roleType))}`, roleType);
  }

  partialUpdate(roleType: PartialUpdateRoleType): Observable<IRoleType> {
    return this.http.patch<IRoleType>(`${this.resourceUrl}/${encodeURIComponent(this.getRoleTypeIdentifier(roleType))}`, roleType);
  }

  find(id: number): Observable<IRoleType> {
    return this.http.get<IRoleType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IRoleType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IRoleType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRoleTypeIdentifier(roleType: Pick<IRoleType, 'id'>): number {
    return roleType.id;
  }

  compareRoleType(o1: Pick<IRoleType, 'id'> | null, o2: Pick<IRoleType, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoleTypeIdentifier(o1) === this.getRoleTypeIdentifier(o2) : o1 === o2;
  }

  addRoleTypeToCollectionIfMissing<Type extends Pick<IRoleType, 'id'>>(
    roleTypeCollection: Type[],
    ...roleTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roleTypes: Type[] = roleTypesToCheck.filter(roleTypeItem => roleTypeItem !== null && roleTypeItem !== undefined);
    if (roleTypes.length > 0) {
      const roleTypeCollectionIdentifiers = roleTypeCollection.map(roleTypeItem => this.getRoleTypeIdentifier(roleTypeItem));
      const roleTypesToAdd = roleTypes.filter(roleTypeItem => {
        const roleTypeIdentifier = this.getRoleTypeIdentifier(roleTypeItem);
        if (roleTypeCollectionIdentifiers.includes(roleTypeIdentifier)) {
          return false;
        }
        roleTypeCollectionIdentifiers.push(roleTypeIdentifier);
        return true;
      });
      return [...roleTypesToAdd, ...roleTypeCollection];
    }
    return roleTypeCollection;
  }
}
