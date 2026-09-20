import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItemType } from '../order-item-type.model';
import { OrderItemTypeService } from '../service/order-item-type.service';

import { OrderItemTypeFormGroup, OrderItemTypeFormService } from './order-item-type-form.service';

@Component({
  selector: 'sys-order-item-type-update',
  templateUrl: './order-item-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderItemTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderItemType: IOrderItemType | null = null;

  protected orderItemTypeService = inject(OrderItemTypeService);
  protected orderItemTypeFormService = inject(OrderItemTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderItemTypeFormGroup = this.orderItemTypeFormService.createOrderItemTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemType }) => {
      this.orderItemType = orderItemType;
      if (orderItemType) {
        this.updateForm(orderItemType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderItemType = this.orderItemTypeFormService.getOrderItemType(this.editForm);
    if (orderItemType.id === null) {
      this.subscribeToSaveResponse(this.orderItemTypeService.create(orderItemType));
    } else {
      this.subscribeToSaveResponse(this.orderItemTypeService.update(orderItemType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderItemType | null>): void {
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

  protected updateForm(orderItemType: IOrderItemType): void {
    this.orderItemType = orderItemType;
    this.orderItemTypeFormService.resetForm(this.editForm, orderItemType);
  }
}
