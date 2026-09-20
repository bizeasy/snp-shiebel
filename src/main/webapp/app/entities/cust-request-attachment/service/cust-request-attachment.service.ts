import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICustRequestAttachment, NewCustRequestAttachment } from '../cust-request-attachment.model';

export type PartialUpdateCustRequestAttachment = Partial<ICustRequestAttachment> & Pick<ICustRequestAttachment, 'id'>;

@Service()
export class CustRequestAttachmentsService {
  readonly custRequestAttachmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly custRequestAttachmentsResource = httpResource<ICustRequestAttachment[]>(() => {
    const params = this.custRequestAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of custRequestAttachment that have been fetched. It is updated when the custRequestAttachmentsResource emits a new value.
   * In case of error while fetching the custRequestAttachments, the signal is set to an empty array.
   */
  readonly custRequestAttachments = computed(() =>
    this.custRequestAttachmentsResource.hasValue() ? this.custRequestAttachmentsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cust-request-attachments`;
}

@Service()
export class CustRequestAttachmentService extends CustRequestAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(custRequestAttachment: NewCustRequestAttachment): Observable<ICustRequestAttachment> {
    return this.http.post<ICustRequestAttachment>(this.resourceUrl, custRequestAttachment);
  }

  update(custRequestAttachment: ICustRequestAttachment): Observable<ICustRequestAttachment> {
    return this.http.put<ICustRequestAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestAttachmentIdentifier(custRequestAttachment))}`,
      custRequestAttachment,
    );
  }

  partialUpdate(custRequestAttachment: PartialUpdateCustRequestAttachment): Observable<ICustRequestAttachment> {
    return this.http.patch<ICustRequestAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCustRequestAttachmentIdentifier(custRequestAttachment))}`,
      custRequestAttachment,
    );
  }

  find(id: number): Observable<ICustRequestAttachment> {
    return this.http.get<ICustRequestAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICustRequestAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICustRequestAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCustRequestAttachmentIdentifier(custRequestAttachment: Pick<ICustRequestAttachment, 'id'>): number {
    return custRequestAttachment.id;
  }

  compareCustRequestAttachment(o1: Pick<ICustRequestAttachment, 'id'> | null, o2: Pick<ICustRequestAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustRequestAttachmentIdentifier(o1) === this.getCustRequestAttachmentIdentifier(o2) : o1 === o2;
  }

  addCustRequestAttachmentToCollectionIfMissing<Type extends Pick<ICustRequestAttachment, 'id'>>(
    custRequestAttachmentCollection: Type[],
    ...custRequestAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const custRequestAttachments: Type[] = custRequestAttachmentsToCheck.filter(
      custRequestAttachmentItem => custRequestAttachmentItem !== null && custRequestAttachmentItem !== undefined,
    );
    if (custRequestAttachments.length > 0) {
      const custRequestAttachmentCollectionIdentifiers = custRequestAttachmentCollection.map(custRequestAttachmentItem =>
        this.getCustRequestAttachmentIdentifier(custRequestAttachmentItem),
      );
      const custRequestAttachmentsToAdd = custRequestAttachments.filter(custRequestAttachmentItem => {
        const custRequestAttachmentIdentifier = this.getCustRequestAttachmentIdentifier(custRequestAttachmentItem);
        if (custRequestAttachmentCollectionIdentifiers.includes(custRequestAttachmentIdentifier)) {
          return false;
        }
        custRequestAttachmentCollectionIdentifiers.push(custRequestAttachmentIdentifier);
        return true;
      });
      return [...custRequestAttachmentsToAdd, ...custRequestAttachmentCollection];
    }
    return custRequestAttachmentCollection;
  }
}
