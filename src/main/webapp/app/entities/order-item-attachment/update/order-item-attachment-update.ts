import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItemAttachment } from '../order-item-attachment.model';
import { OrderItemAttachmentService } from '../service/order-item-attachment.service';

import { OrderItemAttachmentFormGroup, OrderItemAttachmentFormService } from './order-item-attachment-form.service';

@Component({
  selector: 'sys-order-item-attachment-update',
  templateUrl: './order-item-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderItemAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderItemAttachment: IOrderItemAttachment | null = null;

  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected orderItemAttachmentService = inject(OrderItemAttachmentService);
  protected orderItemAttachmentFormService = inject(OrderItemAttachmentFormService);
  protected orderItemService = inject(OrderItemService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderItemAttachmentFormGroup = this.orderItemAttachmentFormService.createOrderItemAttachmentFormGroup();

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemAttachment }) => {
      this.orderItemAttachment = orderItemAttachment;
      if (orderItemAttachment) {
        this.updateForm(orderItemAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderItemAttachment = this.orderItemAttachmentFormService.getOrderItemAttachment(this.editForm);
    if (orderItemAttachment.id === null) {
      this.subscribeToSaveResponse(this.orderItemAttachmentService.create(orderItemAttachment));
    } else {
      this.subscribeToSaveResponse(this.orderItemAttachmentService.update(orderItemAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderItemAttachment | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(orderItemAttachment: IOrderItemAttachment): void {
    this.orderItemAttachment = orderItemAttachment;
    this.orderItemAttachmentFormService.resetForm(this.editForm, orderItemAttachment);

    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, orderItemAttachment.orderItem),
    );
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, orderItemAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.orderItemAttachment?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.orderItemAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
