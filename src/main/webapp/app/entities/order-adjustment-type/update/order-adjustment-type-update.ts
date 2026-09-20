import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderAdjustmentType } from '../order-adjustment-type.model';
import { OrderAdjustmentTypeService } from '../service/order-adjustment-type.service';

import { OrderAdjustmentTypeFormGroup, OrderAdjustmentTypeFormService } from './order-adjustment-type-form.service';

@Component({
  selector: 'sys-order-adjustment-type-update',
  templateUrl: './order-adjustment-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderAdjustmentTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderAdjustmentType: IOrderAdjustmentType | null = null;

  orderAdjustmentTypesSharedCollection = signal<IOrderAdjustmentType[]>([]);

  protected orderAdjustmentTypeService = inject(OrderAdjustmentTypeService);
  protected orderAdjustmentTypeFormService = inject(OrderAdjustmentTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderAdjustmentTypeFormGroup = this.orderAdjustmentTypeFormService.createOrderAdjustmentTypeFormGroup();

  compareOrderAdjustmentType = (o1: IOrderAdjustmentType | null, o2: IOrderAdjustmentType | null): boolean =>
    this.orderAdjustmentTypeService.compareOrderAdjustmentType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderAdjustmentType }) => {
      this.orderAdjustmentType = orderAdjustmentType;
      if (orderAdjustmentType) {
        this.updateForm(orderAdjustmentType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderAdjustmentType = this.orderAdjustmentTypeFormService.getOrderAdjustmentType(this.editForm);
    if (orderAdjustmentType.id === null) {
      this.subscribeToSaveResponse(this.orderAdjustmentTypeService.create(orderAdjustmentType));
    } else {
      this.subscribeToSaveResponse(this.orderAdjustmentTypeService.update(orderAdjustmentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderAdjustmentType | null>): void {
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

  protected updateForm(orderAdjustmentType: IOrderAdjustmentType): void {
    this.orderAdjustmentType = orderAdjustmentType;
    this.orderAdjustmentTypeFormService.resetForm(this.editForm, orderAdjustmentType);

    this.orderAdjustmentTypesSharedCollection.update(orderAdjustmentTypes =>
      this.orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing<IOrderAdjustmentType>(
        orderAdjustmentTypes,
        orderAdjustmentType.parent,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderAdjustmentTypeService
      .query()
      .pipe(map((res: HttpResponse<IOrderAdjustmentType[]>) => res.body ?? []))
      .pipe(
        map((orderAdjustmentTypes: IOrderAdjustmentType[]) =>
          this.orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing<IOrderAdjustmentType>(
            orderAdjustmentTypes,
            this.orderAdjustmentType?.parent,
          ),
        ),
      )
      .subscribe((orderAdjustmentTypes: IOrderAdjustmentType[]) => this.orderAdjustmentTypesSharedCollection.set(orderAdjustmentTypes));
  }
}
