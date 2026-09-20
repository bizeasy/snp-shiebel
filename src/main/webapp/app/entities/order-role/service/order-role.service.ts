import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderRole, NewOrderRole } from '../order-role.model';

export type PartialUpdateOrderRole = Partial<IOrderRole> & Pick<IOrderRole, 'id'>;

@Service()
export class OrderRolesService {
  readonly orderRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderRolesResource = httpResource<IOrderRole[]>(() => {
    const params = this.orderRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderRole that have been fetched. It is updated when the orderRolesResource emits a new value.
   * In case of error while fetching the orderRoles, the signal is set to an empty array.
   */
  readonly orderRoles = computed(() => (this.orderRolesResource.hasValue() ? this.orderRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/order-roles`;
}

@Service()
export class OrderRoleService extends OrderRolesService {
  protected readonly http = inject(HttpClient);

  create(orderRole: NewOrderRole): Observable<IOrderRole> {
    return this.http.post<IOrderRole>(this.resourceUrl, orderRole);
  }

  update(orderRole: IOrderRole): Observable<IOrderRole> {
    return this.http.put<IOrderRole>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderRoleIdentifier(orderRole))}`, orderRole);
  }

  partialUpdate(orderRole: PartialUpdateOrderRole): Observable<IOrderRole> {
    return this.http.patch<IOrderRole>(`${this.resourceUrl}/${encodeURIComponent(this.getOrderRoleIdentifier(orderRole))}`, orderRole);
  }

  find(id: number): Observable<IOrderRole> {
    return this.http.get<IOrderRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderRoleIdentifier(orderRole: Pick<IOrderRole, 'id'>): number {
    return orderRole.id;
  }

  compareOrderRole(o1: Pick<IOrderRole, 'id'> | null, o2: Pick<IOrderRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderRoleIdentifier(o1) === this.getOrderRoleIdentifier(o2) : o1 === o2;
  }

  addOrderRoleToCollectionIfMissing<Type extends Pick<IOrderRole, 'id'>>(
    orderRoleCollection: Type[],
    ...orderRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderRoles: Type[] = orderRolesToCheck.filter(orderRoleItem => orderRoleItem !== null && orderRoleItem !== undefined);
    if (orderRoles.length > 0) {
      const orderRoleCollectionIdentifiers = orderRoleCollection.map(orderRoleItem => this.getOrderRoleIdentifier(orderRoleItem));
      const orderRolesToAdd = orderRoles.filter(orderRoleItem => {
        const orderRoleIdentifier = this.getOrderRoleIdentifier(orderRoleItem);
        if (orderRoleCollectionIdentifiers.includes(orderRoleIdentifier)) {
          return false;
        }
        orderRoleCollectionIdentifiers.push(orderRoleIdentifier);
        return true;
      });
      return [...orderRolesToAdd, ...orderRoleCollection];
    }
    return orderRoleCollection;
  }
}
