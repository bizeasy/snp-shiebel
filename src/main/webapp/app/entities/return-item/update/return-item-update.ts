import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IReturnHeader } from 'app/entities/return-header/return-header.model';
import { ReturnHeaderService } from 'app/entities/return-header/service/return-header.service';
import { IReturnHeaderType } from 'app/entities/return-header-type/return-header-type.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IReturnItem } from '../return-item.model';
import { ReturnItemService } from '../service/return-item.service';

import { ReturnItemFormGroup, ReturnItemFormService } from './return-item-form.service';
import { ReturnHeaderTypeService } from 'app/entities/return-header-type/service/return-header-type.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';

@Component({
  selector: 'sys-return-item-update',
  templateUrl: './return-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ReturnItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  returnItem: IReturnItem | null = null;

  returnHeadersSharedCollection = signal<IReturnHeader[]>([]);
  returnHeaderTypesSharedCollection = signal<IReturnHeaderType[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected returnItemService = inject(ReturnItemService);
  protected returnItemFormService = inject(ReturnItemFormService);
  protected returnHeaderService = inject(ReturnHeaderService);
  protected returnHeaderTypeService = inject(ReturnHeaderTypeService);
  protected productService = inject(ProductService);
  protected orderItemService = inject(OrderItemService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReturnItemFormGroup = this.returnItemFormService.createReturnItemFormGroup();

  compareReturnHeader = (o1: IReturnHeader | null, o2: IReturnHeader | null): boolean =>
    this.returnHeaderService.compareReturnHeader(o1, o2);

  compareReturnHeaderType = (o1: IReturnHeaderType | null, o2: IReturnHeaderType | null): boolean =>
    this.returnHeaderTypeService.compareReturnHeaderType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnItem }) => {
      this.returnItem = returnItem;
      if (returnItem) {
        this.updateForm(returnItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const returnItem = this.returnItemFormService.getReturnItem(this.editForm);
    if (returnItem.id === null) {
      this.subscribeToSaveResponse(this.returnItemService.create(returnItem));
    } else {
      this.subscribeToSaveResponse(this.returnItemService.update(returnItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IReturnItem | null>): void {
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

  protected updateForm(returnItem: IReturnItem): void {
    this.returnItem = returnItem;
    this.returnItemFormService.resetForm(this.editForm, returnItem);

    this.returnHeadersSharedCollection.update(returnHeaders =>
      this.returnHeaderService.addReturnHeaderToCollectionIfMissing<IReturnHeader>(returnHeaders, returnItem.returnHeader),
    );
    this.returnHeaderTypesSharedCollection.update(returnHeaderTypes =>
      this.returnHeaderTypeService.addReturnHeaderTypeToCollectionIfMissing<IReturnHeaderType>(returnHeaderTypes, returnItem.returnType),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, returnItem.product),
    );
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, returnItem.orderItem),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, returnItem.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.returnHeaderService
      .query()
      .pipe(map((res: HttpResponse<IReturnHeader[]>) => res.body ?? []))
      .pipe(
        map((returnHeaders: IReturnHeader[]) =>
          this.returnHeaderService.addReturnHeaderToCollectionIfMissing<IReturnHeader>(returnHeaders, this.returnItem?.returnHeader),
        ),
      )
      .subscribe((returnHeaders: IReturnHeader[]) => this.returnHeadersSharedCollection.set(returnHeaders));

    this.returnHeaderTypeService
      .query()
      .pipe(map((res: HttpResponse<IReturnHeaderType[]>) => res.body ?? []))
      .pipe(
        map((returnHeaderTypes: IReturnHeaderType[]) =>
          this.returnHeaderTypeService.addReturnHeaderTypeToCollectionIfMissing<IReturnHeaderType>(
            returnHeaderTypes,
            this.returnItem?.returnType,
          ),
        ),
      )
      .subscribe((returnHeaderTypes: IReturnHeaderType[]) => this.returnHeaderTypesSharedCollection.set(returnHeaderTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.returnItem?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.returnItem?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.returnItem?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
