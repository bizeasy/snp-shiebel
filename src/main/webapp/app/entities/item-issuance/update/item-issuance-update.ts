import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from 'app/entities/shipment-item/service/shipment-item.service';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IItemIssuance } from '../item-issuance.model';
import { ItemIssuanceService } from '../service/item-issuance.service';

import { ItemIssuanceFormGroup, ItemIssuanceFormService } from './item-issuance-form.service';

@Component({
  selector: 'sys-item-issuance-update',
  templateUrl: './item-issuance-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ItemIssuanceUpdate implements OnInit {
  readonly isSaving = signal(false);
  itemIssuance: IItemIssuance | null = null;

  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  shipmentsSharedCollection = signal<IShipment[]>([]);
  shipmentItemsSharedCollection = signal<IShipmentItem[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected itemIssuanceService = inject(ItemIssuanceService);
  protected itemIssuanceFormService = inject(ItemIssuanceFormService);
  protected inventoryItemService = inject(InventoryItemService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected shipmentService = inject(ShipmentService);
  protected shipmentItemService = inject(ShipmentItemService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ItemIssuanceFormGroup = this.itemIssuanceFormService.createItemIssuanceFormGroup();

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareShipmentItem = (o1: IShipmentItem | null, o2: IShipmentItem | null): boolean =>
    this.shipmentItemService.compareShipmentItem(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemIssuance }) => {
      this.itemIssuance = itemIssuance;
      if (itemIssuance) {
        this.updateForm(itemIssuance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const itemIssuance = this.itemIssuanceFormService.getItemIssuance(this.editForm);
    if (itemIssuance.id === null) {
      this.subscribeToSaveResponse(this.itemIssuanceService.create(itemIssuance));
    } else {
      this.subscribeToSaveResponse(this.itemIssuanceService.update(itemIssuance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IItemIssuance | null>): void {
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

  protected updateForm(itemIssuance: IItemIssuance): void {
    this.itemIssuance = itemIssuance;
    this.itemIssuanceFormService.resetForm(this.editForm, itemIssuance);

    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, itemIssuance.inventoryItem),
    );
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, itemIssuance.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, itemIssuance.orderItem),
    );
    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, itemIssuance.shipment),
    );
    this.shipmentItemsSharedCollection.update(shipmentItems =>
      this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, itemIssuance.shipmentItem),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, itemIssuance.issuedByParty),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, this.itemIssuance?.inventoryItem),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.itemIssuance?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.itemIssuance?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.itemIssuance?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.shipmentItemService
      .query()
      .pipe(map((res: HttpResponse<IShipmentItem[]>) => res.body ?? []))
      .pipe(
        map((shipmentItems: IShipmentItem[]) =>
          this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, this.itemIssuance?.shipmentItem),
        ),
      )
      .subscribe((shipmentItems: IShipmentItem[]) => this.shipmentItemsSharedCollection.set(shipmentItems));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.itemIssuance?.issuedByParty)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
