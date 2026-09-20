import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICashBook, NewCashBook } from '../cash-book.model';

export type PartialUpdateCashBook = Partial<ICashBook> & Pick<ICashBook, 'id'>;

type RestOf<T extends ICashBook | NewCashBook> = Omit<T, 'transactionDate' | 'dueDate' | 'invoiceDate'> & {
  transactionDate?: string | null;
  dueDate?: string | null;
  invoiceDate?: string | null;
};

export type RestCashBook = RestOf<ICashBook>;

export type NewRestCashBook = RestOf<NewCashBook>;

export type PartialUpdateRestCashBook = RestOf<PartialUpdateCashBook>;

@Service()
export class CashBooksService {
  readonly cashBooksParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly cashBooksResource = httpResource<RestCashBook[]>(() => {
    const params = this.cashBooksParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of cashBook that have been fetched. It is updated when the cashBooksResource emits a new value.
   * In case of error while fetching the cashBooks, the signal is set to an empty array.
   */
  readonly cashBooks = computed(() =>
    (this.cashBooksResource.hasValue() ? this.cashBooksResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cash-books`;

  protected convertValueFromServer(restCashBook: RestCashBook): ICashBook {
    return {
      ...restCashBook,
      transactionDate: restCashBook.transactionDate ? dayjs(restCashBook.transactionDate) : undefined,
      dueDate: restCashBook.dueDate ? dayjs(restCashBook.dueDate) : undefined,
      invoiceDate: restCashBook.invoiceDate ? dayjs(restCashBook.invoiceDate) : undefined,
    };
  }
}

@Service()
export class CashBookService extends CashBooksService {
  protected readonly http = inject(HttpClient);

  create(cashBook: NewCashBook): Observable<ICashBook> {
    const copy = this.convertValueFromClient(cashBook);
    return this.http.post<RestCashBook>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cashBook: ICashBook): Observable<ICashBook> {
    const copy = this.convertValueFromClient(cashBook);
    return this.http
      .put<RestCashBook>(`${this.resourceUrl}/${encodeURIComponent(this.getCashBookIdentifier(cashBook))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cashBook: PartialUpdateCashBook): Observable<ICashBook> {
    const copy = this.convertValueFromClient(cashBook);
    return this.http
      .patch<RestCashBook>(`${this.resourceUrl}/${encodeURIComponent(this.getCashBookIdentifier(cashBook))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICashBook> {
    return this.http
      .get<RestCashBook>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICashBook[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCashBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCashBookIdentifier(cashBook: Pick<ICashBook, 'id'>): number {
    return cashBook.id;
  }

  compareCashBook(o1: Pick<ICashBook, 'id'> | null, o2: Pick<ICashBook, 'id'> | null): boolean {
    return o1 && o2 ? this.getCashBookIdentifier(o1) === this.getCashBookIdentifier(o2) : o1 === o2;
  }

  addCashBookToCollectionIfMissing<Type extends Pick<ICashBook, 'id'>>(
    cashBookCollection: Type[],
    ...cashBooksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cashBooks: Type[] = cashBooksToCheck.filter(cashBookItem => cashBookItem !== null && cashBookItem !== undefined);
    if (cashBooks.length > 0) {
      const cashBookCollectionIdentifiers = cashBookCollection.map(cashBookItem => this.getCashBookIdentifier(cashBookItem));
      const cashBooksToAdd = cashBooks.filter(cashBookItem => {
        const cashBookIdentifier = this.getCashBookIdentifier(cashBookItem);
        if (cashBookCollectionIdentifiers.includes(cashBookIdentifier)) {
          return false;
        }
        cashBookCollectionIdentifiers.push(cashBookIdentifier);
        return true;
      });
      return [...cashBooksToAdd, ...cashBookCollection];
    }
    return cashBookCollection;
  }

  protected convertValueFromClient<T extends ICashBook | NewCashBook | PartialUpdateCashBook>(cashBook: T): RestOf<T> {
    return {
      ...cashBook,
      transactionDate: cashBook.transactionDate?.format(DATE_FORMAT) ?? null,
      dueDate: cashBook.dueDate?.format(DATE_FORMAT) ?? null,
      invoiceDate: cashBook.invoiceDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCashBook): ICashBook {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCashBook[]): ICashBook[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
