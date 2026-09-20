import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITaxAuthorityRateType, NewTaxAuthorityRateType } from '../tax-authority-rate-type.model';

export type PartialUpdateTaxAuthorityRateType = Partial<ITaxAuthorityRateType> & Pick<ITaxAuthorityRateType, 'id'>;

@Service()
export class TaxAuthorityRateTypesService {
  readonly taxAuthorityRateTypesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly taxAuthorityRateTypesResource = httpResource<ITaxAuthorityRateType[]>(() => {
    const params = this.taxAuthorityRateTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of taxAuthorityRateType that have been fetched. It is updated when the taxAuthorityRateTypesResource emits a new value.
   * In case of error while fetching the taxAuthorityRateTypes, the signal is set to an empty array.
   */
  readonly taxAuthorityRateTypes = computed(() =>
    this.taxAuthorityRateTypesResource.hasValue() ? this.taxAuthorityRateTypesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/tax-authority-rate-types`;
}

@Service()
export class TaxAuthorityRateTypeService extends TaxAuthorityRateTypesService {
  protected readonly http = inject(HttpClient);

  create(taxAuthorityRateType: NewTaxAuthorityRateType): Observable<ITaxAuthorityRateType> {
    return this.http.post<ITaxAuthorityRateType>(this.resourceUrl, taxAuthorityRateType);
  }

  update(taxAuthorityRateType: ITaxAuthorityRateType): Observable<ITaxAuthorityRateType> {
    return this.http.put<ITaxAuthorityRateType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTaxAuthorityRateTypeIdentifier(taxAuthorityRateType))}`,
      taxAuthorityRateType,
    );
  }

  partialUpdate(taxAuthorityRateType: PartialUpdateTaxAuthorityRateType): Observable<ITaxAuthorityRateType> {
    return this.http.patch<ITaxAuthorityRateType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTaxAuthorityRateTypeIdentifier(taxAuthorityRateType))}`,
      taxAuthorityRateType,
    );
  }

  find(id: number): Observable<ITaxAuthorityRateType> {
    return this.http.get<ITaxAuthorityRateType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITaxAuthorityRateType[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITaxAuthorityRateType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTaxAuthorityRateTypeIdentifier(taxAuthorityRateType: Pick<ITaxAuthorityRateType, 'id'>): number {
    return taxAuthorityRateType.id;
  }

  compareTaxAuthorityRateType(o1: Pick<ITaxAuthorityRateType, 'id'> | null, o2: Pick<ITaxAuthorityRateType, 'id'> | null): boolean {
    return o1 && o2 ? this.getTaxAuthorityRateTypeIdentifier(o1) === this.getTaxAuthorityRateTypeIdentifier(o2) : o1 === o2;
  }

  addTaxAuthorityRateTypeToCollectionIfMissing<Type extends Pick<ITaxAuthorityRateType, 'id'>>(
    taxAuthorityRateTypeCollection: Type[],
    ...taxAuthorityRateTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const taxAuthorityRateTypes: Type[] = taxAuthorityRateTypesToCheck.filter(
      taxAuthorityRateTypeItem => taxAuthorityRateTypeItem !== null && taxAuthorityRateTypeItem !== undefined,
    );
    if (taxAuthorityRateTypes.length > 0) {
      const taxAuthorityRateTypeCollectionIdentifiers = taxAuthorityRateTypeCollection.map(taxAuthorityRateTypeItem =>
        this.getTaxAuthorityRateTypeIdentifier(taxAuthorityRateTypeItem),
      );
      const taxAuthorityRateTypesToAdd = taxAuthorityRateTypes.filter(taxAuthorityRateTypeItem => {
        const taxAuthorityRateTypeIdentifier = this.getTaxAuthorityRateTypeIdentifier(taxAuthorityRateTypeItem);
        if (taxAuthorityRateTypeCollectionIdentifiers.includes(taxAuthorityRateTypeIdentifier)) {
          return false;
        }
        taxAuthorityRateTypeCollectionIdentifiers.push(taxAuthorityRateTypeIdentifier);
        return true;
      });
      return [...taxAuthorityRateTypesToAdd, ...taxAuthorityRateTypeCollection];
    }
    return taxAuthorityRateTypeCollection;
  }
}
