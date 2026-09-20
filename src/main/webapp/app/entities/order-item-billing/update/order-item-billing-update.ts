import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { ItemIssuanceService } from 'app/entities/item-issuance/service/item-issuance.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ShipmentReceiptService } from 'app/entities/shipment-receipt/service/shipment-receipt.service';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItemBilling } from '../order-item-billing.model';
import { OrderItemBillingService } from '../service/order-item-billing.service';

import { OrderItemBillingFormGroup, OrderItemBillingFormService } from './order-item-billing-form.service';

@Component({
  selector: 'sys-order-item-billing-update',
  templateUrl: './order-item-billing-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderItemBillingUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderItemBilling: IOrderItemBilling | null = null;

  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  invoiceItemsSharedCollection = signal<IInvoiceItem[]>([]);
  shipmentReceiptsSharedCollection = signal<IShipmentReceipt[]>([]);
  itemIssuancesSharedCollection = signal<IItemIssuance[]>([]);

  protected orderItemBillingService = inject(OrderItemBillingService);
  protected orderItemBillingFormService = inject(OrderItemBillingFormService);
  protected orderItemService = inject(OrderItemService);
  protected invoiceItemService = inject(InvoiceItemService);
  protected shipmentReceiptService = inject(ShipmentReceiptService);
  protected itemIssuanceService = inject(ItemIssuanceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderItemBillingFormGroup = this.orderItemBillingFormService.createOrderItemBillingFormGroup();

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  compareShipmentReceipt = (o1: IShipmentReceipt | null, o2: IShipmentReceipt | null): boolean =>
    this.shipmentReceiptService.compareShipmentReceipt(o1, o2);

  compareItemIssuance = (o1: IItemIssuance | null, o2: IItemIssuance | null): boolean =>
    this.itemIssuanceService.compareItemIssuance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemBilling }) => {
      this.orderItemBilling = orderItemBilling;
      if (orderItemBilling) {
        this.updateForm(orderItemBilling);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderItemBilling = this.orderItemBillingFormService.getOrderItemBilling(this.editForm);
    if (orderItemBilling.id === null) {
      this.subscribeToSaveResponse(this.orderItemBillingService.create(orderItemBilling));
    } else {
      this.subscribeToSaveResponse(this.orderItemBillingService.update(orderItemBilling));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderItemBilling | null>): void {
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

  protected updateForm(orderItemBilling: IOrderItemBilling): void {
    this.orderItemBilling = orderItemBilling;
    this.orderItemBillingFormService.resetForm(this.editForm, orderItemBilling);

    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, orderItemBilling.orderItem),
    );
    this.invoiceItemsSharedCollection.update(invoiceItems =>
      this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, orderItemBilling.invoiceItem),
    );
    this.shipmentReceiptsSharedCollection.update(shipmentReceipts =>
      this.shipmentReceiptService.addShipmentReceiptToCollectionIfMissing<IShipmentReceipt>(
        shipmentReceipts,
        orderItemBilling.shipmentReceipt,
      ),
    );
    this.itemIssuancesSharedCollection.update(itemIssuances =>
      this.itemIssuanceService.addItemIssuanceToCollectionIfMissing<IItemIssuance>(itemIssuances, orderItemBilling.itemIssuance),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.orderItemBilling?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, this.orderItemBilling?.invoiceItem),
        ),
      )
      .subscribe((invoiceItems: IInvoiceItem[]) => this.invoiceItemsSharedCollection.set(invoiceItems));

    this.shipmentReceiptService
      .query()
      .pipe(map((res: HttpResponse<IShipmentReceipt[]>) => res.body ?? []))
      .pipe(
        map((shipmentReceipts: IShipmentReceipt[]) =>
          this.shipmentReceiptService.addShipmentReceiptToCollectionIfMissing<IShipmentReceipt>(
            shipmentReceipts,
            this.orderItemBilling?.shipmentReceipt,
          ),
        ),
      )
      .subscribe((shipmentReceipts: IShipmentReceipt[]) => this.shipmentReceiptsSharedCollection.set(shipmentReceipts));

    this.itemIssuanceService
      .query()
      .pipe(map((res: HttpResponse<IItemIssuance[]>) => res.body ?? []))
      .pipe(
        map((itemIssuances: IItemIssuance[]) =>
          this.itemIssuanceService.addItemIssuanceToCollectionIfMissing<IItemIssuance>(itemIssuances, this.orderItemBilling?.itemIssuance),
        ),
      )
      .subscribe((itemIssuances: IItemIssuance[]) => this.itemIssuancesSharedCollection.set(itemIssuances));
  }
}
