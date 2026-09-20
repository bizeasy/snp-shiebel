import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInvoice, NewInvoice } from '../invoice.model';

export type PartialUpdateInvoice = Partial<IInvoice> & Pick<IInvoice, 'id'>;

type RestOf<T extends IInvoice | NewInvoice> = Omit<T, 'invoiceDate' | 'dueDate' | 'paidDate' | 'entryDate'> & {
  invoiceDate?: string | null;
  dueDate?: string | null;
  paidDate?: string | null;
  entryDate?: string | null;
};

export type RestInvoice = RestOf<IInvoice>;

export type NewRestInvoice = RestOf<NewInvoice>;

export type PartialUpdateRestInvoice = RestOf<PartialUpdateInvoice>;

@Service()
export class InvoicesService {
  readonly invoicesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly invoicesResource = httpResource<RestInvoice[]>(() => {
    const params = this.invoicesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of invoice that have been fetched. It is updated when the invoicesResource emits a new value.
   * In case of error while fetching the invoices, the signal is set to an empty array.
   */
  readonly invoices = computed(() =>
    (this.invoicesResource.hasValue() ? this.invoicesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/invoices`;

  protected convertValueFromServer(restInvoice: RestInvoice): IInvoice {
    return {
      ...restInvoice,
      invoiceDate: restInvoice.invoiceDate ? dayjs(restInvoice.invoiceDate) : undefined,
      dueDate: restInvoice.dueDate ? dayjs(restInvoice.dueDate) : undefined,
      paidDate: restInvoice.paidDate ? dayjs(restInvoice.paidDate) : undefined,
      entryDate: restInvoice.entryDate ? dayjs(restInvoice.entryDate) : undefined,
    };
  }
}

@Service()
export class InvoiceService extends InvoicesService {
  protected readonly http = inject(HttpClient);

  create(invoice: NewInvoice): Observable<IInvoice> {
    const copy = this.convertValueFromClient(invoice);
    return this.http.post<RestInvoice>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(invoice: IInvoice): Observable<IInvoice> {
    const copy = this.convertValueFromClient(invoice);
    return this.http
      .put<RestInvoice>(`${this.resourceUrl}/${encodeURIComponent(this.getInvoiceIdentifier(invoice))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(invoice: PartialUpdateInvoice): Observable<IInvoice> {
    const copy = this.convertValueFromClient(invoice);
    return this.http
      .patch<RestInvoice>(`${this.resourceUrl}/${encodeURIComponent(this.getInvoiceIdentifier(invoice))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IInvoice> {
    return this.http
      .get<RestInvoice>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IInvoice[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInvoiceIdentifier(invoice: Pick<IInvoice, 'id'>): number {
    return invoice.id;
  }

  compareInvoice(o1: Pick<IInvoice, 'id'> | null, o2: Pick<IInvoice, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceIdentifier(o1) === this.getInvoiceIdentifier(o2) : o1 === o2;
  }

  addInvoiceToCollectionIfMissing<Type extends Pick<IInvoice, 'id'>>(
    invoiceCollection: Type[],
    ...invoicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoices: Type[] = invoicesToCheck.filter(invoiceItem => invoiceItem !== null && invoiceItem !== undefined);
    if (invoices.length > 0) {
      const invoiceCollectionIdentifiers = invoiceCollection.map(invoiceItem => this.getInvoiceIdentifier(invoiceItem));
      const invoicesToAdd = invoices.filter(invoiceItem => {
        const invoiceIdentifier = this.getInvoiceIdentifier(invoiceItem);
        if (invoiceCollectionIdentifiers.includes(invoiceIdentifier)) {
          return false;
        }
        invoiceCollectionIdentifiers.push(invoiceIdentifier);
        return true;
      });
      return [...invoicesToAdd, ...invoiceCollection];
    }
    return invoiceCollection;
  }

  protected convertValueFromClient<T extends IInvoice | NewInvoice | PartialUpdateInvoice>(invoice: T): RestOf<T> {
    return {
      ...invoice,
      invoiceDate: invoice.invoiceDate?.toJSON() ?? null,
      dueDate: invoice.dueDate?.toJSON() ?? null,
      paidDate: invoice.paidDate?.toJSON() ?? null,
      entryDate: invoice.entryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestInvoice): IInvoice {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestInvoice[]): IInvoice[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
