import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderType } from '../order-type.model';
import { OrderTypeService } from '../service/order-type.service';

import { OrderTypeFormGroup, OrderTypeFormService } from './order-type-form.service';

@Component({
  selector: 'sys-order-type-update',
  templateUrl: './order-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderType: IOrderType | null = null;

  protected orderTypeService = inject(OrderTypeService);
  protected orderTypeFormService = inject(OrderTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderTypeFormGroup = this.orderTypeFormService.createOrderTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderType }) => {
      this.orderType = orderType;
      if (orderType) {
        this.updateForm(orderType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderType = this.orderTypeFormService.getOrderType(this.editForm);
    if (orderType.id === null) {
      this.subscribeToSaveResponse(this.orderTypeService.create(orderType));
    } else {
      this.subscribeToSaveResponse(this.orderTypeService.update(orderType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderType | null>): void {
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

  protected updateForm(orderType: IOrderType): void {
    this.orderType = orderType;
    this.orderTypeFormService.resetForm(this.editForm, orderType);
  }
}
