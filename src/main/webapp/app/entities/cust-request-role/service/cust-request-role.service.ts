import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequestRole, NewCustRequestRole } from '../cust-request-role.model';

export type PartialUpdateCustRequestRole = Partial<ICustRequestRole> & Pick<ICustRequestRole, 'id'>;

@Service()
export class CustRequestRolesService {
  readonly custRequestRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly custRequestRolesResource = httpResource<ICustRequestRole[]>(() => {
    const params = this.custRequestRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequestRole that have been fetched. It is updated when the custRequestRolesResource emits a new value.
   * In case of error while fetching the custRequestRoles, the signal is set to an empty array.
   */
  readonly custRequestRoles = computed(() => (this.custRequestRolesResource.hasValue() ? this.custRequestRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/cust-request-roles`;
}

@Service()
export class CustRequestRoleService extends CustRequestRolesService {
  protected readonly http = inject(HttpClient);

  create(custRequestRole: NewCustRequestRole): Observable<ICustRequestRole> {
    return this.http.post<ICustRequestRole>(this.resourceUrl, custRequestRole);
  }

  update(custRequestRole: ICustRequestRole): Observable<ICustRequestRole> {
    return this.http.put<ICustRequestRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestRoleIdentifier(custRequestRole))}`,
      custRequestRole,
    );
  }

  partialUpdate(custRequestRole: PartialUpdateCustRequestRole): Observable<ICustRequestRole> {
    return this.http.patch<ICustRequestRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestRoleIdentifier(custRequestRole))}`,
      custRequestRole,
    );
  }

  find(id: number): Observable<ICustRequestRole> {
    return this.http.get<ICustRequestRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustRequestRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustRequestRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestRoleIdentifier(custRequestRole: Pick<ICustRequestRole, 'id'>): number {
    return custRequestRole.id;
  }

  compareCustRequestRole(o1: Pick<ICustRequestRole, 'id'> | null, o2: Pick<ICustRequestRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustRequestRoleIdentifier(o1) === this.getCustRequestRoleIdentifier(o2) : o1 === o2;
  }

  addCustRequestRoleToCollectionIfMissing<Type extends Pick<ICustRequestRole, 'id'>>(
    custRequestRoleCollection: Type[],
    ...custRequestRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequestRoles: Type[] = custRequestRolesToCheck.filter(
      custRequestRoleItem => custRequestRoleItem !== null && custRequestRoleItem !== undefined,
    );
    if (custRequestRoles.length > 0) {
      const custRequestRoleCollectionIdentifiers = custRequestRoleCollection.map(custRequestRoleItem =>
        this.getCustRequestRoleIdentifier(custRequestRoleItem),
      );
      const custRequestRolesToAdd = custRequestRoles.filter(custRequestRoleItem => {
        const custRequestRoleIdentifier = this.getCustRequestRoleIdentifier(custRequestRoleItem);
        if (custRequestRoleCollectionIdentifiers.includes(custRequestRoleIdentifier)) {
          return false;
        }
        custRequestRoleCollectionIdentifiers.push(custRequestRoleIdentifier);
        return true;
      });
      return [...custRequestRolesToAdd, ...custRequestRoleCollection];
    }
    return custRequestRoleCollection;
  }
}
