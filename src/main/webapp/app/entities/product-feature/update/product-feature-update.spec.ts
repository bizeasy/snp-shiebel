import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductFeatureCategory } from 'app/entities/product-feature-category/product-feature-category.model';
import { ProductFeatureCategoryService } from 'app/entities/product-feature-category/service/product-feature-category.service';
import { IProductFeatureType } from 'app/entities/product-feature-type/product-feature-type.model';
import { ProductFeatureTypeService } from 'app/entities/product-feature-type/service/product-feature-type.service';
import { IProductFeature } from '../product-feature.model';
import { ProductFeatureService } from '../service/product-feature.service';

import { ProductFeatureFormService } from './product-feature-form.service';
import { ProductFeatureUpdate } from './product-feature-update';

describe('ProductFeature Management Update Component', () => {
  let comp: ProductFeatureUpdate;
  let fixture: ComponentFixture<ProductFeatureUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureFormService: ProductFeatureFormService;
  let productFeatureService: ProductFeatureService;
  let productFeatureTypeService: ProductFeatureTypeService;
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

    fixture = TestBed.createComponent(ProductFeatureUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureFormService = TestBed.inject(ProductFeatureFormService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    productFeatureTypeService = TestBed.inject(ProductFeatureTypeService);
    productFeatureCategoryService = TestBed.inject(ProductFeatureCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ProductFeatureType query and add missing value', () => {
      const productFeature: IProductFeature = { id: 7728 };
      const productFeatureType: IProductFeatureType = { id: 29389 };
      productFeature.productFeatureType = productFeatureType;

      const productFeatureTypeCollection: IProductFeatureType[] = [{ id: 29389 }];
      vi.spyOn(productFeatureTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureTypeCollection })));
      const additionalProductFeatureTypes = [productFeatureType];
      const expectedCollection: IProductFeatureType[] = [...additionalProductFeatureTypes, ...productFeatureTypeCollection];
      vi.spyOn(productFeatureTypeService, 'addProductFeatureTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeature });
      comp.ngOnInit();

      expect(productFeatureTypeService.query).toHaveBeenCalled();
      expect(productFeatureTypeService.addProductFeatureTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureTypeCollection,
        ...additionalProductFeatureTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeatureTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeatureCategory query and add missing value', () => {
      const productFeature: IProductFeature = { id: 7728 };
      const productFeatureCategory: IProductFeatureCategory = { id: 10959 };
      productFeature.productFeatureCategory = productFeatureCategory;

      const productFeatureCategoryCollection: IProductFeatureCategory[] = [{ id: 10959 }];
      vi.spyOn(productFeatureCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCategoryCollection })));
      const additionalProductFeatureCategories = [productFeatureCategory];
      const expectedCollection: IProductFeatureCategory[] = [...additionalProductFeatureCategories, ...productFeatureCategoryCollection];
      vi.spyOn(productFeatureCategoryService, 'addProductFeatureCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeature });
      comp.ngOnInit();

      expect(productFeatureCategoryService.query).toHaveBeenCalled();
      expect(productFeatureCategoryService.addProductFeatureCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCategoryCollection,
        ...additionalProductFeatureCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeatureCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFeature: IProductFeature = { id: 7728 };
      const productFeatureType: IProductFeatureType = { id: 29389 };
      productFeature.productFeatureType = productFeatureType;
      const productFeatureCategory: IProductFeatureCategory = { id: 10959 };
      productFeature.productFeatureCategory = productFeatureCategory;

      activatedRoute.data = of({ productFeature });
      comp.ngOnInit();

      expect(comp.productFeatureTypesSharedCollection()).toContainEqual(productFeatureType);
      expect(comp.productFeatureCategoriesSharedCollection()).toContainEqual(productFeatureCategory);
      expect(comp.productFeature).toEqual(productFeature);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeature>();
      const productFeature = { id: 30957 };
      vi.spyOn(productFeatureFormService, 'getProductFeature').mockReturnValue(productFeature);
      vi.spyOn(productFeatureService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeature);
      saveSubject.complete();

      // THEN
      expect(productFeatureFormService.getProductFeature).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureService.update).toHaveBeenCalledWith(expect.objectContaining(productFeature));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeature>();
      const productFeature = { id: 30957 };
      vi.spyOn(productFeatureFormService, 'getProductFeature').mockReturnValue({ id: null });
      vi.spyOn(productFeatureService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeature: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeature);
      saveSubject.complete();

      // THEN
      expect(productFeatureFormService.getProductFeature).toHaveBeenCalled();
      expect(productFeatureService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeature>();
      const productFeature = { id: 30957 };
      vi.spyOn(productFeatureService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductFeatureType', () => {
      it('should forward to productFeatureTypeService', () => {
        const entity = { id: 29389 };
        const entity2 = { id: 20310 };
        vi.spyOn(productFeatureTypeService, 'compareProductFeatureType');
        comp.compareProductFeatureType(entity, entity2);
        expect(productFeatureTypeService.compareProductFeatureType).toHaveBeenCalledWith(entity, entity2);
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
