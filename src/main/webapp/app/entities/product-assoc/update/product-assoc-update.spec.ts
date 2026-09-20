import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductAssocType } from 'app/entities/product-assoc-type/product-assoc-type.model';
import { ProductAssocTypeService } from 'app/entities/product-assoc-type/service/product-assoc-type.service';
import { IProductAssoc } from '../product-assoc.model';
import { ProductAssocService } from '../service/product-assoc.service';

import { ProductAssocFormService } from './product-assoc-form.service';
import { ProductAssocUpdate } from './product-assoc-update';

describe('ProductAssoc Management Update Component', () => {
  let comp: ProductAssocUpdate;
  let fixture: ComponentFixture<ProductAssocUpdate>;
  let activatedRoute: ActivatedRoute;
  let productAssocFormService: ProductAssocFormService;
  let productAssocService: ProductAssocService;
  let productService: ProductService;
  let productAssocTypeService: ProductAssocTypeService;

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

    fixture = TestBed.createComponent(ProductAssocUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productAssocFormService = TestBed.inject(ProductAssocFormService);
    productAssocService = TestBed.inject(ProductAssocService);
    productService = TestBed.inject(ProductService);
    productAssocTypeService = TestBed.inject(ProductAssocTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productAssoc: IProductAssoc = { id: 19160 };
      const product: IProduct = { id: 21536 };
      productAssoc.product = product;
      const productTo: IProduct = { id: 21536 };
      productAssoc.productTo = productTo;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product, productTo];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productAssoc });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductAssocType query and add missing value', () => {
      const productAssoc: IProductAssoc = { id: 19160 };
      const productAssocType: IProductAssocType = { id: 15174 };
      productAssoc.productAssocType = productAssocType;

      const productAssocTypeCollection: IProductAssocType[] = [{ id: 15174 }];
      vi.spyOn(productAssocTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productAssocTypeCollection })));
      const additionalProductAssocTypes = [productAssocType];
      const expectedCollection: IProductAssocType[] = [...additionalProductAssocTypes, ...productAssocTypeCollection];
      vi.spyOn(productAssocTypeService, 'addProductAssocTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productAssoc });
      comp.ngOnInit();

      expect(productAssocTypeService.query).toHaveBeenCalled();
      expect(productAssocTypeService.addProductAssocTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productAssocTypeCollection,
        ...additionalProductAssocTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productAssocTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productAssoc: IProductAssoc = { id: 19160 };
      const product: IProduct = { id: 21536 };
      productAssoc.product = product;
      const productTo: IProduct = { id: 21536 };
      productAssoc.productTo = productTo;
      const productAssocType: IProductAssocType = { id: 15174 };
      productAssoc.productAssocType = productAssocType;

      activatedRoute.data = of({ productAssoc });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productsSharedCollection()).toContainEqual(productTo);
      expect(comp.productAssocTypesSharedCollection()).toContainEqual(productAssocType);
      expect(comp.productAssoc).toEqual(productAssoc);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAssoc>();
      const productAssoc = { id: 20189 };
      vi.spyOn(productAssocFormService, 'getProductAssoc').mockReturnValue(productAssoc);
      vi.spyOn(productAssocService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAssoc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productAssoc);
      saveSubject.complete();

      // THEN
      expect(productAssocFormService.getProductAssoc).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productAssocService.update).toHaveBeenCalledWith(expect.objectContaining(productAssoc));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAssoc>();
      const productAssoc = { id: 20189 };
      vi.spyOn(productAssocFormService, 'getProductAssoc').mockReturnValue({ id: null });
      vi.spyOn(productAssocService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAssoc: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productAssoc);
      saveSubject.complete();

      // THEN
      expect(productAssocFormService.getProductAssoc).toHaveBeenCalled();
      expect(productAssocService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAssoc>();
      const productAssoc = { id: 20189 };
      vi.spyOn(productAssocService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAssoc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productAssocService.update).toHaveBeenCalled();
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

    describe('compareProductAssocType', () => {
      it('should forward to productAssocTypeService', () => {
        const entity = { id: 15174 };
        const entity2 = { id: 4250 };
        vi.spyOn(productAssocTypeService, 'compareProductAssocType');
        comp.compareProductAssocType(entity, entity2);
        expect(productAssocTypeService.compareProductAssocType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
