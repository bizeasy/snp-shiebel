import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuoteAttachment, NewQuoteAttachment } from '../quote-attachment.model';

export type PartialUpdateQuoteAttachment = Partial<IQuoteAttachment> & Pick<IQuoteAttachment, 'id'>;

@Service()
export class QuoteAttachmentsService {
  readonly quoteAttachmentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quoteAttachmentsResource = httpResource<IQuoteAttachment[]>(() => {
    const params = this.quoteAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quoteAttachment that have been fetched. It is updated when the quoteAttachmentsResource emits a new value.
   * In case of error while fetching the quoteAttachments, the signal is set to an empty array.
   */
  readonly quoteAttachments = computed(() => (this.quoteAttachmentsResource.hasValue() ? this.quoteAttachmentsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/quote-attachments`;
}

@Service()
export class QuoteAttachmentService extends QuoteAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(quoteAttachment: NewQuoteAttachment): Observable<IQuoteAttachment> {
    return this.http.post<IQuoteAttachment>(this.resourceUrl, quoteAttachment);
  }

  update(quoteAttachment: IQuoteAttachment): Observable<IQuoteAttachment> {
    return this.http.put<IQuoteAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getQuoteAttachmentIdentifier(quoteAttachment))}`,
      quoteAttachment,
    );
  }

  partialUpdate(quoteAttachment: PartialUpdateQuoteAttachment): Observable<IQuoteAttachment> {
    return this.http.patch<IQuoteAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getQuoteAttachmentIdentifier(quoteAttachment))}`,
      quoteAttachment,
    );
  }

  find(id: number): Observable<IQuoteAttachment> {
    return this.http.get<IQuoteAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IQuoteAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IQuoteAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuoteAttachmentIdentifier(quoteAttachment: Pick<IQuoteAttachment, 'id'>): number {
    return quoteAttachment.id;
  }

  compareQuoteAttachment(o1: Pick<IQuoteAttachment, 'id'> | null, o2: Pick<IQuoteAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuoteAttachmentIdentifier(o1) === this.getQuoteAttachmentIdentifier(o2) : o1 === o2;
  }

  addQuoteAttachmentToCollectionIfMissing<Type extends Pick<IQuoteAttachment, 'id'>>(
    quoteAttachmentCollection: Type[],
    ...quoteAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quoteAttachments: Type[] = quoteAttachmentsToCheck.filter(
      quoteAttachmentItem => quoteAttachmentItem !== null && quoteAttachmentItem !== undefined,
    );
    if (quoteAttachments.length > 0) {
      const quoteAttachmentCollectionIdentifiers = quoteAttachmentCollection.map(quoteAttachmentItem =>
        this.getQuoteAttachmentIdentifier(quoteAttachmentItem),
      );
      const quoteAttachmentsToAdd = quoteAttachments.filter(quoteAttachmentItem => {
        const quoteAttachmentIdentifier = this.getQuoteAttachmentIdentifier(quoteAttachmentItem);
        if (quoteAttachmentCollectionIdentifiers.includes(quoteAttachmentIdentifier)) {
          return false;
        }
        quoteAttachmentCollectionIdentifiers.push(quoteAttachmentIdentifier);
        return true;
      });
      return [...quoteAttachmentsToAdd, ...quoteAttachmentCollection];
    }
    return quoteAttachmentCollection;
  }
}
