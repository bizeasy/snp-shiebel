import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { PartyService } from 'app/entities/party/service/party.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from 'app/entities/shipment-item/service/shipment-item.service';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShipmentReceiptService } from '../service/shipment-receipt.service';
import { IShipmentReceipt } from '../shipment-receipt.model';

import { ShipmentReceiptFormGroup, ShipmentReceiptFormService } from './shipment-receipt-form.service';

@Component({
  selector: 'sys-shipment-receipt-update',
  templateUrl: './shipment-receipt-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentReceiptUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipmentReceipt: IShipmentReceipt | null = null;

  shipmentsSharedCollection = signal<IShipment[]>([]);
  shipmentItemsSharedCollection = signal<IShipmentItem[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected shipmentReceiptService = inject(ShipmentReceiptService);
  protected shipmentReceiptFormService = inject(ShipmentReceiptFormService);
  protected shipmentService = inject(ShipmentService);
  protected shipmentItemService = inject(ShipmentItemService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected inventoryItemService = inject(InventoryItemService);
  protected productService = inject(ProductService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentReceiptFormGroup = this.shipmentReceiptFormService.createShipmentReceiptFormGroup();

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareShipmentItem = (o1: IShipmentItem | null, o2: IShipmentItem | null): boolean =>
    this.shipmentItemService.compareShipmentItem(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentReceipt }) => {
      this.shipmentReceipt = shipmentReceipt;
      if (shipmentReceipt) {
        this.updateForm(shipmentReceipt);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipmentReceipt = this.shipmentReceiptFormService.getShipmentReceipt(this.editForm);
    if (shipmentReceipt.id === null) {
      this.subscribeToSaveResponse(this.shipmentReceiptService.create(shipmentReceipt));
    } else {
      this.subscribeToSaveResponse(this.shipmentReceiptService.update(shipmentReceipt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipmentReceipt | null>): void {
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

  protected updateForm(shipmentReceipt: IShipmentReceipt): void {
    this.shipmentReceipt = shipmentReceipt;
    this.shipmentReceiptFormService.resetForm(this.editForm, shipmentReceipt);

    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, shipmentReceipt.shipment),
    );
    this.shipmentItemsSharedCollection.update(shipmentItems =>
      this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, shipmentReceipt.shipmentItem),
    );
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, shipmentReceipt.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, shipmentReceipt.orderItem),
    );
    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, shipmentReceipt.inventoryItem),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, shipmentReceipt.product),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, shipmentReceipt.receivedBy),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.shipmentReceipt?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.shipmentItemService
      .query()
      .pipe(map((res: HttpResponse<IShipmentItem[]>) => res.body ?? []))
      .pipe(
        map((shipmentItems: IShipmentItem[]) =>
          this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, this.shipmentReceipt?.shipmentItem),
        ),
      )
      .subscribe((shipmentItems: IShipmentItem[]) => this.shipmentItemsSharedCollection.set(shipmentItems));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.shipmentReceipt?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.shipmentReceipt?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(
            inventoryItems,
            this.shipmentReceipt?.inventoryItem,
          ),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.shipmentReceipt?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.shipmentReceipt?.receivedBy)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
