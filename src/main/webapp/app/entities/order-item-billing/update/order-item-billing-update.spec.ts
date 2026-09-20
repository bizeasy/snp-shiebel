import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { ItemIssuanceService } from 'app/entities/item-issuance/service/item-issuance.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ShipmentReceiptService } from 'app/entities/shipment-receipt/service/shipment-receipt.service';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';
import { IOrderItemBilling } from '../order-item-billing.model';
import { OrderItemBillingService } from '../service/order-item-billing.service';

import { OrderItemBillingFormService } from './order-item-billing-form.service';
import { OrderItemBillingUpdate } from './order-item-billing-update';

describe('OrderItemBilling Management Update Component', () => {
  let comp: OrderItemBillingUpdate;
  let fixture: ComponentFixture<OrderItemBillingUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderItemBillingFormService: OrderItemBillingFormService;
  let orderItemBillingService: OrderItemBillingService;
  let orderItemService: OrderItemService;
  let invoiceItemService: InvoiceItemService;
  let shipmentReceiptService: ShipmentReceiptService;
  let itemIssuanceService: ItemIssuanceService;

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

    fixture = TestBed.createComponent(OrderItemBillingUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemBillingFormService = TestBed.inject(OrderItemBillingFormService);
    orderItemBillingService = TestBed.inject(OrderItemBillingService);
    orderItemService = TestBed.inject(OrderItemService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    shipmentReceiptService = TestBed.inject(ShipmentReceiptService);
    itemIssuanceService = TestBed.inject(ItemIssuanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OrderItem query and add missing value', () => {
      const orderItemBilling: IOrderItemBilling = { id: 16501 };
      const orderItem: IOrderItem = { id: 25971 };
      orderItemBilling.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItem query and add missing value', () => {
      const orderItemBilling: IOrderItemBilling = { id: 16501 };
      const invoiceItem: IInvoiceItem = { id: 29231 };
      orderItemBilling.invoiceItem = invoiceItem;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: 29231 }];
      vi.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [invoiceItem];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      vi.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentReceipt query and add missing value', () => {
      const orderItemBilling: IOrderItemBilling = { id: 16501 };
      const shipmentReceipt: IShipmentReceipt = { id: 7355 };
      orderItemBilling.shipmentReceipt = shipmentReceipt;

      const shipmentReceiptCollection: IShipmentReceipt[] = [{ id: 7355 }];
      vi.spyOn(shipmentReceiptService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentReceiptCollection })));
      const additionalShipmentReceipts = [shipmentReceipt];
      const expectedCollection: IShipmentReceipt[] = [...additionalShipmentReceipts, ...shipmentReceiptCollection];
      vi.spyOn(shipmentReceiptService, 'addShipmentReceiptToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      expect(shipmentReceiptService.query).toHaveBeenCalled();
      expect(shipmentReceiptService.addShipmentReceiptToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentReceiptCollection,
        ...additionalShipmentReceipts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentReceiptsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ItemIssuance query and add missing value', () => {
      const orderItemBilling: IOrderItemBilling = { id: 16501 };
      const itemIssuance: IItemIssuance = { id: 24352 };
      orderItemBilling.itemIssuance = itemIssuance;

      const itemIssuanceCollection: IItemIssuance[] = [{ id: 24352 }];
      vi.spyOn(itemIssuanceService, 'query').mockReturnValue(of(new HttpResponse({ body: itemIssuanceCollection })));
      const additionalItemIssuances = [itemIssuance];
      const expectedCollection: IItemIssuance[] = [...additionalItemIssuances, ...itemIssuanceCollection];
      vi.spyOn(itemIssuanceService, 'addItemIssuanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      expect(itemIssuanceService.query).toHaveBeenCalled();
      expect(itemIssuanceService.addItemIssuanceToCollectionIfMissing).toHaveBeenCalledWith(
        itemIssuanceCollection,
        ...additionalItemIssuances.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.itemIssuancesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderItemBilling: IOrderItemBilling = { id: 16501 };
      const orderItem: IOrderItem = { id: 25971 };
      orderItemBilling.orderItem = orderItem;
      const invoiceItem: IInvoiceItem = { id: 29231 };
      orderItemBilling.invoiceItem = invoiceItem;
      const shipmentReceipt: IShipmentReceipt = { id: 7355 };
      orderItemBilling.shipmentReceipt = shipmentReceipt;
      const itemIssuance: IItemIssuance = { id: 24352 };
      orderItemBilling.itemIssuance = itemIssuance;

      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.invoiceItemsSharedCollection()).toContainEqual(invoiceItem);
      expect(comp.shipmentReceiptsSharedCollection()).toContainEqual(shipmentReceipt);
      expect(comp.itemIssuancesSharedCollection()).toContainEqual(itemIssuance);
      expect(comp.orderItemBilling).toEqual(orderItemBilling);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemBilling>();
      const orderItemBilling = { id: 19790 };
      vi.spyOn(orderItemBillingFormService, 'getOrderItemBilling').mockReturnValue(orderItemBilling);
      vi.spyOn(orderItemBillingService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItemBilling);
      saveSubject.complete();

      // THEN
      expect(orderItemBillingFormService.getOrderItemBilling).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemBillingService.update).toHaveBeenCalledWith(expect.objectContaining(orderItemBilling));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemBilling>();
      const orderItemBilling = { id: 19790 };
      vi.spyOn(orderItemBillingFormService, 'getOrderItemBilling').mockReturnValue({ id: null });
      vi.spyOn(orderItemBillingService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemBilling: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItemBilling);
      saveSubject.complete();

      // THEN
      expect(orderItemBillingFormService.getOrderItemBilling).toHaveBeenCalled();
      expect(orderItemBillingService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemBilling>();
      const orderItemBilling = { id: 19790 };
      vi.spyOn(orderItemBillingService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemBilling });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemBillingService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

    describe('compareItemIssuance', () => {
      it('should forward to itemIssuanceService', () => {
        const entity = { id: 24352 };
        const entity2 = { id: 8405 };
        vi.spyOn(itemIssuanceService, 'compareItemIssuance');
        comp.compareItemIssuance(entity, entity2);
        expect(itemIssuanceService.compareItemIssuance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
