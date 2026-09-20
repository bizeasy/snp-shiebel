import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IShipmentAttachment, NewShipmentAttachment } from '../shipment-attachment.model';

export type PartialUpdateShipmentAttachment = Partial<IShipmentAttachment> & Pick<IShipmentAttachment, 'id'>;

@Service()
export class ShipmentAttachmentsService {
  readonly shipmentAttachmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly shipmentAttachmentsResource = httpResource<IShipmentAttachment[]>(() => {
    const params = this.shipmentAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of shipmentAttachment that have been fetched. It is updated when the shipmentAttachmentsResource emits a new value.
   * In case of error while fetching the shipmentAttachments, the signal is set to an empty array.
   */
  readonly shipmentAttachments = computed(() =>
    this.shipmentAttachmentsResource.hasValue() ? this.shipmentAttachmentsResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/shipment-attachments`;
}

@Service()
export class ShipmentAttachmentService extends ShipmentAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(shipmentAttachment: NewShipmentAttachment): Observable<IShipmentAttachment> {
    return this.http.post<IShipmentAttachment>(this.resourceUrl, shipmentAttachment);
  }

  update(shipmentAttachment: IShipmentAttachment): Observable<IShipmentAttachment> {
    return this.http.put<IShipmentAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentAttachmentIdentifier(shipmentAttachment))}`,
      shipmentAttachment,
    );
  }

  partialUpdate(shipmentAttachment: PartialUpdateShipmentAttachment): Observable<IShipmentAttachment> {
    return this.http.patch<IShipmentAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getShipmentAttachmentIdentifier(shipmentAttachment))}`,
      shipmentAttachment,
    );
  }

  find(id: number): Observable<IShipmentAttachment> {
    return this.http.get<IShipmentAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IShipmentAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IShipmentAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getShipmentAttachmentIdentifier(shipmentAttachment: Pick<IShipmentAttachment, 'id'>): number {
    return shipmentAttachment.id;
  }

  compareShipmentAttachment(o1: Pick<IShipmentAttachment, 'id'> | null, o2: Pick<IShipmentAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipmentAttachmentIdentifier(o1) === this.getShipmentAttachmentIdentifier(o2) : o1 === o2;
  }

  addShipmentAttachmentToCollectionIfMissing<Type extends Pick<IShipmentAttachment, 'id'>>(
    shipmentAttachmentCollection: Type[],
    ...shipmentAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shipmentAttachments: Type[] = shipmentAttachmentsToCheck.filter(
      shipmentAttachmentItem => shipmentAttachmentItem !== null && shipmentAttachmentItem !== undefined,
    );
    if (shipmentAttachments.length > 0) {
      const shipmentAttachmentCollectionIdentifiers = shipmentAttachmentCollection.map(shipmentAttachmentItem =>
        this.getShipmentAttachmentIdentifier(shipmentAttachmentItem),
      );
      const shipmentAttachmentsToAdd = shipmentAttachments.filter(shipmentAttachmentItem => {
        const shipmentAttachmentIdentifier = this.getShipmentAttachmentIdentifier(shipmentAttachmentItem);
        if (shipmentAttachmentCollectionIdentifiers.includes(shipmentAttachmentIdentifier)) {
          return false;
        }
        shipmentAttachmentCollectionIdentifiers.push(shipmentAttachmentIdentifier);
        return true;
      });
      return [...shipmentAttachmentsToAdd, ...shipmentAttachmentCollection];
    }
    return shipmentAttachmentCollection;
  }
}
