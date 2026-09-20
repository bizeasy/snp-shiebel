import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { IProductCategoryMember } from '../product-category-member.model';
import { ProductCategoryMemberService } from '../service/product-category-member.service';

import { ProductCategoryMemberFormService } from './product-category-member-form.service';
import { ProductCategoryMemberUpdate } from './product-category-member-update';

describe('ProductCategoryMember Management Update Component', () => {
  let comp: ProductCategoryMemberUpdate;
  let fixture: ComponentFixture<ProductCategoryMemberUpdate>;
  let activatedRoute: ActivatedRoute;
  let productCategoryMemberFormService: ProductCategoryMemberFormService;
  let productCategoryMemberService: ProductCategoryMemberService;
  let productService: ProductService;
  let productCategoryService: ProductCategoryService;

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

    fixture = TestBed.createComponent(ProductCategoryMemberUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productCategoryMemberFormService = TestBed.inject(ProductCategoryMemberFormService);
    productCategoryMemberService = TestBed.inject(ProductCategoryMemberService);
    productService = TestBed.inject(ProductService);
    productCategoryService = TestBed.inject(ProductCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productCategoryMember: IProductCategoryMember = { id: 337 };
      const product: IProduct = { id: 21536 };
      productCategoryMember.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCategoryMember });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductCategory query and add missing value', () => {
      const productCategoryMember: IProductCategoryMember = { id: 337 };
      const productCategory: IProductCategory = { id: 29286 };
      productCategoryMember.productCategory = productCategory;

      const productCategoryCollection: IProductCategory[] = [{ id: 29286 }];
      vi.spyOn(productCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productCategoryCollection })));
      const additionalProductCategories = [productCategory];
      const expectedCollection: IProductCategory[] = [...additionalProductCategories, ...productCategoryCollection];
      vi.spyOn(productCategoryService, 'addProductCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCategoryMember });
      comp.ngOnInit();

      expect(productCategoryService.query).toHaveBeenCalled();
      expect(productCategoryService.addProductCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productCategoryCollection,
        ...additionalProductCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productCategoryMember: IProductCategoryMember = { id: 337 };
      const product: IProduct = { id: 21536 };
      productCategoryMember.product = product;
      const productCategory: IProductCategory = { id: 29286 };
      productCategoryMember.productCategory = productCategory;

      activatedRoute.data = of({ productCategoryMember });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productCategoriesSharedCollection()).toContainEqual(productCategory);
      expect(comp.productCategoryMember).toEqual(productCategoryMember);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategoryMember>();
      const productCategoryMember = { id: 24718 };
      vi.spyOn(productCategoryMemberFormService, 'getProductCategoryMember').mockReturnValue(productCategoryMember);
      vi.spyOn(productCategoryMemberService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategoryMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productCategoryMember);
      saveSubject.complete();

      // THEN
      expect(productCategoryMemberFormService.getProductCategoryMember).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productCategoryMemberService.update).toHaveBeenCalledWith(expect.objectContaining(productCategoryMember));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategoryMember>();
      const productCategoryMember = { id: 24718 };
      vi.spyOn(productCategoryMemberFormService, 'getProductCategoryMember').mockReturnValue({ id: null });
      vi.spyOn(productCategoryMemberService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategoryMember: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productCategoryMember);
      saveSubject.complete();

      // THEN
      expect(productCategoryMemberFormService.getProductCategoryMember).toHaveBeenCalled();
      expect(productCategoryMemberService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategoryMember>();
      const productCategoryMember = { id: 24718 };
      vi.spyOn(productCategoryMemberService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategoryMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productCategoryMemberService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        vi.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
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
