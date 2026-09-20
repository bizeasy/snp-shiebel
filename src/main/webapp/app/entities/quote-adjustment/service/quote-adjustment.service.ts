import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuoteAdjustment, NewQuoteAdjustment } from '../quote-adjustment.model';

export type PartialUpdateQuoteAdjustment = Partial<IQuoteAdjustment> & Pick<IQuoteAdjustment, 'id'>;

type RestOf<T extends IQuoteAdjustment | NewQuoteAdjustment> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

export type RestQuoteAdjustment = RestOf<IQuoteAdjustment>;

export type NewRestQuoteAdjustment = RestOf<NewQuoteAdjustment>;

export type PartialUpdateRestQuoteAdjustment = RestOf<PartialUpdateQuoteAdjustment>;

@Service()
export class QuoteAdjustmentsService {
  readonly quoteAdjustmentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quoteAdjustmentsResource = httpResource<RestQuoteAdjustment[]>(() => {
    const params = this.quoteAdjustmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quoteAdjustment that have been fetched. It is updated when the quoteAdjustmentsResource emits a new value.
   * In case of error while fetching the quoteAdjustments, the signal is set to an empty array.
   */
  readonly quoteAdjustments = computed(() =>
    (this.quoteAdjustmentsResource.hasValue() ? this.quoteAdjustmentsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/quote-adjustments`;

  protected convertValueFromServer(restQuoteAdjustment: RestQuoteAdjustment): IQuoteAdjustment {
    return {
      ...restQuoteAdjustment,
      entryDate: restQuoteAdjustment.entryDate ? dayjs(restQuoteAdjustment.entryDate) : undefined,
    };
  }
}

@Service()
export class QuoteAdjustmentService extends QuoteAdjustmentsService {
  protected readonly http = inject(HttpClient);

  create(quoteAdjustment: NewQuoteAdjustment): Observable<IQuoteAdjustment> {
    const copy = this.convertValueFromClient(quoteAdjustment);
    return this.http.post<RestQuoteAdjustment>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quoteAdjustment: IQuoteAdjustment): Observable<IQuoteAdjustment> {
    const copy = this.convertValueFromClient(quoteAdjustment);
    return this.http
      .put<RestQuoteAdjustment>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteAdjustmentIdentifier(quoteAdjustment))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quoteAdjustment: PartialUpdateQuoteAdjustment): Observable<IQuoteAdjustment> {
    const copy = this.convertValueFromClient(quoteAdjustment);
    return this.http
      .patch<RestQuoteAdjustment>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteAdjustmentIdentifier(quoteAdjustment))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IQuoteAdjustment> {
    return this.http
      .get<RestQuoteAdjustment>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IQuoteAdjustment[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuoteAdjustment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuoteAdjustmentIdentifier(quoteAdjustment: Pick<IQuoteAdjustment, 'id'>): number {
    return quoteAdjustment.id;
  }

  compareQuoteAdjustment(o1: Pick<IQuoteAdjustment, 'id'> | null, o2: Pick<IQuoteAdjustment, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteAdjustmentIdentifier(o1) === this.getQuoteAdjustmentIdentifier(o2) : o1 === o2;
  }

  addQuoteAdjustmentToCollectionIfMissing<Type extends Pick<IQuoteAdjustment, 'id'>>(
    quoteAdjustmentCollection: Type[],
    ...quoteAdjustmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quoteAdjustments: Type[] = quoteAdjustmentsToCheck.filter(
      quoteAdjustmentItem => quoteAdjustmentItem !== null && quoteAdjustmentItem !== undefined,
    );
    if (quoteAdjustments.length > 0) {
      const quoteAdjustmentCollectionIdentifiers = quoteAdjustmentCollection.map(quoteAdjustmentItem =>
        this.getQuoteAdjustmentIdentifier(quoteAdjustmentItem),
      );
      const quoteAdjustmentsToAdd = quoteAdjustments.filter(quoteAdjustmentItem => {
        const quoteAdjustmentIdentifier = this.getQuoteAdjustmentIdentifier(quoteAdjustmentItem);
        if (quoteAdjustmentCollectionIdentifiers.includes(quoteAdjustmentIdentifier)) {
          return false;
        }
        quoteAdjustmentCollectionIdentifiers.push(quoteAdjustmentIdentifier);
        return true;
      });
      return [...quoteAdjustmentsToAdd, ...quoteAdjustmentCollection];
    }
    return quoteAdjustmentCollection;
  }

  protected convertValueFromClient<T extends IQuoteAdjustment | NewQuoteAdjustment | PartialUpdateQuoteAdjustment>(
    quoteAdjustment: T,
  ): RestOf<T> {
    return {
      ...quoteAdjustment,
      entryDate: quoteAdjustment.entryDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestQuoteAdjustment): IQuoteAdjustment {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestQuoteAdjustment[]): IQuoteAdjustment[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
