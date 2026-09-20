import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuoteItem, NewQuoteItem } from '../quote-item.model';

export type PartialUpdateQuoteItem = Partial<IQuoteItem> & Pick<IQuoteItem, 'id'>;

type RestOf<T extends IQuoteItem | NewQuoteItem> = Omit<T, 'estimatedDeliveryDate'> & {
  estimatedDeliveryDate?: string | null;
};

export type RestQuoteItem = RestOf<IQuoteItem>;

export type NewRestQuoteItem = RestOf<NewQuoteItem>;

export type PartialUpdateRestQuoteItem = RestOf<PartialUpdateQuoteItem>;

@Service()
export class QuoteItemsService {
  readonly quoteItemsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quoteItemsResource = httpResource<RestQuoteItem[]>(() => {
    const params = this.quoteItemsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quoteItem that have been fetched. It is updated when the quoteItemsResource emits a new value.
   * In case of error while fetching the quoteItems, the signal is set to an empty array.
   */
  readonly quoteItems = computed(() =>
    (this.quoteItemsResource.hasValue() ? this.quoteItemsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/quote-items`;

  protected convertValueFromServer(restQuoteItem: RestQuoteItem): IQuoteItem {
    return {
      ...restQuoteItem,
      estimatedDeliveryDate: restQuoteItem.estimatedDeliveryDate ? dayjs(restQuoteItem.estimatedDeliveryDate) : undefined,
    };
  }
}

@Service()
export class QuoteItemService extends QuoteItemsService {
  protected readonly http = inject(HttpClient);

  create(quoteItem: NewQuoteItem): Observable<IQuoteItem> {
    const copy = this.convertValueFromClient(quoteItem);
    return this.http.post<RestQuoteItem>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quoteItem: IQuoteItem): Observable<IQuoteItem> {
    const copy = this.convertValueFromClient(quoteItem);
    return this.http
      .put<RestQuoteItem>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteItemIdentifier(quoteItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quoteItem: PartialUpdateQuoteItem): Observable<IQuoteItem> {
    const copy = this.convertValueFromClient(quoteItem);
    return this.http
      .patch<RestQuoteItem>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteItemIdentifier(quoteItem))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IQuoteItem> {
    return this.http
      .get<RestQuoteItem>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IQuoteItem[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuoteItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuoteItemIdentifier(quoteItem: Pick<IQuoteItem, 'id'>): number {
    return quoteItem.id;
  }

  compareQuoteItem(o1: Pick<IQuoteItem, 'id'> | null, o2: Pick<IQuoteItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteItemIdentifier(o1) === this.getQuoteItemIdentifier(o2) : o1 === o2;
  }

  addQuoteItemToCollectionIfMissing<Type extends Pick<IQuoteItem, 'id'>>(
    quoteItemCollection: Type[],
    ...quoteItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quoteItems: Type[] = quoteItemsToCheck.filter(quoteItemItem => quoteItemItem !== null && quoteItemItem !== undefined);
    if (quoteItems.length > 0) {
      const quoteItemCollectionIdentifiers = quoteItemCollection.map(quoteItemItem => this.getQuoteItemIdentifier(quoteItemItem));
      const quoteItemsToAdd = quoteItems.filter(quoteItemItem => {
        const quoteItemIdentifier = this.getQuoteItemIdentifier(quoteItemItem);
        if (quoteItemCollectionIdentifiers.includes(quoteItemIdentifier)) {
          return false;
        }
        quoteItemCollectionIdentifiers.push(quoteItemIdentifier);
        return true;
      });
      return [...quoteItemsToAdd, ...quoteItemCollection];
    }
    return quoteItemCollection;
  }

  protected convertValueFromClient<T extends IQuoteItem | NewQuoteItem | PartialUpdateQuoteItem>(quoteItem: T): RestOf<T> {
    return {
      ...quoteItem,
      estimatedDeliveryDate: quoteItem.estimatedDeliveryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestQuoteItem): IQuoteItem {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestQuoteItem[]): IQuoteItem[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
