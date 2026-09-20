import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductFeatureApplType } from '../product-feature-appl-type.model';
import { ProductFeatureApplTypeService } from '../service/product-feature-appl-type.service';

import { ProductFeatureApplTypeFormService } from './product-feature-appl-type-form.service';
import { ProductFeatureApplTypeUpdate } from './product-feature-appl-type-update';

describe('ProductFeatureApplType Management Update Component', () => {
  let comp: ProductFeatureApplTypeUpdate;
  let fixture: ComponentFixture<ProductFeatureApplTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureApplTypeFormService: ProductFeatureApplTypeFormService;
  let productFeatureApplTypeService: ProductFeatureApplTypeService;

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

    fixture = TestBed.createComponent(ProductFeatureApplTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureApplTypeFormService = TestBed.inject(ProductFeatureApplTypeFormService);
    productFeatureApplTypeService = TestBed.inject(ProductFeatureApplTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productFeatureApplType: IProductFeatureApplType = { id: 19218 };

      activatedRoute.data = of({ productFeatureApplType });
      comp.ngOnInit();

      expect(comp.productFeatureApplType).toEqual(productFeatureApplType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureApplType>();
      const productFeatureApplType = { id: 6261 };
      vi.spyOn(productFeatureApplTypeFormService, 'getProductFeatureApplType').mockReturnValue(productFeatureApplType);
      vi.spyOn(productFeatureApplTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureApplType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureApplType);
      saveSubject.complete();

      // THEN
      expect(productFeatureApplTypeFormService.getProductFeatureApplType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureApplTypeService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureApplType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureApplType>();
      const productFeatureApplType = { id: 6261 };
      vi.spyOn(productFeatureApplTypeFormService, 'getProductFeatureApplType').mockReturnValue({ id: null });
      vi.spyOn(productFeatureApplTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureApplType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureApplType);
      saveSubject.complete();

      // THEN
      expect(productFeatureApplTypeFormService.getProductFeatureApplType).toHaveBeenCalled();
      expect(productFeatureApplTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureApplType>();
      const productFeatureApplType = { id: 6261 };
      vi.spyOn(productFeatureApplTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureApplType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureApplTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
