import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequestItemAttachment, NewCustRequestItemAttachment } from '../cust-request-item-attachment.model';

export type PartialUpdateCustRequestItemAttachment = Partial<ICustRequestItemAttachment> & Pick<ICustRequestItemAttachment, 'id'>;

@Service()
export class CustRequestItemAttachmentsService {
  readonly custRequestItemAttachmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly custRequestItemAttachmentsResource = httpResource<ICustRequestItemAttachment[]>(() => {
    const params = this.custRequestItemAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequestItemAttachment that have been fetched. It is updated when the custRequestItemAttachmentsResource emits a new value.
   * In case of error while fetching the custRequestItemAttachments, the signal is set to an empty array.
   */
  readonly custRequestItemAttachments = computed(() =>
    this.custRequestItemAttachmentsResource.hasValue() ? this.custRequestItemAttachmentsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cust-request-item-attachments`;
}

@Service()
export class CustRequestItemAttachmentService extends CustRequestItemAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(custRequestItemAttachment: NewCustRequestItemAttachment): Observable<ICustRequestItemAttachment> {
    return this.http.post<ICustRequestItemAttachment>(this.resourceUrl, custRequestItemAttachment);
  }

  update(custRequestItemAttachment: ICustRequestItemAttachment): Observable<ICustRequestItemAttachment> {
    return this.http.put<ICustRequestItemAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestItemAttachmentIdentifier(custRequestItemAttachment))}`,
      custRequestItemAttachment,
    );
  }

  partialUpdate(custRequestItemAttachment: PartialUpdateCustRequestItemAttachment): Observable<ICustRequestItemAttachment> {
    return this.http.patch<ICustRequestItemAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestItemAttachmentIdentifier(custRequestItemAttachment))}`,
      custRequestItemAttachment,
    );
  }

  find(id: number): Observable<ICustRequestItemAttachment> {
    return this.http.get<ICustRequestItemAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustRequestItemAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustRequestItemAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestItemAttachmentIdentifier(custRequestItemAttachment: Pick<ICustRequestItemAttachment, 'id'>): number {
    return custRequestItemAttachment.id;
  }

  compareCustRequestItemAttachment(
    o1: Pick<ICustRequestItemAttachment, 'id'> | null,
    o2: Pick<ICustRequestItemAttachment, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getCustRequestItemAttachmentIdentifier(o1) === this.getCustRequestItemAttachmentIdentifier(o2) : o1 === o2;
  }

  addCustRequestItemAttachmentToCollectionIfMissing<Type extends Pick<ICustRequestItemAttachment, 'id'>>(
    custRequestItemAttachmentCollection: Type[],
    ...custRequestItemAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequestItemAttachments: Type[] = custRequestItemAttachmentsToCheck.filter(
      custRequestItemAttachmentItem => custRequestItemAttachmentItem !== null && custRequestItemAttachmentItem !== undefined,
    );
    if (custRequestItemAttachments.length > 0) {
      const custRequestItemAttachmentCollectionIdentifiers = custRequestItemAttachmentCollection.map(custRequestItemAttachmentItem =>
        this.getCustRequestItemAttachmentIdentifier(custRequestItemAttachmentItem),
      );
      const custRequestItemAttachmentsToAdd = custRequestItemAttachments.filter(custRequestItemAttachmentItem => {
        const custRequestItemAttachmentIdentifier = this.getCustRequestItemAttachmentIdentifier(custRequestItemAttachmentItem);
        if (custRequestItemAttachmentCollectionIdentifiers.includes(custRequestItemAttachmentIdentifier)) {
          return false;
        }
        custRequestItemAttachmentCollectionIdentifiers.push(custRequestItemAttachmentIdentifier);
        return true;
      });
      return [...custRequestItemAttachmentsToAdd, ...custRequestItemAttachmentCollection];
    }
    return custRequestItemAttachmentCollection;
  }
}
