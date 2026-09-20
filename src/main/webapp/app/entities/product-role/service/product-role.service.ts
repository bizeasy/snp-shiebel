import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductRole, NewProductRole } from '../product-role.model';

export type PartialUpdateProductRole = Partial<IProductRole> & Pick<IProductRole, 'id'>;

@Service()
export class ProductRolesService {
  readonly productRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly productRolesResource = httpResource<IProductRole[]>(() => {
    const params = this.productRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productRole that have been fetched. It is updated when the productRolesResource emits a new value.
   * In case of error while fetching the productRoles, the signal is set to an empty array.
   */
  readonly productRoles = computed(() => (this.productRolesResource.hasValue() ? this.productRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-roles`;
}

@Service()
export class ProductRoleService extends ProductRolesService {
  protected readonly http = inject(HttpClient);

  create(productRole: NewProductRole): Observable<IProductRole> {
    return this.http.post<IProductRole>(this.resourceUrl, productRole);
  }

  update(productRole: IProductRole): Observable<IProductRole> {
    return this.http.put<IProductRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductRoleIdentifier(productRole))}`,
      productRole,
    );
  }

  partialUpdate(productRole: PartialUpdateProductRole): Observable<IProductRole> {
    return this.http.patch<IProductRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductRoleIdentifier(productRole))}`,
      productRole,
    );
  }

  find(id: number): Observable<IProductRole> {
    return this.http.get<IProductRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductRoleIdentifier(productRole: Pick<IProductRole, 'id'>): number {
    return productRole.id;
  }

  compareProductRole(o1: Pick<IProductRole, 'id'> | null, o2: Pick<IProductRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductRoleIdentifier(o1) === this.getProductRoleIdentifier(o2) : o1 === o2;
  }

  addProductRoleToCollectionIfMissing<Type extends Pick<IProductRole, 'id'>>(
    productRoleCollection: Type[],
    ...productRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productRoles: Type[] = productRolesToCheck.filter(productRoleItem => productRoleItem !== null && productRoleItem !== undefined);
    if (productRoles.length > 0) {
      const productRoleCollectionIdentifiers = productRoleCollection.map(productRoleItem => this.getProductRoleIdentifier(productRoleItem));
      const productRolesToAdd = productRoles.filter(productRoleItem => {
        const productRoleIdentifier = this.getProductRoleIdentifier(productRoleItem);
        if (productRoleCollectionIdentifiers.includes(productRoleIdentifier)) {
          return false;
        }
        productRoleCollectionIdentifiers.push(productRoleIdentifier);
        return true;
      });
      return [...productRolesToAdd, ...productRoleCollection];
    }
    return productRoleCollection;
  }
}
