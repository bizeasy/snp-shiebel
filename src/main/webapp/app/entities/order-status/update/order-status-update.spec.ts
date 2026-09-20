import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { IOrderStatus } from '../order-status.model';
import { OrderStatusService } from '../service/order-status.service';

import { OrderStatusFormService } from './order-status-form.service';
import { OrderStatusUpdate } from './order-status-update';

describe('OrderStatus Management Update Component', () => {
  let comp: OrderStatusUpdate;
  let fixture: ComponentFixture<OrderStatusUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderStatusFormService: OrderStatusFormService;
  let orderStatusService: OrderStatusService;
  let orderService: OrderService;
  let statusService: StatusService;

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

    fixture = TestBed.createComponent(OrderStatusUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderStatusFormService = TestBed.inject(OrderStatusFormService);
    orderStatusService = TestBed.inject(OrderStatusService);
    orderService = TestBed.inject(OrderService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Order query and add missing value', () => {
      const orderStatus: IOrderStatus = { id: 6707 };
      const order: IOrder = { id: 14644 };
      orderStatus.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const orderStatus: IOrderStatus = { id: 6707 };
      const status: IStatus = { id: 2927 };
      orderStatus.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderStatus: IOrderStatus = { id: 6707 };
      const order: IOrder = { id: 14644 };
      orderStatus.order = order;
      const status: IStatus = { id: 2927 };
      orderStatus.status = status;

      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.orderStatus).toEqual(orderStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderStatus>();
      const orderStatus = { id: 5634 };
      vi.spyOn(orderStatusFormService, 'getOrderStatus').mockReturnValue(orderStatus);
      vi.spyOn(orderStatusService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderStatus);
      saveSubject.complete();

      // THEN
      expect(orderStatusFormService.getOrderStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderStatusService.update).toHaveBeenCalledWith(expect.objectContaining(orderStatus));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderStatus>();
      const orderStatus = { id: 5634 };
      vi.spyOn(orderStatusFormService, 'getOrderStatus').mockReturnValue({ id: null });
      vi.spyOn(orderStatusService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderStatus);
      saveSubject.complete();

      // THEN
      expect(orderStatusFormService.getOrderStatus).toHaveBeenCalled();
      expect(orderStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderStatus>();
      const orderStatus = { id: 5634 };
      vi.spyOn(orderStatusService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderStatusService.update).toHaveBeenCalled();
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

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
