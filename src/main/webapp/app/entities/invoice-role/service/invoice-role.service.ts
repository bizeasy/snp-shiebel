import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInvoiceRole, NewInvoiceRole } from '../invoice-role.model';

export type PartialUpdateInvoiceRole = Partial<IInvoiceRole> & Pick<IInvoiceRole, 'id'>;

@Service()
export class InvoiceRolesService {
  readonly invoiceRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly invoiceRolesResource = httpResource<IInvoiceRole[]>(() => {
    const params = this.invoiceRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of invoiceRole that have been fetched. It is updated when the invoiceRolesResource emits a new value.
   * In case of error while fetching the invoiceRoles, the signal is set to an empty array.
   */
  readonly invoiceRoles = computed(() => (this.invoiceRolesResource.hasValue() ? this.invoiceRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/invoice-roles`;
}

@Service()
export class InvoiceRoleService extends InvoiceRolesService {
  protected readonly http = inject(HttpClient);

  create(invoiceRole: NewInvoiceRole): Observable<IInvoiceRole> {
    return this.http.post<IInvoiceRole>(this.resourceUrl, invoiceRole);
  }

  update(invoiceRole: IInvoiceRole): Observable<IInvoiceRole> {
    return this.http.put<IInvoiceRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceRoleIdentifier(invoiceRole))}`,
      invoiceRole,
    );
  }

  partialUpdate(invoiceRole: PartialUpdateInvoiceRole): Observable<IInvoiceRole> {
    return this.http.patch<IInvoiceRole>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceRoleIdentifier(invoiceRole))}`,
      invoiceRole,
    );
  }

  find(id: number): Observable<IInvoiceRole> {
    return this.http.get<IInvoiceRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInvoiceRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInvoiceRoleIdentifier(invoiceRole: Pick<IInvoiceRole, 'id'>): number {
    return invoiceRole.id;
  }

  compareInvoiceRole(o1: Pick<IInvoiceRole, 'id'> | null, o2: Pick<IInvoiceRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceRoleIdentifier(o1) === this.getInvoiceRoleIdentifier(o2) : o1 === o2;
  }

  addInvoiceRoleToCollectionIfMissing<Type extends Pick<IInvoiceRole, 'id'>>(
    invoiceRoleCollection: Type[],
    ...invoiceRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceRoles: Type[] = invoiceRolesToCheck.filter(invoiceRoleItem => invoiceRoleItem !== null && invoiceRoleItem !== undefined);
    if (invoiceRoles.length > 0) {
      const invoiceRoleCollectionIdentifiers = invoiceRoleCollection.map(invoiceRoleItem => this.getInvoiceRoleIdentifier(invoiceRoleItem));
      const invoiceRolesToAdd = invoiceRoles.filter(invoiceRoleItem => {
        const invoiceRoleIdentifier = this.getInvoiceRoleIdentifier(invoiceRoleItem);
        if (invoiceRoleCollectionIdentifiers.includes(invoiceRoleIdentifier)) {
          return false;
        }
        invoiceRoleCollectionIdentifiers.push(invoiceRoleIdentifier);
        return true;
      });
      return [...invoiceRolesToAdd, ...invoiceRoleCollection];
    }
    return invoiceRoleCollection;
  }
}
