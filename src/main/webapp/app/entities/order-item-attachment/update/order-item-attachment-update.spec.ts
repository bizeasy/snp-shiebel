import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IOrderItemAttachment } from '../order-item-attachment.model';
import { OrderItemAttachmentService } from '../service/order-item-attachment.service';

import { OrderItemAttachmentFormService } from './order-item-attachment-form.service';
import { OrderItemAttachmentUpdate } from './order-item-attachment-update';

describe('OrderItemAttachment Management Update Component', () => {
  let comp: OrderItemAttachmentUpdate;
  let fixture: ComponentFixture<OrderItemAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderItemAttachmentFormService: OrderItemAttachmentFormService;
  let orderItemAttachmentService: OrderItemAttachmentService;
  let orderItemService: OrderItemService;
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

    fixture = TestBed.createComponent(OrderItemAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemAttachmentFormService = TestBed.inject(OrderItemAttachmentFormService);
    orderItemAttachmentService = TestBed.inject(OrderItemAttachmentService);
    orderItemService = TestBed.inject(OrderItemService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OrderItem query and add missing value', () => {
      const orderItemAttachment: IOrderItemAttachment = { id: 26311 };
      const orderItem: IOrderItem = { id: 25971 };
      orderItemAttachment.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 25971 }];
      vi.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      vi.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemAttachment });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const orderItemAttachment: IOrderItemAttachment = { id: 26311 };
      const attachment: IAttachment = { id: 8078 };
      orderItemAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderItemAttachment: IOrderItemAttachment = { id: 26311 };
      const orderItem: IOrderItem = { id: 25971 };
      orderItemAttachment.orderItem = orderItem;
      const attachment: IAttachment = { id: 8078 };
      orderItemAttachment.attachment = attachment;

      activatedRoute.data = of({ orderItemAttachment });
      comp.ngOnInit();

      expect(comp.orderItemsSharedCollection()).toContainEqual(orderItem);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.orderItemAttachment).toEqual(orderItemAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemAttachment>();
      const orderItemAttachment = { id: 4150 };
      vi.spyOn(orderItemAttachmentFormService, 'getOrderItemAttachment').mockReturnValue(orderItemAttachment);
      vi.spyOn(orderItemAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItemAttachment);
      saveSubject.complete();

      // THEN
      expect(orderItemAttachmentFormService.getOrderItemAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(orderItemAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemAttachment>();
      const orderItemAttachment = { id: 4150 };
      vi.spyOn(orderItemAttachmentFormService, 'getOrderItemAttachment').mockReturnValue({ id: null });
      vi.spyOn(orderItemAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItemAttachment);
      saveSubject.complete();

      // THEN
      expect(orderItemAttachmentFormService.getOrderItemAttachment).toHaveBeenCalled();
      expect(orderItemAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemAttachment>();
      const orderItemAttachment = { id: 4150 };
      vi.spyOn(orderItemAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemAttachmentService.update).toHaveBeenCalled();
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
