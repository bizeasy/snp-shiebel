import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuoteType, NewQuoteType } from '../quote-type.model';

export type PartialUpdateQuoteType = Partial<IQuoteType> & Pick<IQuoteType, 'id'>;

@Service()
export class QuoteTypesService {
  readonly quoteTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quoteTypesResource = httpResource<IQuoteType[]>(() => {
    const params = this.quoteTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quoteType that have been fetched. It is updated when the quoteTypesResource emits a new value.
   * In case of error while fetching the quoteTypes, the signal is set to an empty array.
   */
  readonly quoteTypes = computed(() => (this.quoteTypesResource.hasValue() ? this.quoteTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/quote-types`;
}

@Service()
export class QuoteTypeService extends QuoteTypesService {
  protected readonly http = inject(HttpClient);

  create(quoteType: NewQuoteType): Observable<IQuoteType> {
    return this.http.post<IQuoteType>(this.resourceUrl, quoteType);
  }

  update(quoteType: IQuoteType): Observable<IQuoteType> {
    return this.http.put<IQuoteType>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteTypeIdentifier(quoteType))}`, quoteType);
  }

  partialUpdate(quoteType: PartialUpdateQuoteType): Observable<IQuoteType> {
    return this.http.patch<IQuoteType>(`${this.resourceUrl}/${encodeURIComponent(this.getQuoteTypeIdentifier(quoteType))}`, quoteType);
  }

  find(id: number): Observable<IQuoteType> {
    return this.http.get<IQuoteType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IQuoteType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IQuoteType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuoteTypeIdentifier(quoteType: Pick<IQuoteType, 'id'>): number {
    return quoteType.id;
  }

  compareQuoteType(o1: Pick<IQuoteType, 'id'> | null, o2: Pick<IQuoteType, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteTypeIdentifier(o1) === this.getQuoteTypeIdentifier(o2) : o1 === o2;
  }

  addQuoteTypeToCollectionIfMissing<Type extends Pick<IQuoteType, 'id'>>(
    quoteTypeCollection: Type[],
    ...quoteTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quoteTypes: Type[] = quoteTypesToCheck.filter(quoteTypeItem => quoteTypeItem !== null && quoteTypeItem !== undefined);
    if (quoteTypes.length > 0) {
      const quoteTypeCollectionIdentifiers = quoteTypeCollection.map(quoteTypeItem => this.getQuoteTypeIdentifier(quoteTypeItem));
      const quoteTypesToAdd = quoteTypes.filter(quoteTypeItem => {
        const quoteTypeIdentifier = this.getQuoteTypeIdentifier(quoteTypeItem);
        if (quoteTypeCollectionIdentifiers.includes(quoteTypeIdentifier)) {
          return false;
        }
        quoteTypeCollectionIdentifiers.push(quoteTypeIdentifier);
        return true;
      });
      return [...quoteTypesToAdd, ...quoteTypeCollection];
    }
    return quoteTypeCollection;
  }
}
