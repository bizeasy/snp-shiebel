import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

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
import { IItemIssuance } from '../item-issuance.model';
import { ItemIssuanceService } from '../service/item-issuance.service';

import { ItemIssuanceFormService } from './item-issuance-form.service';
import { ItemIssuanceUpdate } from './item-issuance-update';

describe('ItemIssuance Management Update Component', () => {
  let comp: ItemIssuanceUpdate;
  let fixture: ComponentFixture<ItemIssuanceUpdate>;
  let activatedRoute: ActivatedRoute;
  let itemIssuanceFormService: ItemIssuanceFormService;
  let itemIssuanceService: ItemIssuanceService;
  let inventoryItemService: InventoryItemService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;
  let shipmentService: ShipmentService;
  let shipmentItemService: ShipmentItemService;
  let partyService: PartyService;

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

    fixture = TestBed.createComponent(ItemIssuanceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemIssuanceFormService = TestBed.inject(ItemIssuanceFormService);
    itemIssuanceService = TestBed.inject(ItemIssuanceService);
    inventoryItemService = TestBed.inject(InventoryItemService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);
    shipmentService = TestBed.inject(ShipmentService);
    shipmentItemService = TestBed.inject(ShipmentItemService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call InventoryItem query and add missing value', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      itemIssuance.inventoryItem = inventoryItem;

      const inventoryItemCollection: IInventoryItem[] = [{ id: 7462 }];
      vi.spyOn(inventoryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryItemCollection })));
      const additionalInventoryItems = [inventoryItem];
      const expectedCollection: IInventoryItem[] = [...additionalInventoryItems, ...inventoryItemCollection];
      vi.spyOn(inventoryItemService, 'addInventoryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(inventoryItemService.query).toHaveBeenCalled();
      expect(inventoryItemService.addInventoryItemToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryItemCollection,
        ...additionalInventoryItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.inventoryItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Order query and add missing value', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const order: IOrder = { id: 14644 };
      itemIssuance.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const orderItem: IOrderItem = { id: 25971 };
      itemIssuance.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Shipment query and add missing value', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const shipment: IShipment = { id: 9594 };
      itemIssuance.shipment = shipment;

      const shipmentCollection: IShipment[] = [{ id: 9594 }];
      vi.spyOn(shipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentCollection })));
      const additionalShipments = [shipment];
      const expectedCollection: IShipment[] = [...additionalShipments, ...shipmentCollection];
      vi.spyOn(shipmentService, 'addShipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(shipmentService.query).toHaveBeenCalled();
      expect(shipmentService.addShipmentToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentCollection,
        ...additionalShipments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentItem query and add missing value', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const shipmentItem: IShipmentItem = { id: 14901 };
      itemIssuance.shipmentItem = shipmentItem;

      const shipmentItemCollection: IShipmentItem[] = [{ id: 14901 }];
      vi.spyOn(shipmentItemService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentItemCollection })));
      const additionalShipmentItems = [shipmentItem];
      const expectedCollection: IShipmentItem[] = [...additionalShipmentItems, ...shipmentItemCollection];
      vi.spyOn(shipmentItemService, 'addShipmentItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(shipmentItemService.query).toHaveBeenCalled();
      expect(shipmentItemService.addShipmentItemToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentItemCollection,
        ...additionalShipmentItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const issuedByParty: IParty = { id: 24527 };
      itemIssuance.issuedByParty = issuedByParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [issuedByParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const itemIssuance: IItemIssuance = { id: 8405 };
      const inventoryItem: IInventoryItem = { id: 7462 };
      itemIssuance.inventoryItem = inventoryItem;
      const order: IOrder = { id: 14644 };
      itemIssuance.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      itemIssuance.orderItem = orderItem;
      const shipment: IShipment = { id: 9594 };
      itemIssuance.shipment = shipment;
      const shipmentItem: IShipmentItem = { id: 14901 };
      itemIssuance.shipmentItem = shipmentItem;
      const issuedByParty: IParty = { id: 24527 };
      itemIssuance.issuedByParty = issuedByParty;

      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      expect(comp.inventoryItemsSharedCollection()).toContainEqual(inventoryItem);
      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.shipmentsSharedCollection()).toContainEqual(shipment);
      expect(comp.shipmentItemsSharedCollection()).toContainEqual(shipmentItem);
      expect(comp.partiesSharedCollection()).toContainEqual(issuedByParty);
      expect(comp.itemIssuance).toEqual(itemIssuance);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IItemIssuance>();
      const itemIssuance = { id: 24352 };
      vi.spyOn(itemIssuanceFormService, 'getItemIssuance').mockReturnValue(itemIssuance);
      vi.spyOn(itemIssuanceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(itemIssuance);
      saveSubject.complete();

      // THEN
      expect(itemIssuanceFormService.getItemIssuance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemIssuanceService.update).toHaveBeenCalledWith(expect.objectContaining(itemIssuance));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IItemIssuance>();
      const itemIssuance = { id: 24352 };
      vi.spyOn(itemIssuanceFormService, 'getItemIssuance').mockReturnValue({ id: null });
      vi.spyOn(itemIssuanceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemIssuance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(itemIssuance);
      saveSubject.complete();

      // THEN
      expect(itemIssuanceFormService.getItemIssuance).toHaveBeenCalled();
      expect(itemIssuanceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IItemIssuance>();
      const itemIssuance = { id: 24352 };
      vi.spyOn(itemIssuanceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemIssuance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemIssuanceService.update).toHaveBeenCalled();
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

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
