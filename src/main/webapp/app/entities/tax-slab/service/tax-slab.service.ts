import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITaxSlab, NewTaxSlab } from '../tax-slab.model';

export type PartialUpdateTaxSlab = Partial<ITaxSlab> & Pick<ITaxSlab, 'id'>;

@Service()
export class TaxSlabsService {
  readonly taxSlabsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly taxSlabsResource = httpResource<ITaxSlab[]>(() => {
    const params = this.taxSlabsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of taxSlab that have been fetched. It is updated when the taxSlabsResource emits a new value.
   * In case of error while fetching the taxSlabs, the signal is set to an empty array.
   */
  readonly taxSlabs = computed(() => (this.taxSlabsResource.hasValue() ? this.taxSlabsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/tax-slabs`;
}

@Service()
export class TaxSlabService extends TaxSlabsService {
  protected readonly http = inject(HttpClient);

  create(taxSlab: NewTaxSlab): Observable<ITaxSlab> {
    return this.http.post<ITaxSlab>(this.resourceUrl, taxSlab);
  }

  update(taxSlab: ITaxSlab): Observable<ITaxSlab> {
    return this.http.put<ITaxSlab>(`${this.resourceUrl}/${encodeURIComponent(this.getTaxSlabIdentifier(taxSlab))}`, taxSlab);
  }

  partialUpdate(taxSlab: PartialUpdateTaxSlab): Observable<ITaxSlab> {
    return this.http.patch<ITaxSlab>(`${this.resourceUrl}/${encodeURIComponent(this.getTaxSlabIdentifier(taxSlab))}`, taxSlab);
  }

  find(id: number): Observable<ITaxSlab> {
    return this.http.get<ITaxSlab>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITaxSlab[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITaxSlab[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTaxSlabIdentifier(taxSlab: Pick<ITaxSlab, 'id'>): number {
    return taxSlab.id;
  }

  compareTaxSlab(o1: Pick<ITaxSlab, 'id'> | null, o2: Pick<ITaxSlab, 'id'> | null): boolean {
    return o1 && o2 ? this.getTaxSlabIdentifier(o1) === this.getTaxSlabIdentifier(o2) : o1 === o2;
  }

  addTaxSlabToCollectionIfMissing<Type extends Pick<ITaxSlab, 'id'>>(
    taxSlabCollection: Type[],
    ...taxSlabsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const taxSlabs: Type[] = taxSlabsToCheck.filter(taxSlabItem => taxSlabItem !== null && taxSlabItem !== undefined);
    if (taxSlabs.length > 0) {
      const taxSlabCollectionIdentifiers = taxSlabCollection.map(taxSlabItem => this.getTaxSlabIdentifier(taxSlabItem));
      const taxSlabsToAdd = taxSlabs.filter(taxSlabItem => {
        const taxSlabIdentifier = this.getTaxSlabIdentifier(taxSlabItem);
        if (taxSlabCollectionIdentifiers.includes(taxSlabIdentifier)) {
          return false;
        }
        taxSlabCollectionIdentifiers.push(taxSlabIdentifier);
        return true;
      });
      return [...taxSlabsToAdd, ...taxSlabCollection];
    }
    return taxSlabCollection;
  }
}
