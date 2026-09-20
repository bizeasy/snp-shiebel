import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { ContactMechPurposeService } from 'app/entities/contact-mech-purpose/service/contact-mech-purpose.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderContactMech } from '../order-contact-mech.model';
import { OrderContactMechService } from '../service/order-contact-mech.service';

import { OrderContactMechFormService } from './order-contact-mech-form.service';
import { OrderContactMechUpdate } from './order-contact-mech-update';

describe('OrderContactMech Management Update Component', () => {
  let comp: OrderContactMechUpdate;
  let fixture: ComponentFixture<OrderContactMechUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderContactMechFormService: OrderContactMechFormService;
  let orderContactMechService: OrderContactMechService;
  let orderService: OrderService;
  let contactMechService: ContactMechService;
  let contactMechPurposeService: ContactMechPurposeService;

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

    fixture = TestBed.createComponent(OrderContactMechUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderContactMechFormService = TestBed.inject(OrderContactMechFormService);
    orderContactMechService = TestBed.inject(OrderContactMechService);
    orderService = TestBed.inject(OrderService);
    contactMechService = TestBed.inject(ContactMechService);
    contactMechPurposeService = TestBed.inject(ContactMechPurposeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Order query and add missing value', () => {
      const orderContactMech: IOrderContactMech = { id: 17412 };
      const order: IOrder = { id: 14644 };
      orderContactMech.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderContactMech });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMech query and add missing value', () => {
      const orderContactMech: IOrderContactMech = { id: 17412 };
      const contactMech: IContactMech = { id: 5019 };
      orderContactMech.contactMech = contactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [contactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderContactMech });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMechPurpose query and add missing value', () => {
      const orderContactMech: IOrderContactMech = { id: 17412 };
      const contactMechPurpose: IContactMechPurpose = { id: 25033 };
      orderContactMech.contactMechPurpose = contactMechPurpose;

      const contactMechPurposeCollection: IContactMechPurpose[] = [{ id: 25033 }];
      vi.spyOn(contactMechPurposeService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechPurposeCollection })));
      const additionalContactMechPurposes = [contactMechPurpose];
      const expectedCollection: IContactMechPurpose[] = [...additionalContactMechPurposes, ...contactMechPurposeCollection];
      vi.spyOn(contactMechPurposeService, 'addContactMechPurposeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderContactMech });
      comp.ngOnInit();

      expect(contactMechPurposeService.query).toHaveBeenCalled();
      expect(contactMechPurposeService.addContactMechPurposeToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechPurposeCollection,
        ...additionalContactMechPurposes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechPurposesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderContactMech: IOrderContactMech = { id: 17412 };
      const order: IOrder = { id: 14644 };
      orderContactMech.order = order;
      const contactMech: IContactMech = { id: 5019 };
      orderContactMech.contactMech = contactMech;
      const contactMechPurpose: IContactMechPurpose = { id: 25033 };
      orderContactMech.contactMechPurpose = contactMechPurpose;

      activatedRoute.data = of({ orderContactMech });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.contactMechesSharedCollection()).toContainEqual(contactMech);
      expect(comp.contactMechPurposesSharedCollection()).toContainEqual(contactMechPurpose);
      expect(comp.orderContactMech).toEqual(orderContactMech);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderContactMech>();
      const orderContactMech = { id: 112 };
      vi.spyOn(orderContactMechFormService, 'getOrderContactMech').mockReturnValue(orderContactMech);
      vi.spyOn(orderContactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderContactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderContactMech);
      saveSubject.complete();

      // THEN
      expect(orderContactMechFormService.getOrderContactMech).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderContactMechService.update).toHaveBeenCalledWith(expect.objectContaining(orderContactMech));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderContactMech>();
      const orderContactMech = { id: 112 };
      vi.spyOn(orderContactMechFormService, 'getOrderContactMech').mockReturnValue({ id: null });
      vi.spyOn(orderContactMechService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderContactMech: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderContactMech);
      saveSubject.complete();

      // THEN
      expect(orderContactMechFormService.getOrderContactMech).toHaveBeenCalled();
      expect(orderContactMechService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderContactMech>();
      const orderContactMech = { id: 112 };
      vi.spyOn(orderContactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderContactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderContactMechService.update).toHaveBeenCalled();
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

    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareContactMechPurpose', () => {
      it('should forward to contactMechPurposeService', () => {
        const entity = { id: 25033 };
        const entity2 = { id: 10745 };
        vi.spyOn(contactMechPurposeService, 'compareContactMechPurpose');
        comp.compareContactMechPurpose(entity, entity2);
        expect(contactMechPurposeService.compareContactMechPurpose).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
