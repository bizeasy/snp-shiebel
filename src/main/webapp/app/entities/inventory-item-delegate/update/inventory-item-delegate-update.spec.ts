import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { ItemIssuanceService } from 'app/entities/item-issuance/service/item-issuance.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from 'app/entities/shipment-item/service/shipment-item.service';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { ShipmentReceiptService } from 'app/entities/shipment-receipt/service/shipment-receipt.service';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';
import { IInventoryItemDelegate } from '../inventory-item-delegate.model';
import { InventoryItemDelegateService } from '../service/inventory-item-delegate.service';

import { InventoryItemDelegateFormService } from './inventory-item-delegate-form.service';
import { InventoryItemDelegateUpdate } from './inventory-item-delegate-update';

describe('InventoryItemDelegate Management Update Component', () => {
  let comp: InventoryItemDelegateUpdate;
  let fixture: ComponentFixture<InventoryItemDelegateUpdate>;
  let activatedRoute: ActivatedRoute;
  let inventoryItemDelegateFormService: InventoryItemDelegateFormService;
  let inventoryItemDelegateService: InventoryItemDelegateService;
  let inventoryItemService: InventoryItemService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;
  let invoiceService: InvoiceService;
  let invoiceItemService: InvoiceItemService;
  let shipmentService: ShipmentService;
  let shipmentItemService: ShipmentItemService;
  let itemIssuanceService: ItemIssuanceService;
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

    fixture = TestBed.createComponent(InventoryItemDelegateUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryItemDelegateFormService = TestBed.inject(InventoryItemDelegateFormService);
    inventoryItemDelegateService = TestBed.inject(InventoryItemDelegateService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);
    invoiceService = TestBed.inject(InvoiceService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    shipmentService = TestBed.inject(ShipmentService);
    shipmentItemService = TestBed.inject(ShipmentItemService);
    itemIssuanceService = TestBed.inject(ItemIssuanceService);
    shipmentReceiptService = TestBed.inject(ShipmentReceiptService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InventoryItem query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      inventoryItemDelegate.inventoryItem = inventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [inventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const order: IOrder = { id: 14644 };
      inventoryItemDelegate.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const orderItem: IOrderItem = { id: 25971 };
      inventoryItemDelegate.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Invoice query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const invoice: IInvoice = { id: 1362 };
      inventoryItemDelegate.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 1362 }];
      vi.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      vi.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call InvoiceItem query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const invoiceItem: IInvoiceItem = { id: 29231 };
      inventoryItemDelegate.invoiceItem = invoiceItem;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: 29231 }];
      vi.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [invoiceItem];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      vi.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.invoiceItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Shipment query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const shipment: IShipment = { id: 9594 };
      inventoryItemDelegate.shipment = shipment;

      const shipmentCollection: IShipment[] = [{ id: 9594 }];
      vi.spyOn(shipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentCollection })));
      const additionalShipments = [shipment];
      const expectedCollection: IShipment[] = [...additionalShipments, ...shipmentCollection];
      vi.spyOn(shipmentService, 'addShipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(shipmentService.query).toHaveBeenCalled();
      expect(shipmentService.addShipmentToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentCollection,
        ...additionalShipments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentItem query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const shipmentItem: IShipmentItem = { id: 14901 };
      inventoryItemDelegate.shipmentItem = shipmentItem;

      const shipmentItemCollection: IShipmentItem[] = [{ id: 14901 }];
      vi.spyOn(shipmentItemService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentItemCollection })));
      const additionalShipmentItems = [shipmentItem];
      const expectedCollection: IShipmentItem[] = [...additionalShipmentItems, ...shipmentItemCollection];
      vi.spyOn(shipmentItemService, 'addShipmentItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(shipmentItemService.query).toHaveBeenCalled();
      expect(shipmentItemService.addShipmentItemToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentItemCollection,
        ...additionalShipmentItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ItemIssuance query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const itemIssuance: IItemIssuance = { id: 24352 };
      inventoryItemDelegate.itemIssuance = itemIssuance;

      const itemIssuanceCollection: IItemIssuance[] = [{ id: 24352 }];
      vi.spyOn(itemIssuanceService, 'query').mockReturnValue(of(new HttpResponse({ body: itemIssuanceCollection })));
      const additionalItemIssuances = [itemIssuance];
      const expectedCollection: IItemIssuance[] = [...additionalItemIssuances, ...itemIssuanceCollection];
      vi.spyOn(itemIssuanceService, 'addItemIssuanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(itemIssuanceService.query).toHaveBeenCalled();
      expect(itemIssuanceService.addItemIssuanceToCollectionIfMissing).toHaveBeenCalledWith(
        itemIssuanceCollection,
        ...additionalItemIssuances.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.itemIssuancesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentReceipt query and add missing value', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const receipt: IShipmentReceipt = { id: 7355 };
      inventoryItemDelegate.receipt = receipt;

      const shipmentReceiptCollection: IShipmentReceipt[] = [{ id: 7355 }];
      vi.spyOn(shipmentReceiptService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentReceiptCollection })));
      const additionalShipmentReceipts = [receipt];
      const expectedCollection: IShipmentReceipt[] = [...additionalShipmentReceipts, ...shipmentReceiptCollection];
      vi.spyOn(shipmentReceiptService, 'addShipmentReceiptToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(shipmentReceiptService.query).toHaveBeenCalled();
      expect(shipmentReceiptService.addShipmentReceiptToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentReceiptCollection,
        ...additionalShipmentReceipts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentReceiptsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inventoryItemDelegate: IInventoryItemDelegate = { id: 1018 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      inventoryItemDelegate.inventoryItem = inventoryItem;
      const order: IOrder = { id: 14644 };
      inventoryItemDelegate.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      inventoryItemDelegate.orderItem = orderItem;
      const invoice: IInvoice = { id: 1362 };
      inventoryItemDelegate.invoice = invoice;
      const invoiceItem: IInvoiceItem = { id: 29231 };
      inventoryItemDelegate.invoiceItem = invoiceItem;
      const shipment: IShipment = { id: 9594 };
      inventoryItemDelegate.shipment = shipment;
      const shipmentItem: IShipmentItem = { id: 14901 };
      inventoryItemDelegate.shipmentItem = shipmentItem;
      const itemIssuance: IItemIssuance = { id: 24352 };
      inventoryItemDelegate.itemIssuance = itemIssuance;
      const receipt: IShipmentReceipt = { id: 7355 };
      inventoryItemDelegate.receipt = receipt;

      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      expect(comp.inventoryItemsSharedCollection()).toContainEqual(inventoryItem);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.invoicesSharedCollection()).toContainEqual(invoice);
      expect(comp.invoiceItemsSharedCollection()).toContainEqual(invoiceItem);
      expect(comp.shipmentsSharedCollection()).toContainEqual(shipment);
      expect(comp.shipmentItemsSharedCollection()).toContainEqual(shipmentItem);
      expect(comp.itemIssuancesSharedCollection()).toContainEqual(itemIssuance);
      expect(comp.shipmentReceiptsSharedCollection()).toContainEqual(receipt);
      expect(comp.inventoryItemDelegate).toEqual(inventoryItemDelegate);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemDelegate>();
      const inventoryItemDelegate = { id: 14485 };
      vi.spyOn(inventoryItemDelegateFormService, 'getInventoryItemDelegate').mockReturnValue(inventoryItemDelegate);
      vi.spyOn(inventoryItemDelegateService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItemDelegate);
      saveSubject.complete();

      // THEN
      expect(inventoryItemDelegateFormService.getInventoryItemDelegate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryItemDelegateService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryItemDelegate));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemDelegate>();
      const inventoryItemDelegate = { id: 14485 };
      vi.spyOn(inventoryItemDelegateFormService, 'getInventoryItemDelegate').mockReturnValue({ id: null });
      vi.spyOn(inventoryItemDelegateService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemDelegate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventoryItemDelegate);
      saveSubject.complete();

      // THEN
      expect(inventoryItemDelegateFormService.getInventoryItemDelegate).toHaveBeenCalled();
      expect(inventoryItemDelegateService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInventoryItemDelegate>();
      const inventoryItemDelegate = { id: 14485 };
      vi.spyOn(inventoryItemDelegateService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryItemDelegate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryItemDelegateService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInventoryItem', () => {
      it('should forward to inventoryItemService', () => {
        const entity = { id: 7462 };
        const entity2 = { id: 4332 };
        vi.spyOn(inventoryItemService, 'compareInventoryItem');
        comp.compareInventoryItem(entity, entity2);
        expect(inventoryItemService.compareInventoryItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrder', () => {
      it('should forward to orderService', () => {
        const entity = { id: 14644 };
        const entity2 = { id: 4253 };
        vi.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareInvoice', () => {
      it('should forward to invoiceService', () => {
        const entity = { id: 1362 };
        const entity2 = { id: 1801 };
        vi.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareShipment', () => {
      it('should forward to shipmentService', () => {
        const entity = { id: 9594 };
        const entity2 = { id: 9190 };
        vi.spyOn(shipmentService, 'compareShipment');
        comp.compareShipment(entity, entity2);
        expect(shipmentService.compareShipment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareShipmentItem', () => {
      it('should forward to shipmentItemService', () => {
        const entity = { id: 14901 };
        const entity2 = { id: 27511 };
        vi.spyOn(shipmentItemService, 'compareShipmentItem');
        comp.compareShipmentItem(entity, entity2);
        expect(shipmentItemService.compareShipmentItem).toHaveBeenCalledWith(entity, entity2);
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
