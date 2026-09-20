import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrderItemAttachment, NewOrderItemAttachment } from '../order-item-attachment.model';

export type PartialUpdateOrderItemAttachment = Partial<IOrderItemAttachment> & Pick<IOrderItemAttachment, 'id'>;

@Service()
export class OrderItemAttachmentsService {
  readonly orderItemAttachmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly orderItemAttachmentsResource = httpResource<IOrderItemAttachment[]>(() => {
    const params = this.orderItemAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orderItemAttachment that have been fetched. It is updated when the orderItemAttachmentsResource emits a new value.
   * In case of error while fetching the orderItemAttachments, the signal is set to an empty array.
   */
  readonly orderItemAttachments = computed(() =>
    this.orderItemAttachmentsResource.hasValue() ? this.orderItemAttachmentsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/order-item-attachments`;
}

@Service()
export class OrderItemAttachmentService extends OrderItemAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(orderItemAttachment: NewOrderItemAttachment): Observable<IOrderItemAttachment> {
    return this.http.post<IOrderItemAttachment>(this.resourceUrl, orderItemAttachment);
  }

  update(orderItemAttachment: IOrderItemAttachment): Observable<IOrderItemAttachment> {
    return this.http.put<IOrderItemAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderItemAttachmentIdentifier(orderItemAttachment))}`,
      orderItemAttachment,
    );
  }

  partialUpdate(orderItemAttachment: PartialUpdateOrderItemAttachment): Observable<IOrderItemAttachment> {
    return this.http.patch<IOrderItemAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getOrderItemAttachmentIdentifier(orderItemAttachment))}`,
      orderItemAttachment,
    );
  }

  find(id: number): Observable<IOrderItemAttachment> {
    return this.http.get<IOrderItemAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOrderItemAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOrderItemAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrderItemAttachmentIdentifier(orderItemAttachment: Pick<IOrderItemAttachment, 'id'>): number {
    return orderItemAttachment.id;
  }

  compareOrderItemAttachment(o1: Pick<IOrderItemAttachment, 'id'> | null, o2: Pick<IOrderItemAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderItemAttachmentIdentifier(o1) === this.getOrderItemAttachmentIdentifier(o2) : o1 === o2;
  }

  addOrderItemAttachmentToCollectionIfMissing<Type extends Pick<IOrderItemAttachment, 'id'>>(
    orderItemAttachmentCollection: Type[],
    ...orderItemAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderItemAttachments: Type[] = orderItemAttachmentsToCheck.filter(
      orderItemAttachmentItem => orderItemAttachmentItem !== null && orderItemAttachmentItem !== undefined,
    );
    if (orderItemAttachments.length > 0) {
      const orderItemAttachmentCollectionIdentifiers = orderItemAttachmentCollection.map(orderItemAttachmentItem =>
        this.getOrderItemAttachmentIdentifier(orderItemAttachmentItem),
      );
      const orderItemAttachmentsToAdd = orderItemAttachments.filter(orderItemAttachmentItem => {
        const orderItemAttachmentIdentifier = this.getOrderItemAttachmentIdentifier(orderItemAttachmentItem);
        if (orderItemAttachmentCollectionIdentifiers.includes(orderItemAttachmentIdentifier)) {
          return false;
        }
        orderItemAttachmentCollectionIdentifiers.push(orderItemAttachmentIdentifier);
        return true;
      });
      return [...orderItemAttachmentsToAdd, ...orderItemAttachmentCollection];
    }
    return orderItemAttachmentCollection;
  }
}
