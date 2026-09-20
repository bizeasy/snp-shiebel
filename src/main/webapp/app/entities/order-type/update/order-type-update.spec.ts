import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrderType } from '../order-type.model';
import { OrderTypeService } from '../service/order-type.service';

import { OrderTypeFormService } from './order-type-form.service';
import { OrderTypeUpdate } from './order-type-update';

describe('OrderType Management Update Component', () => {
  let comp: OrderTypeUpdate;
  let fixture: ComponentFixture<OrderTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderTypeFormService: OrderTypeFormService;
  let orderTypeService: OrderTypeService;

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

    fixture = TestBed.createComponent(OrderTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderTypeFormService = TestBed.inject(OrderTypeFormService);
    orderTypeService = TestBed.inject(OrderTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const orderType: IOrderType = { id: 28377 };

      activatedRoute.data = of({ orderType });
      comp.ngOnInit();

      expect(comp.orderType).toEqual(orderType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderType>();
      const orderType = { id: 10129 };
      vi.spyOn(orderTypeFormService, 'getOrderType').mockReturnValue(orderType);
      vi.spyOn(orderTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderType);
      saveSubject.complete();

      // THEN
      expect(orderTypeFormService.getOrderType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderTypeService.update).toHaveBeenCalledWith(expect.objectContaining(orderType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderType>();
      const orderType = { id: 10129 };
      vi.spyOn(orderTypeFormService, 'getOrderType').mockReturnValue({ id: null });
      vi.spyOn(orderTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderType);
      saveSubject.complete();

      // THEN
      expect(orderTypeFormService.getOrderType).toHaveBeenCalled();
      expect(orderTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderType>();
      const orderType = { id: 10129 };
      vi.spyOn(orderTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
