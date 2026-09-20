import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderAttachment, NewOrderAttachment } from '../order-attachment.model';

export type PartialUpdateOrderAttachment = Partial<IOrderAttachment> & Pick<IOrderAttachment, 'id'>;

@Service()
export class OrderAttachmentsService {
  readonly orderAttachmentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly orderAttachmentsResource = httpResource<IOrderAttachment[]>(() => {
    const params = this.orderAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderAttachment that have been fetched. It is updated when the orderAttachmentsResource emits a new value.
   * In case of error while fetching the orderAttachments, the signal is set to an empty array.
   */
  readonly orderAttachments = computed(() => (this.orderAttachmentsResource.hasValue() ? this.orderAttachmentsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/order-attachments`;
}

@Service()
export class OrderAttachmentService extends OrderAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(orderAttachment: NewOrderAttachment): Observable<IOrderAttachment> {
    return this.http.post<IOrderAttachment>(this.resourceUrl, orderAttachment);
  }

  update(orderAttachment: IOrderAttachment): Observable<IOrderAttachment> {
    return this.http.put<IOrderAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderAttachmentIdentifier(orderAttachment))}`,
      orderAttachment,
    );
  }

  partialUpdate(orderAttachment: PartialUpdateOrderAttachment): Observable<IOrderAttachment> {
    return this.http.patch<IOrderAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderAttachmentIdentifier(orderAttachment))}`,
      orderAttachment,
    );
  }

  find(id: number): Observable<IOrderAttachment> {
    return this.http.get<IOrderAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderAttachmentIdentifier(orderAttachment: Pick<IOrderAttachment, 'id'>): number {
    return orderAttachment.id;
  }

  compareOrderAttachment(o1: Pick<IOrderAttachment, 'id'> | null, o2: Pick<IOrderAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderAttachmentIdentifier(o1) === this.getOrderAttachmentIdentifier(o2) : o1 === o2;
  }

  addOrderAttachmentToCollectionIfMissing<Type extends Pick<IOrderAttachment, 'id'>>(
    orderAttachmentCollection: Type[],
    ...orderAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderAttachments: Type[] = orderAttachmentsToCheck.filter(
      orderAttachmentItem => orderAttachmentItem !== null && orderAttachmentItem !== undefined,
    );
    if (orderAttachments.length > 0) {
      const orderAttachmentCollectionIdentifiers = orderAttachmentCollection.map(orderAttachmentItem =>
        this.getOrderAttachmentIdentifier(orderAttachmentItem),
      );
      const orderAttachmentsToAdd = orderAttachments.filter(orderAttachmentItem => {
        const orderAttachmentIdentifier = this.getOrderAttachmentIdentifier(orderAttachmentItem);
        if (orderAttachmentCollectionIdentifiers.includes(orderAttachmentIdentifier)) {
          return false;
        }
        orderAttachmentCollectionIdentifiers.push(orderAttachmentIdentifier);
        return true;
      });
      return [...orderAttachmentsToAdd, ...orderAttachmentCollection];
    }
    return orderAttachmentCollection;
  }
}
