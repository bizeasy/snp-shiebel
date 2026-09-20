import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInvoiceItemTypeMap, NewInvoiceItemTypeMap } from '../invoice-item-type-map.model';

export type PartialUpdateInvoiceItemTypeMap = Partial<IInvoiceItemTypeMap> & Pick<IInvoiceItemTypeMap, 'id'>;

@Service()
export class InvoiceItemTypeMapsService {
  readonly invoiceItemTypeMapsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly invoiceItemTypeMapsResource = httpResource<IInvoiceItemTypeMap[]>(() => {
    const params = this.invoiceItemTypeMapsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of invoiceItemTypeMap that have been fetched. It is updated when the invoiceItemTypeMapsResource emits a new value.
   * In case of error while fetching the invoiceItemTypeMaps, the signal is set to an empty array.
   */
  readonly invoiceItemTypeMaps = computed(() =>
    this.invoiceItemTypeMapsResource.hasValue() ? this.invoiceItemTypeMapsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/invoice-item-type-maps`;
}

@Service()
export class InvoiceItemTypeMapService extends InvoiceItemTypeMapsService {
  protected readonly http = inject(HttpClient);

  create(invoiceItemTypeMap: NewInvoiceItemTypeMap): Observable<IInvoiceItemTypeMap> {
    return this.http.post<IInvoiceItemTypeMap>(this.resourceUrl, invoiceItemTypeMap);
  }

  update(invoiceItemTypeMap: IInvoiceItemTypeMap): Observable<IInvoiceItemTypeMap> {
    return this.http.put<IInvoiceItemTypeMap>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceItemTypeMapIdentifier(invoiceItemTypeMap))}`,
      invoiceItemTypeMap,
    );
  }

  partialUpdate(invoiceItemTypeMap: PartialUpdateInvoiceItemTypeMap): Observable<IInvoiceItemTypeMap> {
    return this.http.patch<IInvoiceItemTypeMap>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceItemTypeMapIdentifier(invoiceItemTypeMap))}`,
      invoiceItemTypeMap,
    );
  }

  find(id: number): Observable<IInvoiceItemTypeMap> {
    return this.http.get<IInvoiceItemTypeMap>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInvoiceItemTypeMap[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceItemTypeMap[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInvoiceItemTypeMapIdentifier(invoiceItemTypeMap: Pick<IInvoiceItemTypeMap, 'id'>): number {
    return invoiceItemTypeMap.id;
  }

  compareInvoiceItemTypeMap(o1: Pick<IInvoiceItemTypeMap, 'id'> | null, o2: Pick<IInvoiceItemTypeMap, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceItemTypeMapIdentifier(o1) === this.getInvoiceItemTypeMapIdentifier(o2) : o1 === o2;
  }

  addInvoiceItemTypeMapToCollectionIfMissing<Type extends Pick<IInvoiceItemTypeMap, 'id'>>(
    invoiceItemTypeMapCollection: Type[],
    ...invoiceItemTypeMapsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceItemTypeMaps: Type[] = invoiceItemTypeMapsToCheck.filter(
      invoiceItemTypeMapItem => invoiceItemTypeMapItem !== null && invoiceItemTypeMapItem !== undefined,
    );
    if (invoiceItemTypeMaps.length > 0) {
      const invoiceItemTypeMapCollectionIdentifiers = invoiceItemTypeMapCollection.map(invoiceItemTypeMapItem =>
        this.getInvoiceItemTypeMapIdentifier(invoiceItemTypeMapItem),
      );
      const invoiceItemTypeMapsToAdd = invoiceItemTypeMaps.filter(invoiceItemTypeMapItem => {
        const invoiceItemTypeMapIdentifier = this.getInvoiceItemTypeMapIdentifier(invoiceItemTypeMapItem);
        if (invoiceItemTypeMapCollectionIdentifiers.includes(invoiceItemTypeMapIdentifier)) {
          return false;
        }
        invoiceItemTypeMapCollectionIdentifiers.push(invoiceItemTypeMapIdentifier);
        return true;
      });
      return [...invoiceItemTypeMapsToAdd, ...invoiceItemTypeMapCollection];
    }
    return invoiceItemTypeMapCollection;
  }
}
