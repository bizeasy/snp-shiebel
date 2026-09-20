import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInvoiceItem, NewInvoiceItem } from '../invoice-item.model';

export type PartialUpdateInvoiceItem = Partial<IInvoiceItem> & Pick<IInvoiceItem, 'id'>;

type RestOf<T extends IInvoiceItem | NewInvoiceItem> = Omit<
  T,
  'estimatedShipDate' | 'estimatedDeliveryDate' | 'shipBeforeDate' | 'shipAfterDate' | 'entryDate'
> & {
  estimatedShipDate?: string | null;
  estimatedDeliveryDate?: string | null;
  shipBeforeDate?: string | null;
  shipAfterDate?: string | null;
  entryDate?: string | null;
};

export type RestInvoiceItem = RestOf<IInvoiceItem>;

export type NewRestInvoiceItem = RestOf<NewInvoiceItem>;

export type PartialUpdateRestInvoiceItem = RestOf<PartialUpdateInvoiceItem>;

@Service()
export class InvoiceItemsService {
  readonly invoiceItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly invoiceItemsResource = httpResource<RestInvoiceItem[]>(() => {
    const params = this.invoiceItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of invoiceItem that have been fetched. It is updated when the invoiceItemsResource emits a new value.
   * In case of error while fetching the invoiceItems, the signal is set to an empty array.
   */
  readonly invoiceItems = computed(() =>
    (this.invoiceItemsResource.hasValue() ? this.invoiceItemsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/invoice-items`;

  protected convertValueFromServer(restInvoiceItem: RestInvoiceItem): IInvoiceItem {
    return {
      ...restInvoiceItem,
      estimatedShipDate: restInvoiceItem.estimatedShipDate ? dayjs(restInvoiceItem.estimatedShipDate) : undefined,
      estimatedDeliveryDate: restInvoiceItem.estimatedDeliveryDate ? dayjs(restInvoiceItem.estimatedDeliveryDate) : undefined,
      shipBeforeDate: restInvoiceItem.shipBeforeDate ? dayjs(restInvoiceItem.shipBeforeDate) : undefined,
      shipAfterDate: restInvoiceItem.shipAfterDate ? dayjs(restInvoiceItem.shipAfterDate) : undefined,
      entryDate: restInvoiceItem.entryDate ? dayjs(restInvoiceItem.entryDate) : undefined,
    };
  }
}

@Service()
export class InvoiceItemService extends InvoiceItemsService {
  protected readonly http = inject(HttpClient);

  create(invoiceItem: NewInvoiceItem): Observable<IInvoiceItem> {
    const copy = this.convertValueFromClient(invoiceItem);
    return this.http.post<RestInvoiceItem>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(invoiceItem: IInvoiceItem): Observable<IInvoiceItem> {
    const copy = this.convertValueFromClient(invoiceItem);
    return this.http
      .put<RestInvoiceItem>(`${this.resourceUrl}/${encodeURIComponent(this.getInvoiceItemIdentifier(invoiceItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(invoiceItem: PartialUpdateInvoiceItem): Observable<IInvoiceItem> {
    const copy = this.convertValueFromClient(invoiceItem);
    return this.http
      .patch<RestInvoiceItem>(`${this.resourceUrl}/${encodeURIComponent(this.getInvoiceItemIdentifier(invoiceItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IInvoiceItem> {
    return this.http
      .get<RestInvoiceItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IInvoiceItem[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInvoiceItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInvoiceItemIdentifier(invoiceItem: Pick<IInvoiceItem, 'id'>): number {
    return invoiceItem.id;
  }

  compareInvoiceItem(o1: Pick<IInvoiceItem, 'id'> | null, o2: Pick<IInvoiceItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceItemIdentifier(o1) === this.getInvoiceItemIdentifier(o2) : o1 === o2;
  }

  addInvoiceItemToCollectionIfMissing<Type extends Pick<IInvoiceItem, 'id'>>(
    invoiceItemCollection: Type[],
    ...invoiceItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceItems: Type[] = invoiceItemsToCheck.filter(invoiceItemItem => invoiceItemItem !== null && invoiceItemItem !== undefined);
    if (invoiceItems.length > 0) {
      const invoiceItemCollectionIdentifiers = invoiceItemCollection.map(invoiceItemItem => this.getInvoiceItemIdentifier(invoiceItemItem));
      const invoiceItemsToAdd = invoiceItems.filter(invoiceItemItem => {
        const invoiceItemIdentifier = this.getInvoiceItemIdentifier(invoiceItemItem);
        if (invoiceItemCollectionIdentifiers.includes(invoiceItemIdentifier)) {
          return false;
        }
        invoiceItemCollectionIdentifiers.push(invoiceItemIdentifier);
        return true;
      });
      return [...invoiceItemsToAdd, ...invoiceItemCollection];
    }
    return invoiceItemCollection;
  }

  protected convertValueFromClient<T extends IInvoiceItem | NewInvoiceItem | PartialUpdateInvoiceItem>(invoiceItem: T): RestOf<T> {
    return {
      ...invoiceItem,
      estimatedShipDate: invoiceItem.estimatedShipDate?.toJSON() ?? null,
      estimatedDeliveryDate: invoiceItem.estimatedDeliveryDate?.toJSON() ?? null,
      shipBeforeDate: invoiceItem.shipBeforeDate?.toJSON() ?? null,
      shipAfterDate: invoiceItem.shipAfterDate?.toJSON() ?? null,
      entryDate: invoiceItem.entryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestInvoiceItem): IInvoiceItem {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestInvoiceItem[]): IInvoiceItem[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
