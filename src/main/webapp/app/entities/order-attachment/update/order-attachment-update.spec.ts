import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderAttachment } from '../order-attachment.model';
import { OrderAttachmentService } from '../service/order-attachment.service';

import { OrderAttachmentFormService } from './order-attachment-form.service';
import { OrderAttachmentUpdate } from './order-attachment-update';

describe('OrderAttachment Management Update Component', () => {
  let comp: OrderAttachmentUpdate;
  let fixture: ComponentFixture<OrderAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderAttachmentFormService: OrderAttachmentFormService;
  let orderAttachmentService: OrderAttachmentService;
  let orderService: OrderService;
  let attachmentService: AttachmentService;

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

    fixture = TestBed.createComponent(OrderAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderAttachmentFormService = TestBed.inject(OrderAttachmentFormService);
    orderAttachmentService = TestBed.inject(OrderAttachmentService);
    orderService = TestBed.inject(OrderService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Order query and add missing value', () => {
      const orderAttachment: IOrderAttachment = { id: 21727 };
      const order: IOrder = { id: 14644 };
      orderAttachment.order = order;

      const orderCollection: IOrder[] = [{ id: 14644 }];
      vi.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      vi.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAttachment });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(
        orderCollection,
        ...additionalOrders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const orderAttachment: IOrderAttachment = { id: 21727 };
      const attachment: IAttachment = { id: 8078 };
      orderAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderAttachment: IOrderAttachment = { id: 21727 };
      const order: IOrder = { id: 14644 };
      orderAttachment.order = order;
      const attachment: IAttachment = { id: 8078 };
      orderAttachment.attachment = attachment;

      activatedRoute.data = of({ orderAttachment });
      comp.ngOnInit();

      expect(comp.ordersSharedCollection()).toContainEqual(order);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.orderAttachment).toEqual(orderAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAttachment>();
      const orderAttachment = { id: 4865 };
      vi.spyOn(orderAttachmentFormService, 'getOrderAttachment').mockReturnValue(orderAttachment);
      vi.spyOn(orderAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderAttachment);
      saveSubject.complete();

      // THEN
      expect(orderAttachmentFormService.getOrderAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(orderAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAttachment>();
      const orderAttachment = { id: 4865 };
      vi.spyOn(orderAttachmentFormService, 'getOrderAttachment').mockReturnValue({ id: null });
      vi.spyOn(orderAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderAttachment);
      saveSubject.complete();

      // THEN
      expect(orderAttachmentFormService.getOrderAttachment).toHaveBeenCalled();
      expect(orderAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAttachment>();
      const orderAttachment = { id: 4865 };
      vi.spyOn(orderAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderAttachmentService.update).toHaveBeenCalled();
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

    describe('compareAttachment', () => {
      it('should forward to attachmentService', () => {
        const entity = { id: 8078 };
        const entity2 = { id: 16801 };
        vi.spyOn(attachmentService, 'compareAttachment');
        comp.compareAttachment(entity, entity2);
        expect(attachmentService.compareAttachment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
