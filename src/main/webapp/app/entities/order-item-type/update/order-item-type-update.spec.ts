import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrderItemType } from '../order-item-type.model';
import { OrderItemTypeService } from '../service/order-item-type.service';

import { OrderItemTypeFormService } from './order-item-type-form.service';
import { OrderItemTypeUpdate } from './order-item-type-update';

describe('OrderItemType Management Update Component', () => {
  let comp: OrderItemTypeUpdate;
  let fixture: ComponentFixture<OrderItemTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderItemTypeFormService: OrderItemTypeFormService;
  let orderItemTypeService: OrderItemTypeService;

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

    fixture = TestBed.createComponent(OrderItemTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemTypeFormService = TestBed.inject(OrderItemTypeFormService);
    orderItemTypeService = TestBed.inject(OrderItemTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const orderItemType: IOrderItemType = { id: 18748 };

      activatedRoute.data = of({ orderItemType });
      comp.ngOnInit();

      expect(comp.orderItemType).toEqual(orderItemType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemType>();
      const orderItemType = { id: 31138 };
      vi.spyOn(orderItemTypeFormService, 'getOrderItemType').mockReturnValue(orderItemType);
      vi.spyOn(orderItemTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItemType);
      saveSubject.complete();

      // THEN
      expect(orderItemTypeFormService.getOrderItemType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemTypeService.update).toHaveBeenCalledWith(expect.objectContaining(orderItemType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemType>();
      const orderItemType = { id: 31138 };
      vi.spyOn(orderItemTypeFormService, 'getOrderItemType').mockReturnValue({ id: null });
      vi.spyOn(orderItemTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderItemType);
      saveSubject.complete();

      // THEN
      expect(orderItemTypeFormService.getOrderItemType).toHaveBeenCalled();
      expect(orderItemTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderItemType>();
      const orderItemType = { id: 31138 };
      vi.spyOn(orderItemTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
