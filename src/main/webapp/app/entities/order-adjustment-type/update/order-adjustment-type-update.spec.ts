import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrderAdjustmentType } from '../order-adjustment-type.model';
import { OrderAdjustmentTypeService } from '../service/order-adjustment-type.service';

import { OrderAdjustmentTypeFormService } from './order-adjustment-type-form.service';
import { OrderAdjustmentTypeUpdate } from './order-adjustment-type-update';

describe('OrderAdjustmentType Management Update Component', () => {
  let comp: OrderAdjustmentTypeUpdate;
  let fixture: ComponentFixture<OrderAdjustmentTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderAdjustmentTypeFormService: OrderAdjustmentTypeFormService;
  let orderAdjustmentTypeService: OrderAdjustmentTypeService;

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

    fixture = TestBed.createComponent(OrderAdjustmentTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderAdjustmentTypeFormService = TestBed.inject(OrderAdjustmentTypeFormService);
    orderAdjustmentTypeService = TestBed.inject(OrderAdjustmentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OrderAdjustmentType query and add missing value', () => {
      const orderAdjustmentType: IOrderAdjustmentType = { id: 11072 };
      const parent: IOrderAdjustmentType = { id: 1453 };
      orderAdjustmentType.parent = parent;

      const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [{ id: 1453 }];
      vi.spyOn(orderAdjustmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: orderAdjustmentTypeCollection })));
      const additionalOrderAdjustmentTypes = [parent];
      const expectedCollection: IOrderAdjustmentType[] = [...additionalOrderAdjustmentTypes, ...orderAdjustmentTypeCollection];
      vi.spyOn(orderAdjustmentTypeService, 'addOrderAdjustmentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderAdjustmentType });
      comp.ngOnInit();

      expect(orderAdjustmentTypeService.query).toHaveBeenCalled();
      expect(orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        orderAdjustmentTypeCollection,
        ...additionalOrderAdjustmentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderAdjustmentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orderAdjustmentType: IOrderAdjustmentType = { id: 11072 };
      const parent: IOrderAdjustmentType = { id: 1453 };
      orderAdjustmentType.parent = parent;

      activatedRoute.data = of({ orderAdjustmentType });
      comp.ngOnInit();

      expect(comp.orderAdjustmentTypesSharedCollection()).toContainEqual(parent);
      expect(comp.orderAdjustmentType).toEqual(orderAdjustmentType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAdjustmentType>();
      const orderAdjustmentType = { id: 1453 };
      vi.spyOn(orderAdjustmentTypeFormService, 'getOrderAdjustmentType').mockReturnValue(orderAdjustmentType);
      vi.spyOn(orderAdjustmentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAdjustmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderAdjustmentType);
      saveSubject.complete();

      // THEN
      expect(orderAdjustmentTypeFormService.getOrderAdjustmentType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderAdjustmentTypeService.update).toHaveBeenCalledWith(expect.objectContaining(orderAdjustmentType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAdjustmentType>();
      const orderAdjustmentType = { id: 1453 };
      vi.spyOn(orderAdjustmentTypeFormService, 'getOrderAdjustmentType').mockReturnValue({ id: null });
      vi.spyOn(orderAdjustmentTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAdjustmentType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orderAdjustmentType);
      saveSubject.complete();

      // THEN
      expect(orderAdjustmentTypeFormService.getOrderAdjustmentType).toHaveBeenCalled();
      expect(orderAdjustmentTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrderAdjustmentType>();
      const orderAdjustmentType = { id: 1453 };
      vi.spyOn(orderAdjustmentTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderAdjustmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderAdjustmentTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrderAdjustmentType', () => {
      it('should forward to orderAdjustmentTypeService', () => {
        const entity = { id: 1453 };
        const entity2 = { id: 11072 };
        vi.spyOn(orderAdjustmentTypeService, 'compareOrderAdjustmentType');
        comp.compareOrderAdjustmentType(entity, entity2);
        expect(orderAdjustmentTypeService.compareOrderAdjustmentType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
