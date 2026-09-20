import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICashBookAttachment, NewCashBookAttachment } from '../cash-book-attachment.model';

export type PartialUpdateCashBookAttachment = Partial<ICashBookAttachment> & Pick<ICashBookAttachment, 'id'>;

@Service()
export class CashBookAttachmentsService {
  readonly cashBookAttachmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly cashBookAttachmentsResource = httpResource<ICashBookAttachment[]>(() => {
    const params = this.cashBookAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of cashBookAttachment that have been fetched. It is updated when the cashBookAttachmentsResource emits a new value.
   * In case of error while fetching the cashBookAttachments, the signal is set to an empty array.
   */
  readonly cashBookAttachments = computed(() =>
    this.cashBookAttachmentsResource.hasValue() ? this.cashBookAttachmentsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cash-book-attachments`;
}

@Service()
export class CashBookAttachmentService extends CashBookAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(cashBookAttachment: NewCashBookAttachment): Observable<ICashBookAttachment> {
    return this.http.post<ICashBookAttachment>(this.resourceUrl, cashBookAttachment);
  }

  update(cashBookAttachment: ICashBookAttachment): Observable<ICashBookAttachment> {
    return this.http.put<ICashBookAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCashBookAttachmentIdentifier(cashBookAttachment))}`,
      cashBookAttachment,
    );
  }

  partialUpdate(cashBookAttachment: PartialUpdateCashBookAttachment): Observable<ICashBookAttachment> {
    return this.http.patch<ICashBookAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCashBookAttachmentIdentifier(cashBookAttachment))}`,
      cashBookAttachment,
    );
  }

  find(id: number): Observable<ICashBookAttachment> {
    return this.http.get<ICashBookAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICashBookAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICashBookAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCashBookAttachmentIdentifier(cashBookAttachment: Pick<ICashBookAttachment, 'id'>): number {
    return cashBookAttachment.id;
  }

  compareCashBookAttachment(o1: Pick<ICashBookAttachment, 'id'> | null, o2: Pick<ICashBookAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getCashBookAttachmentIdentifier(o1) === this.getCashBookAttachmentIdentifier(o2) : o1 === o2;
  }

  addCashBookAttachmentToCollectionIfMissing<Type extends Pick<ICashBookAttachment, 'id'>>(
    cashBookAttachmentCollection: Type[],
    ...cashBookAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cashBookAttachments: Type[] = cashBookAttachmentsToCheck.filter(
      cashBookAttachmentItem => cashBookAttachmentItem !== null && cashBookAttachmentItem !== undefined,
    );
    if (cashBookAttachments.length > 0) {
      const cashBookAttachmentCollectionIdentifiers = cashBookAttachmentCollection.map(cashBookAttachmentItem =>
        this.getCashBookAttachmentIdentifier(cashBookAttachmentItem),
      );
      const cashBookAttachmentsToAdd = cashBookAttachments.filter(cashBookAttachmentItem => {
        const cashBookAttachmentIdentifier = this.getCashBookAttachmentIdentifier(cashBookAttachmentItem);
        if (cashBookAttachmentCollectionIdentifiers.includes(cashBookAttachmentIdentifier)) {
          return false;
        }
        cashBookAttachmentCollectionIdentifiers.push(cashBookAttachmentIdentifier);
        return true;
      });
      return [...cashBookAttachmentsToAdd, ...cashBookAttachmentCollection];
    }
    return cashBookAttachmentCollection;
  }
}
