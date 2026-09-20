import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInvoiceItemType, NewInvoiceItemType } from '../invoice-item-type.model';

export type PartialUpdateInvoiceItemType = Partial<IInvoiceItemType> & Pick<IInvoiceItemType, 'id'>;

@Service()
export class InvoiceItemTypesService {
  readonly invoiceItemTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly invoiceItemTypesResource = httpResource<IInvoiceItemType[]>(() => {
    const params = this.invoiceItemTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of invoiceItemType that have been fetched. It is updated when the invoiceItemTypesResource emits a new value.
   * In case of error while fetching the invoiceItemTypes, the signal is set to an empty array.
   */
  readonly invoiceItemTypes = computed(() => (this.invoiceItemTypesResource.hasValue() ? this.invoiceItemTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/invoice-item-types`;
}

@Service()
export class InvoiceItemTypeService extends InvoiceItemTypesService {
  protected readonly http = inject(HttpClient);

  create(invoiceItemType: NewInvoiceItemType): Observable<IInvoiceItemType> {
    return this.http.post<IInvoiceItemType>(this.resourceUrl, invoiceItemType);
  }

  update(invoiceItemType: IInvoiceItemType): Observable<IInvoiceItemType> {
    return this.http.put<IInvoiceItemType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceItemTypeIdentifier(invoiceItemType))}`,
      invoiceItemType,
    );
  }

  partialUpdate(invoiceItemType: PartialUpdateInvoiceItemType): Observable<IInvoiceItemType> {
    return this.http.patch<IInvoiceItemType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceItemTypeIdentifier(invoiceItemType))}`,
      invoiceItemType,
    );
  }

  find(id: number): Observable<IInvoiceItemType> {
    return this.http.get<IInvoiceItemType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInvoiceItemType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceItemType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInvoiceItemTypeIdentifier(invoiceItemType: Pick<IInvoiceItemType, 'id'>): number {
    return invoiceItemType.id;
  }

  compareInvoiceItemType(o1: Pick<IInvoiceItemType, 'id'> | null, o2: Pick<IInvoiceItemType, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceItemTypeIdentifier(o1) === this.getInvoiceItemTypeIdentifier(o2) : o1 === o2;
  }

  addInvoiceItemTypeToCollectionIfMissing<Type extends Pick<IInvoiceItemType, 'id'>>(
    invoiceItemTypeCollection: Type[],
    ...invoiceItemTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceItemTypes: Type[] = invoiceItemTypesToCheck.filter(
      invoiceItemTypeItem => invoiceItemTypeItem !== null && invoiceItemTypeItem !== undefined,
    );
    if (invoiceItemTypes.length > 0) {
      const invoiceItemTypeCollectionIdentifiers = invoiceItemTypeCollection.map(invoiceItemTypeItem =>
        this.getInvoiceItemTypeIdentifier(invoiceItemTypeItem),
      );
      const invoiceItemTypesToAdd = invoiceItemTypes.filter(invoiceItemTypeItem => {
        const invoiceItemTypeIdentifier = this.getInvoiceItemTypeIdentifier(invoiceItemTypeItem);
        if (invoiceItemTypeCollectionIdentifiers.includes(invoiceItemTypeIdentifier)) {
          return false;
        }
        invoiceItemTypeCollectionIdentifiers.push(invoiceItemTypeIdentifier);
        return true;
      });
      return [...invoiceItemTypesToAdd, ...invoiceItemTypeCollection];
    }
    return invoiceItemTypeCollection;
  }
}
