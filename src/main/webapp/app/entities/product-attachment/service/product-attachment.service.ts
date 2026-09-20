import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IProductAttachment, NewProductAttachment } from '../product-attachment.model';

export type PartialUpdateProductAttachment = Partial<IProductAttachment> & Pick<IProductAttachment, 'id'>;

@Service()
export class ProductAttachmentsService {
  readonly productAttachmentsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly productAttachmentsResource = httpResource<IProductAttachment[]>(() => {
    const params = this.productAttachmentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of productAttachment that have been fetched. It is updated when the productAttachmentsResource emits a new value.
   * In case of error while fetching the productAttachments, the signal is set to an empty array.
   */
  readonly productAttachments = computed(() => (this.productAttachmentsResource.hasValue() ? this.productAttachmentsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/product-attachments`;
}

@Service()
export class ProductAttachmentService extends ProductAttachmentsService {
  protected readonly http = inject(HttpClient);

  create(productAttachment: NewProductAttachment): Observable<IProductAttachment> {
    return this.http.post<IProductAttachment>(this.resourceUrl, productAttachment);
  }

  update(productAttachment: IProductAttachment): Observable<IProductAttachment> {
    return this.http.put<IProductAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductAttachmentIdentifier(productAttachment))}`,
      productAttachment,
    );
  }

  partialUpdate(productAttachment: PartialUpdateProductAttachment): Observable<IProductAttachment> {
    return this.http.patch<IProductAttachment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getProductAttachmentIdentifier(productAttachment))}`,
      productAttachment,
    );
  }

  find(id: number): Observable<IProductAttachment> {
    return this.http.get<IProductAttachment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IProductAttachment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IProductAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getProductAttachmentIdentifier(productAttachment: Pick<IProductAttachment, 'id'>): number {
    return productAttachment.id;
  }

  compareProductAttachment(o1: Pick<IProductAttachment, 'id'> | null, o2: Pick<IProductAttachment, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductAttachmentIdentifier(o1) === this.getProductAttachmentIdentifier(o2) : o1 === o2;
  }

  addProductAttachmentToCollectionIfMissing<Type extends Pick<IProductAttachment, 'id'>>(
    productAttachmentCollection: Type[],
    ...productAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productAttachments: Type[] = productAttachmentsToCheck.filter(
      productAttachmentItem => productAttachmentItem !== null && productAttachmentItem !== undefined,
    );
    if (productAttachments.length > 0) {
      const productAttachmentCollectionIdentifiers = productAttachmentCollection.map(productAttachmentItem =>
        this.getProductAttachmentIdentifier(productAttachmentItem),
      );
      const productAttachmentsToAdd = productAttachments.filter(productAttachmentItem => {
        const productAttachmentIdentifier = this.getProductAttachmentIdentifier(productAttachmentItem);
        if (productAttachmentCollectionIdentifiers.includes(productAttachmentIdentifier)) {
          return false;
        }
        productAttachmentCollectionIdentifiers.push(productAttachmentIdentifier);
        return true;
      });
      return [...productAttachmentsToAdd, ...productAttachmentCollection];
    }
    return productAttachmentCollection;
  }
}
