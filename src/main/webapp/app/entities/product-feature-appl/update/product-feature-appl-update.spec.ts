import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IProductFeatureApplType } from 'app/entities/product-feature-appl-type/product-feature-appl-type.model';
import { ProductFeatureApplTypeService } from 'app/entities/product-feature-appl-type/service/product-feature-appl-type.service';
import { IProductFeatureAppl } from '../product-feature-appl.model';
import { ProductFeatureApplService } from '../service/product-feature-appl.service';

import { ProductFeatureApplFormService } from './product-feature-appl-form.service';
import { ProductFeatureApplUpdate } from './product-feature-appl-update';

describe('ProductFeatureAppl Management Update Component', () => {
  let comp: ProductFeatureApplUpdate;
  let fixture: ComponentFixture<ProductFeatureApplUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureApplFormService: ProductFeatureApplFormService;
  let productFeatureApplService: ProductFeatureApplService;
  let productService: ProductService;
  let productFeatureService: ProductFeatureService;
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

    fixture = TestBed.createComponent(ProductFeatureApplUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureApplFormService = TestBed.inject(ProductFeatureApplFormService);
    productFeatureApplService = TestBed.inject(ProductFeatureApplService);
    productService = TestBed.inject(ProductService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    productFeatureApplTypeService = TestBed.inject(ProductFeatureApplTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productFeatureAppl: IProductFeatureAppl = { id: 1945 };
      const product: IProduct = { id: 21536 };
      productFeatureAppl.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureAppl });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const productFeatureAppl: IProductFeatureAppl = { id: 1945 };
      const productFeature: IProductFeature = { id: 30957 };
      productFeatureAppl.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureAppl });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeatureApplType query and add missing value', () => {
      const productFeatureAppl: IProductFeatureAppl = { id: 1945 };
      const featureApplType: IProductFeatureApplType = { id: 6261 };
      productFeatureAppl.featureApplType = featureApplType;

      const productFeatureApplTypeCollection: IProductFeatureApplType[] = [{ id: 6261 }];
      vi.spyOn(productFeatureApplTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureApplTypeCollection })));
      const additionalProductFeatureApplTypes = [featureApplType];
      const expectedCollection: IProductFeatureApplType[] = [...additionalProductFeatureApplTypes, ...productFeatureApplTypeCollection];
      vi.spyOn(productFeatureApplTypeService, 'addProductFeatureApplTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureAppl });
      comp.ngOnInit();

      expect(productFeatureApplTypeService.query).toHaveBeenCalled();
      expect(productFeatureApplTypeService.addProductFeatureApplTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureApplTypeCollection,
        ...additionalProductFeatureApplTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeatureApplTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFeatureAppl: IProductFeatureAppl = { id: 1945 };
      const product: IProduct = { id: 21536 };
      productFeatureAppl.product = product;
      const productFeature: IProductFeature = { id: 30957 };
      productFeatureAppl.productFeature = productFeature;
      const featureApplType: IProductFeatureApplType = { id: 6261 };
      productFeatureAppl.featureApplType = featureApplType;

      activatedRoute.data = of({ productFeatureAppl });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.productFeatureApplTypesSharedCollection()).toContainEqual(featureApplType);
      expect(comp.productFeatureAppl).toEqual(productFeatureAppl);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureAppl>();
      const productFeatureAppl = { id: 32420 };
      vi.spyOn(productFeatureApplFormService, 'getProductFeatureAppl').mockReturnValue(productFeatureAppl);
      vi.spyOn(productFeatureApplService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureAppl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureAppl);
      saveSubject.complete();

      // THEN
      expect(productFeatureApplFormService.getProductFeatureAppl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureApplService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureAppl));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureAppl>();
      const productFeatureAppl = { id: 32420 };
      vi.spyOn(productFeatureApplFormService, 'getProductFeatureAppl').mockReturnValue({ id: null });
      vi.spyOn(productFeatureApplService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureAppl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureAppl);
      saveSubject.complete();

      // THEN
      expect(productFeatureApplFormService.getProductFeatureAppl).toHaveBeenCalled();
      expect(productFeatureApplService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureAppl>();
      const productFeatureAppl = { id: 32420 };
      vi.spyOn(productFeatureApplService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureAppl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureApplService.update).toHaveBeenCalled();
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

    describe('compareProductFeature', () => {
      it('should forward to productFeatureService', () => {
        const entity = { id: 30957 };
        const entity2 = { id: 7728 };
        vi.spyOn(productFeatureService, 'compareProductFeature');
        comp.compareProductFeature(entity, entity2);
        expect(productFeatureService.compareProductFeature).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductFeatureApplType', () => {
      it('should forward to productFeatureApplTypeService', () => {
        const entity = { id: 6261 };
        const entity2 = { id: 19218 };
        vi.spyOn(productFeatureApplTypeService, 'compareProductFeatureApplType');
        comp.compareProductFeatureApplType(entity, entity2);
        expect(productFeatureApplTypeService.compareProductFeatureApplType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
