import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { IProductFeatureCategory } from 'app/entities/product-feature-category/product-feature-category.model';
import { ProductFeatureCategoryService } from 'app/entities/product-feature-category/service/product-feature-category.service';
import { IProductFeatureCategoryAppl } from '../product-feature-category-appl.model';
import { ProductFeatureCategoryApplService } from '../service/product-feature-category-appl.service';

import { ProductFeatureCategoryApplFormService } from './product-feature-category-appl-form.service';
import { ProductFeatureCategoryApplUpdate } from './product-feature-category-appl-update';

describe('ProductFeatureCategoryAppl Management Update Component', () => {
  let comp: ProductFeatureCategoryApplUpdate;
  let fixture: ComponentFixture<ProductFeatureCategoryApplUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureCategoryApplFormService: ProductFeatureCategoryApplFormService;
  let productFeatureCategoryApplService: ProductFeatureCategoryApplService;
  let productCategoryService: ProductCategoryService;
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

    fixture = TestBed.createComponent(ProductFeatureCategoryApplUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureCategoryApplFormService = TestBed.inject(ProductFeatureCategoryApplFormService);
    productFeatureCategoryApplService = TestBed.inject(ProductFeatureCategoryApplService);
    productCategoryService = TestBed.inject(ProductCategoryService);
    productFeatureCategoryService = TestBed.inject(ProductFeatureCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ProductCategory query and add missing value', () => {
      const productFeatureCategoryAppl: IProductFeatureCategoryAppl = { id: 12894 };
      const productCategory: IProductCategory = { id: 29286 };
      productFeatureCategoryAppl.productCategory = productCategory;

      const productCategoryCollection: IProductCategory[] = [{ id: 29286 }];
      vi.spyOn(productCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productCategoryCollection })));
      const additionalProductCategories = [productCategory];
      const expectedCollection: IProductCategory[] = [...additionalProductCategories, ...productCategoryCollection];
      vi.spyOn(productCategoryService, 'addProductCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureCategoryAppl });
      comp.ngOnInit();

      expect(productCategoryService.query).toHaveBeenCalled();
      expect(productCategoryService.addProductCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productCategoryCollection,
        ...additionalProductCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeatureCategory query and add missing value', () => {
      const productFeatureCategoryAppl: IProductFeatureCategoryAppl = { id: 12894 };
      const productFeatureCategory: IProductFeatureCategory = { id: 10959 };
      productFeatureCategoryAppl.productFeatureCategory = productFeatureCategory;

      const productFeatureCategoryCollection: IProductFeatureCategory[] = [{ id: 10959 }];
      vi.spyOn(productFeatureCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCategoryCollection })));
      const additionalProductFeatureCategories = [productFeatureCategory];
      const expectedCollection: IProductFeatureCategory[] = [...additionalProductFeatureCategories, ...productFeatureCategoryCollection];
      vi.spyOn(productFeatureCategoryService, 'addProductFeatureCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureCategoryAppl });
      comp.ngOnInit();

      expect(productFeatureCategoryService.query).toHaveBeenCalled();
      expect(productFeatureCategoryService.addProductFeatureCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCategoryCollection,
        ...additionalProductFeatureCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeatureCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFeatureCategoryAppl: IProductFeatureCategoryAppl = { id: 12894 };
      const productCategory: IProductCategory = { id: 29286 };
      productFeatureCategoryAppl.productCategory = productCategory;
      const productFeatureCategory: IProductFeatureCategory = { id: 10959 };
      productFeatureCategoryAppl.productFeatureCategory = productFeatureCategory;

      activatedRoute.data = of({ productFeatureCategoryAppl });
      comp.ngOnInit();

      expect(comp.productCategoriesSharedCollection()).toContainEqual(productCategory);
      expect(comp.productFeatureCategoriesSharedCollection()).toContainEqual(productFeatureCategory);
      expect(comp.productFeatureCategoryAppl).toEqual(productFeatureCategoryAppl);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureCategoryAppl>();
      const productFeatureCategoryAppl = { id: 5520 };
      vi.spyOn(productFeatureCategoryApplFormService, 'getProductFeatureCategoryAppl').mockReturnValue(productFeatureCategoryAppl);
      vi.spyOn(productFeatureCategoryApplService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureCategoryAppl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureCategoryAppl);
      saveSubject.complete();

      // THEN
      expect(productFeatureCategoryApplFormService.getProductFeatureCategoryAppl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureCategoryApplService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureCategoryAppl));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureCategoryAppl>();
      const productFeatureCategoryAppl = { id: 5520 };
      vi.spyOn(productFeatureCategoryApplFormService, 'getProductFeatureCategoryAppl').mockReturnValue({ id: null });
      vi.spyOn(productFeatureCategoryApplService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureCategoryAppl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureCategoryAppl);
      saveSubject.complete();

      // THEN
      expect(productFeatureCategoryApplFormService.getProductFeatureCategoryAppl).toHaveBeenCalled();
      expect(productFeatureCategoryApplService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureCategoryAppl>();
      const productFeatureCategoryAppl = { id: 5520 };
      vi.spyOn(productFeatureCategoryApplService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureCategoryAppl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureCategoryApplService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductCategory', () => {
      it('should forward to productCategoryService', () => {
        const entity = { id: 29286 };
        const entity2 = { id: 19244 };
        vi.spyOn(productCategoryService, 'compareProductCategory');
        comp.compareProductCategory(entity, entity2);
        expect(productCategoryService.compareProductCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductFeatureCategory', () => {
      it('should forward to productFeatureCategoryService', () => {
        const entity = { id: 10959 };
        const entity2 = { id: 18781 };
        vi.spyOn(productFeatureCategoryService, 'compareProductFeatureCategory');
        comp.compareProductFeatureCategory(entity, entity2);
        expect(productFeatureCategoryService.compareProductFeatureCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
