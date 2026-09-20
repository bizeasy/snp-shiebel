import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IReturnItem } from 'app/entities/return-item/return-item.model';
import { ReturnItemService } from 'app/entities/return-item/service/return-item.service';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IReturnItemBilling } from '../return-item-billing.model';
import { ReturnItemBillingService } from '../service/return-item-billing.service';

import { ReturnItemBillingFormGroup, ReturnItemBillingFormService } from './return-item-billing-form.service';
import { ShipmentReceiptService } from 'app/entities/shipment-receipt/service/shipment-receipt.service';

@Component({
  selector: 'sys-return-item-billing-update',
  templateUrl: './return-item-billing-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ReturnItemBillingUpdate implements OnInit {
  readonly isSaving = signal(false);
  returnItemBilling: IReturnItemBilling | null = null;

  returnItemsSharedCollection = signal<IReturnItem[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  invoiceItemsSharedCollection = signal<IInvoiceItem[]>([]);
  shipmentReceiptsSharedCollection = signal<IShipmentReceipt[]>([]);

  protected returnItemBillingService = inject(ReturnItemBillingService);
  protected returnItemBillingFormService = inject(ReturnItemBillingFormService);
  protected returnItemService = inject(ReturnItemService);
  protected orderItemService = inject(OrderItemService);
  protected invoiceItemService = inject(InvoiceItemService);
  protected shipmentReceiptService = inject(ShipmentReceiptService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReturnItemBillingFormGroup = this.returnItemBillingFormService.createReturnItemBillingFormGroup();

  compareReturnItem = (o1: IReturnItem | null, o2: IReturnItem | null): boolean => this.returnItemService.compareReturnItem(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  compareShipmentReceipt = (o1: IShipmentReceipt | null, o2: IShipmentReceipt | null): boolean =>
    this.shipmentReceiptService.compareShipmentReceipt(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnItemBilling }) => {
      this.returnItemBilling = returnItemBilling;
      if (returnItemBilling) {
        this.updateForm(returnItemBilling);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const returnItemBilling = this.returnItemBillingFormService.getReturnItemBilling(this.editForm);
    if (returnItemBilling.id === null) {
      this.subscribeToSaveResponse(this.returnItemBillingService.create(returnItemBilling));
    } else {
      this.subscribeToSaveResponse(this.returnItemBillingService.update(returnItemBilling));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IReturnItemBilling | null>): void {
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

  protected updateForm(returnItemBilling: IReturnItemBilling): void {
    this.returnItemBilling = returnItemBilling;
    this.returnItemBillingFormService.resetForm(this.editForm, returnItemBilling);

    this.returnItemsSharedCollection.update(returnItems =>
      this.returnItemService.addReturnItemToCollectionIfMissing<IReturnItem>(returnItems, returnItemBilling.returnItem),
    );
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, returnItemBilling.orderItem),
    );
    this.invoiceItemsSharedCollection.update(invoiceItems =>
      this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, returnItemBilling.invoiceItem),
    );
    this.shipmentReceiptsSharedCollection.update(shipmentReceipts =>
      this.shipmentReceiptService.addShipmentReceiptToCollectionIfMissing<IShipmentReceipt>(
        shipmentReceipts,
        returnItemBilling.shipmentReceipt,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.returnItemService
      .query()
      .pipe(map((res: HttpResponse<IReturnItem[]>) => res.body ?? []))
      .pipe(
        map((returnItems: IReturnItem[]) =>
          this.returnItemService.addReturnItemToCollectionIfMissing<IReturnItem>(returnItems, this.returnItemBilling?.returnItem),
        ),
      )
      .subscribe((returnItems: IReturnItem[]) => this.returnItemsSharedCollection.set(returnItems));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.returnItemBilling?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, this.returnItemBilling?.invoiceItem),
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
            this.returnItemBilling?.shipmentReceipt,
          ),
        ),
      )
      .subscribe((shipmentReceipts: IShipmentReceipt[]) => this.shipmentReceiptsSharedCollection.set(shipmentReceipts));
  }
}
