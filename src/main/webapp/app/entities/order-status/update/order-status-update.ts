import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderStatus } from '../order-status.model';
import { OrderStatusService } from '../service/order-status.service';

import { OrderStatusFormGroup, OrderStatusFormService } from './order-status-form.service';

@Component({
  selector: 'sys-order-status-update',
  templateUrl: './order-status-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderStatusUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderStatus: IOrderStatus | null = null;

  ordersSharedCollection = signal<IOrder[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected orderStatusService = inject(OrderStatusService);
  protected orderStatusFormService = inject(OrderStatusFormService);
  protected orderService = inject(OrderService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderStatusFormGroup = this.orderStatusFormService.createOrderStatusFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderStatus }) => {
      this.orderStatus = orderStatus;
      if (orderStatus) {
        this.updateForm(orderStatus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderStatus = this.orderStatusFormService.getOrderStatus(this.editForm);
    if (orderStatus.id === null) {
      this.subscribeToSaveResponse(this.orderStatusService.create(orderStatus));
    } else {
      this.subscribeToSaveResponse(this.orderStatusService.update(orderStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderStatus | null>): void {
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

  protected updateForm(orderStatus: IOrderStatus): void {
    this.orderStatus = orderStatus;
    this.orderStatusFormService.resetForm(this.editForm, orderStatus);

    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, orderStatus.order));
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, orderStatus.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.orderStatus?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.orderStatus?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
