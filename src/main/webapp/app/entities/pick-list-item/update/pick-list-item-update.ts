import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { FacilityLocationService } from 'app/entities/facility-location/service/facility-location.service';
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IPickList } from 'app/entities/pick-list/pick-list.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IPickListItem } from '../pick-list-item.model';
import { PickListItemService } from '../service/pick-list-item.service';

import { PickListItemFormGroup, PickListItemFormService } from './pick-list-item-form.service';
import { PickListService } from 'app/entities/pick-list/service/pick-list.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';

@Component({
  selector: 'sys-pick-list-item-update',
  templateUrl: './pick-list-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PickListItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  pickListItem: IPickListItem | null = null;

  pickListsSharedCollection = signal<IPickList[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  facilityLocationsSharedCollection = signal<IFacilityLocation[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected pickListItemService = inject(PickListItemService);
  protected pickListItemFormService = inject(PickListItemFormService);
  protected pickListService = inject(PickListService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected inventoryItemService = inject(InventoryItemService);
  protected productService = inject(ProductService);
  protected facilityLocationService = inject(FacilityLocationService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PickListItemFormGroup = this.pickListItemFormService.createPickListItemFormGroup();

  comparePickList = (o1: IPickList | null, o2: IPickList | null): boolean => this.pickListService.comparePickList(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareFacilityLocation = (o1: IFacilityLocation | null, o2: IFacilityLocation | null): boolean =>
    this.facilityLocationService.compareFacilityLocation(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pickListItem }) => {
      this.pickListItem = pickListItem;
      if (pickListItem) {
        this.updateForm(pickListItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const pickListItem = this.pickListItemFormService.getPickListItem(this.editForm);
    if (pickListItem.id === null) {
      this.subscribeToSaveResponse(this.pickListItemService.create(pickListItem));
    } else {
      this.subscribeToSaveResponse(this.pickListItemService.update(pickListItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPickListItem | null>): void {
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

  protected updateForm(pickListItem: IPickListItem): void {
    this.pickListItem = pickListItem;
    this.pickListItemFormService.resetForm(this.editForm, pickListItem);

    this.pickListsSharedCollection.update(pickLists =>
      this.pickListService.addPickListToCollectionIfMissing<IPickList>(pickLists, pickListItem.pickList),
    );
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, pickListItem.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, pickListItem.orderItem),
    );
    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, pickListItem.inventoryItem),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, pickListItem.product),
    );
    this.facilityLocationsSharedCollection.update(facilityLocations =>
      this.facilityLocationService.addFacilityLocationToCollectionIfMissing<IFacilityLocation>(
        facilityLocations,
        pickListItem.facilityLocation,
      ),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, pickListItem.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pickListService
      .query()
      .pipe(map((res: HttpResponse<IPickList[]>) => res.body ?? []))
      .pipe(
        map((pickLists: IPickList[]) =>
          this.pickListService.addPickListToCollectionIfMissing<IPickList>(pickLists, this.pickListItem?.pickList),
        ),
      )
      .subscribe((pickLists: IPickList[]) => this.pickListsSharedCollection.set(pickLists));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.pickListItem?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.pickListItem?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, this.pickListItem?.inventoryItem),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.pickListItem?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.facilityLocationService
      .query()
      .pipe(map((res: HttpResponse<IFacilityLocation[]>) => res.body ?? []))
      .pipe(
        map((facilityLocations: IFacilityLocation[]) =>
          this.facilityLocationService.addFacilityLocationToCollectionIfMissing<IFacilityLocation>(
            facilityLocations,
            this.pickListItem?.facilityLocation,
          ),
        ),
      )
      .subscribe((facilityLocations: IFacilityLocation[]) => this.facilityLocationsSharedCollection.set(facilityLocations));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.pickListItem?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
