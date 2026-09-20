import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductCategoryType } from 'app/entities/product-category-type/product-category-type.model';
import { ProductCategoryTypeService } from 'app/entities/product-category-type/service/product-category-type.service';
import { IProductCategory } from '../product-category.model';
import { ProductCategoryService } from '../service/product-category.service';

import { ProductCategoryFormService } from './product-category-form.service';
import { ProductCategoryUpdate } from './product-category-update';

describe('ProductCategory Management Update Component', () => {
  let comp: ProductCategoryUpdate;
  let fixture: ComponentFixture<ProductCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let productCategoryFormService: ProductCategoryFormService;
  let productCategoryService: ProductCategoryService;
  let productCategoryTypeService: ProductCategoryTypeService;

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

    fixture = TestBed.createComponent(ProductCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productCategoryFormService = TestBed.inject(ProductCategoryFormService);
    productCategoryService = TestBed.inject(ProductCategoryService);
    productCategoryTypeService = TestBed.inject(ProductCategoryTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ProductCategoryType query and add missing value', () => {
      const productCategory: IProductCategory = { id: 19244 };
      const productCategoryType: IProductCategoryType = { id: 10914 };
      productCategory.productCategoryType = productCategoryType;

      const productCategoryTypeCollection: IProductCategoryType[] = [{ id: 10914 }];
      vi.spyOn(productCategoryTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productCategoryTypeCollection })));
      const additionalProductCategoryTypes = [productCategoryType];
      const expectedCollection: IProductCategoryType[] = [...additionalProductCategoryTypes, ...productCategoryTypeCollection];
      vi.spyOn(productCategoryTypeService, 'addProductCategoryTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      expect(productCategoryTypeService.query).toHaveBeenCalled();
      expect(productCategoryTypeService.addProductCategoryTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productCategoryTypeCollection,
        ...additionalProductCategoryTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productCategoryTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductCategory query and add missing value', () => {
      const productCategory: IProductCategory = { id: 19244 };
      const parent: IProductCategory = { id: 29286 };
      productCategory.parent = parent;

      const productCategoryCollection: IProductCategory[] = [{ id: 29286 }];
      vi.spyOn(productCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productCategoryCollection })));
      const additionalProductCategories = [parent];
      const expectedCollection: IProductCategory[] = [...additionalProductCategories, ...productCategoryCollection];
      vi.spyOn(productCategoryService, 'addProductCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      expect(productCategoryService.query).toHaveBeenCalled();
      expect(productCategoryService.addProductCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productCategoryCollection,
        ...additionalProductCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productCategory: IProductCategory = { id: 19244 };
      const productCategoryType: IProductCategoryType = { id: 10914 };
      productCategory.productCategoryType = productCategoryType;
      const parent: IProductCategory = { id: 29286 };
      productCategory.parent = parent;

      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      expect(comp.productCategoryTypesSharedCollection()).toContainEqual(productCategoryType);
      expect(comp.productCategoriesSharedCollection()).toContainEqual(parent);
      expect(comp.productCategory).toEqual(productCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategory>();
      const productCategory = { id: 29286 };
      vi.spyOn(productCategoryFormService, 'getProductCategory').mockReturnValue(productCategory);
      vi.spyOn(productCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productCategory);
      saveSubject.complete();

      // THEN
      expect(productCategoryFormService.getProductCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(productCategory));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategory>();
      const productCategory = { id: 29286 };
      vi.spyOn(productCategoryFormService, 'getProductCategory').mockReturnValue({ id: null });
      vi.spyOn(productCategoryService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productCategory);
      saveSubject.complete();

      // THEN
      expect(productCategoryFormService.getProductCategory).toHaveBeenCalled();
      expect(productCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategory>();
      const productCategory = { id: 29286 };
      vi.spyOn(productCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductCategoryType', () => {
      it('should forward to productCategoryTypeService', () => {
        const entity = { id: 10914 };
        const entity2 = { id: 32619 };
        vi.spyOn(productCategoryTypeService, 'compareProductCategoryType');
        comp.compareProductCategoryType(entity, entity2);
        expect(productCategoryTypeService.compareProductCategoryType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductCategory', () => {
      it('should forward to productCategoryService', () => {
        const entity = { id: 29286 };
        const entity2 = { id: 19244 };
        vi.spyOn(productCategoryService, 'compareProductCategory');
        comp.compareProductCategory(entity, entity2);
        expect(productCategoryService.compareProductCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
