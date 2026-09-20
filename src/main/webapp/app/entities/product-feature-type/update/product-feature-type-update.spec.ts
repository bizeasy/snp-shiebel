import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductFeatureType } from '../product-feature-type.model';
import { ProductFeatureTypeService } from '../service/product-feature-type.service';

import { ProductFeatureTypeFormService } from './product-feature-type-form.service';
import { ProductFeatureTypeUpdate } from './product-feature-type-update';

describe('ProductFeatureType Management Update Component', () => {
  let comp: ProductFeatureTypeUpdate;
  let fixture: ComponentFixture<ProductFeatureTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureTypeFormService: ProductFeatureTypeFormService;
  let productFeatureTypeService: ProductFeatureTypeService;

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

    fixture = TestBed.createComponent(ProductFeatureTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureTypeFormService = TestBed.inject(ProductFeatureTypeFormService);
    productFeatureTypeService = TestBed.inject(ProductFeatureTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productFeatureType: IProductFeatureType = { id: 20310 };

      activatedRoute.data = of({ productFeatureType });
      comp.ngOnInit();

      expect(comp.productFeatureType).toEqual(productFeatureType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureType>();
      const productFeatureType = { id: 29389 };
      vi.spyOn(productFeatureTypeFormService, 'getProductFeatureType').mockReturnValue(productFeatureType);
      vi.spyOn(productFeatureTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureType);
      saveSubject.complete();

      // THEN
      expect(productFeatureTypeFormService.getProductFeatureType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureTypeService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureType>();
      const productFeatureType = { id: 29389 };
      vi.spyOn(productFeatureTypeFormService, 'getProductFeatureType').mockReturnValue({ id: null });
      vi.spyOn(productFeatureTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureType);
      saveSubject.complete();

      // THEN
      expect(productFeatureTypeFormService.getProductFeatureType).toHaveBeenCalled();
      expect(productFeatureTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureType>();
      const productFeatureType = { id: 29389 };
      vi.spyOn(productFeatureTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
