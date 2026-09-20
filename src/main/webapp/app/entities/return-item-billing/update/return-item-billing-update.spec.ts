import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IReturnItem } from 'app/entities/return-item/return-item.model';
import { ReturnItemService } from 'app/entities/return-item/service/return-item.service';
import { ShipmentReceiptService } from 'app/entities/shipment-receipt/service/shipment-receipt.service';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';
import { IReturnItemBilling } from '../return-item-billing.model';
import { ReturnItemBillingService } from '../service/return-item-billing.service';

import { ReturnItemBillingFormService } from './return-item-billing-form.service';
import { ReturnItemBillingUpdate } from './return-item-billing-update';

describe('ReturnItemBilling Management Update Component', () => {
  let comp: ReturnItemBillingUpdate;
  let fixture: ComponentFixture<ReturnItemBillingUpdate>;
  let activatedRoute: ActivatedRoute;
  let returnItemBillingFormService: ReturnItemBillingFormService;
  let returnItemBillingService: ReturnItemBillingService;
  let returnItemService: ReturnItemService;
  let orderItemService: OrderItemService;
  let invoiceItemService: InvoiceItemService;
  let shipmentReceiptService: ShipmentReceiptService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ReturnItemBillingUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    returnItemBillingFormService = TestBed.inject(ReturnItemBillingFormService);
    returnItemBillingService = TestBed.inject(ReturnItemBillingService);
    returnItemService = TestBed.inject(ReturnItemService);
    orderItemService = TestBed.inject(OrderItemService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    shipmentReceiptService = TestBed.inject(ShipmentReceiptService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ReturnItem query and add missing value', () => {
      const returnItemBilling: IReturnItemBilling = { id: 3543 };
      const returnItem: IReturnItem = { id: 28210 };
      returnItemBilling.returnItem = returnItem;

      const returnItemCollection: IReturnItem[] = [{ id: 28210 }];
      vi.spyOn(returnItemService, 'query').mockReturnValue(of(new HttpResponse({ body: returnItemCollection })));
      const additionalReturnItems = [returnItem];
      const expectedCollection: IReturnItem[] = [...additionalReturnItems, ...returnItemCollection];
      vi.spyOn(returnItemService, 'addReturnItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      expect(returnItemService.query).toHaveBeenCalled();
      expect(returnItemService.addReturnItemToCollectionIfMissing).toHaveBeenCalledWith(
        returnItemCollection,
        ...additionalReturnItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.returnItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const returnItemBilling: IReturnItemBilling = { id: 3543 };
      const orderItem: IOrderItem = { id: 25971 };
      returnItemBilling.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItem query and add missing value', () => {
      const returnItemBilling: IReturnItemBilling = { id: 3543 };
      const invoiceItem: IInvoiceItem = { id: 29231 };
      returnItemBilling.invoiceItem = invoiceItem;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: 29231 }];
      vi.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [invoiceItem];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      vi.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentReceipt query and add missing value', () => {
      const returnItemBilling: IReturnItemBilling = { id: 3543 };
      const shipmentReceipt: IShipmentReceipt = { id: 7355 };
      returnItemBilling.shipmentReceipt = shipmentReceipt;

      const shipmentReceiptCollection: IShipmentReceipt[] = [{ id: 7355 }];
      vi.spyOn(shipmentReceiptService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentReceiptCollection })));
      const additionalShipmentReceipts = [shipmentReceipt];
      const expectedCollection: IShipmentReceipt[] = [...additionalShipmentReceipts, ...shipmentReceiptCollection];
      vi.spyOn(shipmentReceiptService, 'addShipmentReceiptToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      expect(shipmentReceiptService.query).toHaveBeenCalled();
      expect(shipmentReceiptService.addShipmentReceiptToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentReceiptCollection,
        ...additionalShipmentReceipts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentReceiptsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const returnItemBilling: IReturnItemBilling = { id: 3543 };
      const returnItem: IReturnItem = { id: 28210 };
      returnItemBilling.returnItem = returnItem;
      const orderItem: IOrderItem = { id: 25971 };
      returnItemBilling.orderItem = orderItem;
      const invoiceItem: IInvoiceItem = { id: 29231 };
      returnItemBilling.invoiceItem = invoiceItem;
      const shipmentReceipt: IShipmentReceipt = { id: 7355 };
      returnItemBilling.shipmentReceipt = shipmentReceipt;

      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      expect(comp.returnItemsSharedCollection()).toContainEqual(returnItem);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.invoiceItemsSharedCollection()).toContainEqual(invoiceItem);
      expect(comp.shipmentReceiptsSharedCollection()).toContainEqual(shipmentReceipt);
      expect(comp.returnItemBilling).toEqual(returnItemBilling);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnItemBilling>();
      const returnItemBilling = { id: 25080 };
      vi.spyOn(returnItemBillingFormService, 'getReturnItemBilling').mockReturnValue(returnItemBilling);
      vi.spyOn(returnItemBillingService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnItemBilling);
      saveSubject.complete();

      // THEN
      expect(returnItemBillingFormService.getReturnItemBilling).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(returnItemBillingService.update).toHaveBeenCalledWith(expect.objectContaining(returnItemBilling));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnItemBilling>();
      const returnItemBilling = { id: 25080 };
      vi.spyOn(returnItemBillingFormService, 'getReturnItemBilling').mockReturnValue({ id: null });
      vi.spyOn(returnItemBillingService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnItemBilling: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnItemBilling);
      saveSubject.complete();

      // THEN
      expect(returnItemBillingFormService.getReturnItemBilling).toHaveBeenCalled();
      expect(returnItemBillingService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnItemBilling>();
      const returnItemBilling = { id: 25080 };
      vi.spyOn(returnItemBillingService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnItemBilling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(returnItemBillingService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReturnItem', () => {
      it('should forward to returnItemService', () => {
        const entity = { id: 28210 };
        const entity2 = { id: 12707 };
        vi.spyOn(returnItemService, 'compareReturnItem');
        comp.compareReturnItem(entity, entity2);
        expect(returnItemService.compareReturnItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrderItem', () => {
      it('should forward to orderItemService', () => {
        const entity = { id: 25971 };
        const entity2 = { id: 123 };
        vi.spyOn(orderItemService, 'compareOrderItem');
        comp.compareOrderItem(entity, entity2);
        expect(orderItemService.compareOrderItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoiceItem', () => {
      it('should forward to invoiceItemService', () => {
        const entity = { id: 29231 };
        const entity2 = { id: 28999 };
        vi.spyOn(invoiceItemService, 'compareInvoiceItem');
        comp.compareInvoiceItem(entity, entity2);
        expect(invoiceItemService.compareInvoiceItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareShipmentReceipt', () => {
      it('should forward to shipmentReceiptService', () => {
        const entity = { id: 7355 };
        const entity2 = { id: 18394 };
        vi.spyOn(shipmentReceiptService, 'compareShipmentReceipt');
        comp.compareShipmentReceipt(entity, entity2);
        expect(shipmentReceiptService.compareShipmentReceipt).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
