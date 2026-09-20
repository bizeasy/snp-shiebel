import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuoteRole, NewQuoteRole } from '../quote-role.model';

export type PartialUpdateQuoteRole = Partial<IQuoteRole> & Pick<IQuoteRole, 'id'>;

@Service()
export class QuoteRolesService {
  readonly quoteRolesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quoteRolesResource = httpResource<IQuoteRole[]>(() => {
    const params = this.quoteRolesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quoteRole that have been fetched. It is updated when the quoteRolesResource emits a new value.
   * In case of error while fetching the quoteRoles, the signal is set to an empty array.
   */
  readonly quoteRoles = computed(() => (this.quoteRolesResource.hasValue() ? this.quoteRolesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/quote-roles`;
}

@Service()
export class QuoteRoleService extends QuoteRolesService {
  protected readonly http = inject(HttpClient);

  create(quoteRole: NewQuoteRole): Observable<IQuoteRole> {
    return this.http.post<IQuoteRole>(this.resourceUrl, quoteRole);
  }

  update(quoteRole: IQuoteRole): Observable<IQuoteRole> {
    return this.http.put<IQuoteRole>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteRoleIdentifier(quoteRole))}`, quoteRole);
  }

  partialUpdate(quoteRole: PartialUpdateQuoteRole): Observable<IQuoteRole> {
    return this.http.patch<IQuoteRole>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteRoleIdentifier(quoteRole))}`, quoteRole);
  }

  find(id: number): Observable<IQuoteRole> {
    return this.http.get<IQuoteRole>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IQuoteRole[]>> {
    const options = createRequestOption(req);
    return this.http.get<IQuoteRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuoteRoleIdentifier(quoteRole: Pick<IQuoteRole, 'id'>): number {
    return quoteRole.id;
  }

  compareQuoteRole(o1: Pick<IQuoteRole, 'id'> | null, o2: Pick<IQuoteRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteRoleIdentifier(o1) === this.getQuoteRoleIdentifier(o2) : o1 === o2;
  }

  addQuoteRoleToCollectionIfMissing<Type extends Pick<IQuoteRole, 'id'>>(
    quoteRoleCollection: Type[],
    ...quoteRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quoteRoles: Type[] = quoteRolesToCheck.filter(quoteRoleItem => quoteRoleItem !== null && quoteRoleItem !== undefined);
    if (quoteRoles.length > 0) {
      const quoteRoleCollectionIdentifiers = quoteRoleCollection.map(quoteRoleItem => this.getQuoteRoleIdentifier(quoteRoleItem));
      const quoteRolesToAdd = quoteRoles.filter(quoteRoleItem => {
        const quoteRoleIdentifier = this.getQuoteRoleIdentifier(quoteRoleItem);
        if (quoteRoleCollectionIdentifiers.includes(quoteRoleIdentifier)) {
          return false;
        }
        quoteRoleCollectionIdentifiers.push(quoteRoleIdentifier);
        return true;
      });
      return [...quoteRolesToAdd, ...quoteRoleCollection];
    }
    return quoteRoleCollection;
  }
}
