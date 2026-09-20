import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderAttachment } from '../order-attachment.model';
import { OrderAttachmentService } from '../service/order-attachment.service';

import { OrderAttachmentFormGroup, OrderAttachmentFormService } from './order-attachment-form.service';

@Component({
  selector: 'sys-order-attachment-update',
  templateUrl: './order-attachment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderAttachmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderAttachment: IOrderAttachment | null = null;

  ordersSharedCollection = signal<IOrder[]>([]);
  attachmentsSharedCollection = signal<IAttachment[]>([]);

  protected orderAttachmentService = inject(OrderAttachmentService);
  protected orderAttachmentFormService = inject(OrderAttachmentFormService);
  protected orderService = inject(OrderService);
  protected attachmentService = inject(AttachmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderAttachmentFormGroup = this.orderAttachmentFormService.createOrderAttachmentFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareAttachment = (o1: IAttachment | null, o2: IAttachment | null): boolean => this.attachmentService.compareAttachment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderAttachment }) => {
      this.orderAttachment = orderAttachment;
      if (orderAttachment) {
        this.updateForm(orderAttachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderAttachment = this.orderAttachmentFormService.getOrderAttachment(this.editForm);
    if (orderAttachment.id === null) {
      this.subscribeToSaveResponse(this.orderAttachmentService.create(orderAttachment));
    } else {
      this.subscribeToSaveResponse(this.orderAttachmentService.update(orderAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderAttachment | null>): void {
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

  protected updateForm(orderAttachment: IOrderAttachment): void {
    this.orderAttachment = orderAttachment;
    this.orderAttachmentFormService.resetForm(this.editForm, orderAttachment);

    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, orderAttachment.order));
    this.attachmentsSharedCollection.update(attachments =>
      this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, orderAttachment.attachment),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.orderAttachment?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.attachmentService
      .query()
      .pipe(map((res: HttpResponse<IAttachment[]>) => res.body ?? []))
      .pipe(
        map((attachments: IAttachment[]) =>
          this.attachmentService.addAttachmentToCollectionIfMissing<IAttachment>(attachments, this.orderAttachment?.attachment),
        ),
      )
      .subscribe((attachments: IAttachment[]) => this.attachmentsSharedCollection.set(attachments));
  }
}
