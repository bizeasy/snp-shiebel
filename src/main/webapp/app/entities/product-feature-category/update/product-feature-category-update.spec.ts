import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductFeatureCategory } from '../product-feature-category.model';
import { ProductFeatureCategoryService } from '../service/product-feature-category.service';

import { ProductFeatureCategoryFormService } from './product-feature-category-form.service';
import { ProductFeatureCategoryUpdate } from './product-feature-category-update';

describe('ProductFeatureCategory Management Update Component', () => {
  let comp: ProductFeatureCategoryUpdate;
  let fixture: ComponentFixture<ProductFeatureCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureCategoryFormService: ProductFeatureCategoryFormService;
  let productFeatureCategoryService: ProductFeatureCategoryService;

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

    fixture = TestBed.createComponent(ProductFeatureCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureCategoryFormService = TestBed.inject(ProductFeatureCategoryFormService);
    productFeatureCategoryService = TestBed.inject(ProductFeatureCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productFeatureCategory: IProductFeatureCategory = { id: 18781 };

      activatedRoute.data = of({ productFeatureCategory });
      comp.ngOnInit();

      expect(comp.productFeatureCategory).toEqual(productFeatureCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureCategory>();
      const productFeatureCategory = { id: 10959 };
      vi.spyOn(productFeatureCategoryFormService, 'getProductFeatureCategory').mockReturnValue(productFeatureCategory);
      vi.spyOn(productFeatureCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureCategory);
      saveSubject.complete();

      // THEN
      expect(productFeatureCategoryFormService.getProductFeatureCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureCategory));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureCategory>();
      const productFeatureCategory = { id: 10959 };
      vi.spyOn(productFeatureCategoryFormService, 'getProductFeatureCategory').mockReturnValue({ id: null });
      vi.spyOn(productFeatureCategoryService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureCategory);
      saveSubject.complete();

      // THEN
      expect(productFeatureCategoryFormService.getProductFeatureCategory).toHaveBeenCalled();
      expect(productFeatureCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureCategory>();
      const productFeatureCategory = { id: 10959 };
      vi.spyOn(productFeatureCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
