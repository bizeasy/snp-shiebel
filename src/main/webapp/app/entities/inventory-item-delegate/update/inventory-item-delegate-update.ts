import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from 'app/entities/shipment-item/service/shipment-item.service';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { ItemIssuanceService } from 'app/entities/item-issuance/service/item-issuance.service';
import { ShipmentReceiptService } from 'app/entities/shipment-receipt/service/shipment-receipt.service';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItemDelegate } from '../inventory-item-delegate.model';
import { InventoryItemDelegateService } from '../service/inventory-item-delegate.service';

import { InventoryItemDelegateFormGroup, InventoryItemDelegateFormService } from './inventory-item-delegate-form.service';

@Component({
  selector: 'sys-inventory-item-delegate-update',
  templateUrl: './inventory-item-delegate-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InventoryItemDelegateUpdate implements OnInit {
  readonly isSaving = signal(false);
  inventoryItemDelegate: IInventoryItemDelegate | null = null;

  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  invoicesSharedCollection = signal<IInvoice[]>([]);
  invoiceItemsSharedCollection = signal<IInvoiceItem[]>([]);
  shipmentsSharedCollection = signal<IShipment[]>([]);
  shipmentItemsSharedCollection = signal<IShipmentItem[]>([]);
  itemIssuancesSharedCollection = signal<IItemIssuance[]>([]);
  shipmentReceiptsSharedCollection = signal<IShipmentReceipt[]>([]);

  protected inventoryItemDelegateService = inject(InventoryItemDelegateService);
  protected inventoryItemDelegateFormService = inject(InventoryItemDelegateFormService);
  protected inventoryItemService = inject(InventoryItemService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected invoiceService = inject(InvoiceService);
  protected invoiceItemService = inject(InvoiceItemService);
  protected shipmentService = inject(ShipmentService);
  protected shipmentItemService = inject(ShipmentItemService);
  protected itemIssuanceService = inject(ItemIssuanceService);
  protected shipmentReceiptService = inject(ShipmentReceiptService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryItemDelegateFormGroup = this.inventoryItemDelegateFormService.createInventoryItemDelegateFormGroup();

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareShipmentItem = (o1: IShipmentItem | null, o2: IShipmentItem | null): boolean =>
    this.shipmentItemService.compareShipmentItem(o1, o2);

  compareItemIssuance = (o1: IItemIssuance | null, o2: IItemIssuance | null): boolean =>
    this.itemIssuanceService.compareItemIssuance(o1, o2);

  compareShipmentReceipt = (o1: IShipmentReceipt | null, o2: IShipmentReceipt | null): boolean =>
    this.shipmentReceiptService.compareShipmentReceipt(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItemDelegate }) => {
      this.inventoryItemDelegate = inventoryItemDelegate;
      if (inventoryItemDelegate) {
        this.updateForm(inventoryItemDelegate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const inventoryItemDelegate = this.inventoryItemDelegateFormService.getInventoryItemDelegate(this.editForm);
    if (inventoryItemDelegate.id === null) {
      this.subscribeToSaveResponse(this.inventoryItemDelegateService.create(inventoryItemDelegate));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemDelegateService.update(inventoryItemDelegate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInventoryItemDelegate | null>): void {
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

  protected updateForm(inventoryItemDelegate: IInventoryItemDelegate): void {
    this.inventoryItemDelegate = inventoryItemDelegate;
    this.inventoryItemDelegateFormService.resetForm(this.editForm, inventoryItemDelegate);

    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, inventoryItemDelegate.inventoryItem),
    );
    this.ordersSharedCollection.update(orders =>
      this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, inventoryItemDelegate.order),
    );
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, inventoryItemDelegate.orderItem),
    );
    this.invoicesSharedCollection.update(invoices =>
      this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, inventoryItemDelegate.invoice),
    );
    this.invoiceItemsSharedCollection.update(invoiceItems =>
      this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, inventoryItemDelegate.invoiceItem),
    );
    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, inventoryItemDelegate.shipment),
    );
    this.shipmentItemsSharedCollection.update(shipmentItems =>
      this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, inventoryItemDelegate.shipmentItem),
    );
    this.itemIssuancesSharedCollection.update(itemIssuances =>
      this.itemIssuanceService.addItemIssuanceToCollectionIfMissing<IItemIssuance>(itemIssuances, inventoryItemDelegate.itemIssuance),
    );
    this.shipmentReceiptsSharedCollection.update(shipmentReceipts =>
      this.shipmentReceiptService.addShipmentReceiptToCollectionIfMissing<IShipmentReceipt>(
        shipmentReceipts,
        inventoryItemDelegate.receipt,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(
            inventoryItems,
            this.inventoryItemDelegate?.inventoryItem,
          ),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.inventoryItemDelegate?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.inventoryItemDelegate?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) =>
          this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.inventoryItemDelegate?.invoice),
        ),
      )
      .subscribe((invoices: IInvoice[]) => this.invoicesSharedCollection.set(invoices));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, this.inventoryItemDelegate?.invoiceItem),
        ),
      )
      .subscribe((invoiceItems: IInvoiceItem[]) => this.invoiceItemsSharedCollection.set(invoiceItems));

    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.inventoryItemDelegate?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.shipmentItemService
      .query()
      .pipe(map((res: HttpResponse<IShipmentItem[]>) => res.body ?? []))
      .pipe(
        map((shipmentItems: IShipmentItem[]) =>
          this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(
            shipmentItems,
            this.inventoryItemDelegate?.shipmentItem,
          ),
        ),
      )
      .subscribe((shipmentItems: IShipmentItem[]) => this.shipmentItemsSharedCollection.set(shipmentItems));

    this.itemIssuanceService
      .query()
      .pipe(map((res: HttpResponse<IItemIssuance[]>) => res.body ?? []))
      .pipe(
        map((itemIssuances: IItemIssuance[]) =>
          this.itemIssuanceService.addItemIssuanceToCollectionIfMissing<IItemIssuance>(
            itemIssuances,
            this.inventoryItemDelegate?.itemIssuance,
          ),
        ),
      )
      .subscribe((itemIssuances: IItemIssuance[]) => this.itemIssuancesSharedCollection.set(itemIssuances));

    this.shipmentReceiptService
      .query()
      .pipe(map((res: HttpResponse<IShipmentReceipt[]>) => res.body ?? []))
      .pipe(
        map((shipmentReceipts: IShipmentReceipt[]) =>
          this.shipmentReceiptService.addShipmentReceiptToCollectionIfMissing<IShipmentReceipt>(
            shipmentReceipts,
            this.inventoryItemDelegate?.receipt,
          ),
        ),
      )
      .subscribe((shipmentReceipts: IShipmentReceipt[]) => this.shipmentReceiptsSharedCollection.set(shipmentReceipts));
  }
}
