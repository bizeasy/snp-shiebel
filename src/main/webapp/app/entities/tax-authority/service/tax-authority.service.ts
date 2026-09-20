import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITaxAuthority, NewTaxAuthority } from '../tax-authority.model';

export type PartialUpdateTaxAuthority = Partial<ITaxAuthority> & Pick<ITaxAuthority, 'id'>;

@Service()
export class TaxAuthoritiesService {
  readonly taxAuthoritiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly taxAuthoritiesResource = httpResource<ITaxAuthority[]>(() => {
    const params = this.taxAuthoritiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of taxAuthority that have been fetched. It is updated when the taxAuthoritiesResource emits a new value.
   * In case of error while fetching the taxAuthorities, the signal is set to an empty array.
   */
  readonly taxAuthorities = computed(() => (this.taxAuthoritiesResource.hasValue() ? this.taxAuthoritiesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/tax-authorities`;
}

@Service()
export class TaxAuthorityService extends TaxAuthoritiesService {
  protected readonly http = inject(HttpClient);

  create(taxAuthority: NewTaxAuthority): Observable<ITaxAuthority> {
    return this.http.post<ITaxAuthority>(this.resourceUrl, taxAuthority);
  }

  update(taxAuthority: ITaxAuthority): Observable<ITaxAuthority> {
    return this.http.put<ITaxAuthority>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTaxAuthorityIdentifier(taxAuthority))}`,
      taxAuthority,
    );
  }

  partialUpdate(taxAuthority: PartialUpdateTaxAuthority): Observable<ITaxAuthority> {
    return this.http.patch<ITaxAuthority>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTaxAuthorityIdentifier(taxAuthority))}`,
      taxAuthority,
    );
  }

  find(id: number): Observable<ITaxAuthority> {
    return this.http.get<ITaxAuthority>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITaxAuthority[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITaxAuthority[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTaxAuthorityIdentifier(taxAuthority: Pick<ITaxAuthority, 'id'>): number {
    return taxAuthority.id;
  }

  compareTaxAuthority(o1: Pick<ITaxAuthority, 'id'> | null, o2: Pick<ITaxAuthority, 'id'> | null): boolean {
    return o1 && o2 ? this.getTaxAuthorityIdentifier(o1) === this.getTaxAuthorityIdentifier(o2) : o1 === o2;
  }

  addTaxAuthorityToCollectionIfMissing<Type extends Pick<ITaxAuthority, 'id'>>(
    taxAuthorityCollection: Type[],
    ...taxAuthoritiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const taxAuthorities: Type[] = taxAuthoritiesToCheck.filter(
      taxAuthorityItem => taxAuthorityItem !== null && taxAuthorityItem !== undefined,
    );
    if (taxAuthorities.length > 0) {
      const taxAuthorityCollectionIdentifiers = taxAuthorityCollection.map(taxAuthorityItem =>
        this.getTaxAuthorityIdentifier(taxAuthorityItem),
      );
      const taxAuthoritiesToAdd = taxAuthorities.filter(taxAuthorityItem => {
        const taxAuthorityIdentifier = this.getTaxAuthorityIdentifier(taxAuthorityItem);
        if (taxAuthorityCollectionIdentifiers.includes(taxAuthorityIdentifier)) {
          return false;
        }
        taxAuthorityCollectionIdentifiers.push(taxAuthorityIdentifier);
        return true;
      });
      return [...taxAuthoritiesToAdd, ...taxAuthorityCollection];
    }
    return taxAuthorityCollection;
  }
}
