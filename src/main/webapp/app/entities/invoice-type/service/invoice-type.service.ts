import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInvoiceType, NewInvoiceType } from '../invoice-type.model';

export type PartialUpdateInvoiceType = Partial<IInvoiceType> & Pick<IInvoiceType, 'id'>;

@Service()
export class InvoiceTypesService {
  readonly invoiceTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly invoiceTypesResource = httpResource<IInvoiceType[]>(() => {
    const params = this.invoiceTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of invoiceType that have been fetched. It is updated when the invoiceTypesResource emits a new value.
   * In case of error while fetching the invoiceTypes, the signal is set to an empty array.
   */
  readonly invoiceTypes = computed(() => (this.invoiceTypesResource.hasValue() ? this.invoiceTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/invoice-types`;
}

@Service()
export class InvoiceTypeService extends InvoiceTypesService {
  protected readonly http = inject(HttpClient);

  create(invoiceType: NewInvoiceType): Observable<IInvoiceType> {
    return this.http.post<IInvoiceType>(this.resourceUrl, invoiceType);
  }

  update(invoiceType: IInvoiceType): Observable<IInvoiceType> {
    return this.http.put<IInvoiceType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceTypeIdentifier(invoiceType))}`,
      invoiceType,
    );
  }

  partialUpdate(invoiceType: PartialUpdateInvoiceType): Observable<IInvoiceType> {
    return this.http.patch<IInvoiceType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInvoiceTypeIdentifier(invoiceType))}`,
      invoiceType,
    );
  }

  find(id: number): Observable<IInvoiceType> {
    return this.http.get<IInvoiceType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInvoiceType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInvoiceTypeIdentifier(invoiceType: Pick<IInvoiceType, 'id'>): number {
    return invoiceType.id;
  }

  compareInvoiceType(o1: Pick<IInvoiceType, 'id'> | null, o2: Pick<IInvoiceType, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceTypeIdentifier(o1) === this.getInvoiceTypeIdentifier(o2) : o1 === o2;
  }

  addInvoiceTypeToCollectionIfMissing<Type extends Pick<IInvoiceType, 'id'>>(
    invoiceTypeCollection: Type[],
    ...invoiceTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceTypes: Type[] = invoiceTypesToCheck.filter(invoiceTypeItem => invoiceTypeItem !== null && invoiceTypeItem !== undefined);
    if (invoiceTypes.length > 0) {
      const invoiceTypeCollectionIdentifiers = invoiceTypeCollection.map(invoiceTypeItem => this.getInvoiceTypeIdentifier(invoiceTypeItem));
      const invoiceTypesToAdd = invoiceTypes.filter(invoiceTypeItem => {
        const invoiceTypeIdentifier = this.getInvoiceTypeIdentifier(invoiceTypeItem);
        if (invoiceTypeCollectionIdentifiers.includes(invoiceTypeIdentifier)) {
          return false;
        }
        invoiceTypeCollectionIdentifiers.push(invoiceTypeIdentifier);
        return true;
      });
      return [...invoiceTypesToAdd, ...invoiceTypeCollection];
    }
    return invoiceTypeCollection;
  }
}
