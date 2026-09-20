import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from 'app/entities/shipment-item/service/shipment-item.service';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { IOrderShipment } from '../order-shipment.model';
import { OrderShipmentService } from '../service/order-shipment.service';

import { OrderShipmentFormService } from './order-shipment-form.service';
import { OrderShipmentUpdate } from './order-shipment-update';

describe('OrderShipment Management Update Component', () => {
  let comp: OrderShipmentUpdate;
  let fixture: ComponentFixture<OrderShipmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderShipmentFormService: OrderShipmentFormService;
  let orderShipmentService: OrderShipmentService;
  let orderService: OrderService;
  let orderItemService: OrderItemService;
  let shipmentService: ShipmentService;
  let shipmentItemService: ShipmentItemService;

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

    fixture = TestBed.createComponent(OrderShipmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderShipmentFormService = TestBed.inject(OrderShipmentFormService);
    orderShipmentService = TestBed.inject(OrderShipmentService);
    orderService = TestBed.inject(OrderService);
    orderItemService = TestBed.inject(OrderItemService);
    shipmentService = TestBed.inject(ShipmentService);
    shipmentItemService = TestBed.inject(ShipmentItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Order query and add missing value', () => {
      const orderShipment: IOrderShipment = { id: 18335 };
      const order: IOrder = { id: 14644 };
      orderShipment.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderItem query and add missing value', () => {
      const orderShipment: IOrderShipment = { id: 18335 };
      const orderItem: IOrderItem = { id: 25971 };
      orderShipment.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Shipment query and add missing value', () => {
      const orderShipment: IOrderShipment = { id: 18335 };
      const shipment: IShipment = { id: 9594 };
      orderShipment.shipment = shipment;

      const shipmentCollection: IShipment[] = [{ id: 9594 }];
      vi.spyOn(shipmentService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentCollection })));
      const additionalShipments = [shipment];
      const expectedCollection: IShipment[] = [...additionalShipments, ...shipmentCollection];
      vi.spyOn(shipmentService, 'addShipmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      expect(shipmentService.query).toHaveBeenCalled();
      expect(shipmentService.addShipmentToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentCollection,
        ...additionalShipments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ShipmentItem query and add missing value', () => {
      const orderShipment: IOrderShipment = { id: 18335 };
      const shipmentItem: IShipmentItem = { id: 14901 };
      orderShipment.shipmentItem = shipmentItem;

      const shipmentItemCollection: IShipmentItem[] = [{ id: 14901 }];
      vi.spyOn(shipmentItemService, 'query').mockReturnValue(of(new HttpResponse({ body: shipmentItemCollection })));
      const additionalShipmentItems = [shipmentItem];
      const expectedCollection: IShipmentItem[] = [...additionalShipmentItems, ...shipmentItemCollection];
      vi.spyOn(shipmentItemService, 'addShipmentItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      expect(shipmentItemService.query).toHaveBeenCalled();
      expect(shipmentItemService.addShipmentItemToCollectionIfMissing).toHaveBeenCalledWith(
        shipmentItemCollection,
        ...additionalShipmentItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shipmentItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderShipment: IOrderShipment = { id: 18335 };
      const order: IOrder = { id: 14644 };
      orderShipment.order = order;
      const orderItem: IOrderItem = { id: 25971 };
      orderShipment.orderItem = orderItem;
      const shipment: IShipment = { id: 9594 };
      orderShipment.shipment = shipment;
      const shipmentItem: IShipmentItem = { id: 14901 };
      orderShipment.shipmentItem = shipmentItem;

      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.shipmentsSharedCollection()).toContainEqual(shipment);
      expect(comp.shipmentItemsSharedCollection()).toContainEqual(shipmentItem);
      expect(comp.orderShipment).toEqual(orderShipment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderShipment>();
      const orderShipment = { id: 15441 };
      vi.spyOn(orderShipmentFormService, 'getOrderShipment').mockReturnValue(orderShipment);
      vi.spyOn(orderShipmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderShipment);
      saveSubject.complete();

      // THEN
      expect(orderShipmentFormService.getOrderShipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderShipmentService.update).toHaveBeenCalledWith(expect.objectContaining(orderShipment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderShipment>();
      const orderShipment = { id: 15441 };
      vi.spyOn(orderShipmentFormService, 'getOrderShipment').mockReturnValue({ id: null });
      vi.spyOn(orderShipmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderShipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderShipment);
      saveSubject.complete();

      // THEN
      expect(orderShipmentFormService.getOrderShipment).toHaveBeenCalled();
      expect(orderShipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderShipment>();
      const orderShipment = { id: 15441 };
      vi.spyOn(orderShipmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderShipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderShipmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
  });
});
