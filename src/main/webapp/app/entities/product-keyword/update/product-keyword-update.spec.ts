import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IKeywordType } from 'app/entities/keyword-type/keyword-type.model';
import { KeywordTypeService } from 'app/entities/keyword-type/service/keyword-type.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductKeyword } from '../product-keyword.model';
import { ProductKeywordService } from '../service/product-keyword.service';

import { ProductKeywordFormService } from './product-keyword-form.service';
import { ProductKeywordUpdate } from './product-keyword-update';

describe('ProductKeyword Management Update Component', () => {
  let comp: ProductKeywordUpdate;
  let fixture: ComponentFixture<ProductKeywordUpdate>;
  let activatedRoute: ActivatedRoute;
  let productKeywordFormService: ProductKeywordFormService;
  let productKeywordService: ProductKeywordService;
  let productService: ProductService;
  let keywordTypeService: KeywordTypeService;

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

    fixture = TestBed.createComponent(ProductKeywordUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productKeywordFormService = TestBed.inject(ProductKeywordFormService);
    productKeywordService = TestBed.inject(ProductKeywordService);
    productService = TestBed.inject(ProductService);
    keywordTypeService = TestBed.inject(KeywordTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productKeyword: IProductKeyword = { id: 12434 };
      const product: IProduct = { id: 21536 };
      productKeyword.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productKeyword });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call KeywordType query and add missing value', () => {
      const productKeyword: IProductKeyword = { id: 12434 };
      const keywordType: IKeywordType = { id: 27532 };
      productKeyword.keywordType = keywordType;

      const keywordTypeCollection: IKeywordType[] = [{ id: 27532 }];
      vi.spyOn(keywordTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: keywordTypeCollection })));
      const additionalKeywordTypes = [keywordType];
      const expectedCollection: IKeywordType[] = [...additionalKeywordTypes, ...keywordTypeCollection];
      vi.spyOn(keywordTypeService, 'addKeywordTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productKeyword });
      comp.ngOnInit();

      expect(keywordTypeService.query).toHaveBeenCalled();
      expect(keywordTypeService.addKeywordTypeToCollectionIfMissing).toHaveBeenCalledWith(
        keywordTypeCollection,
        ...additionalKeywordTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.keywordTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productKeyword: IProductKeyword = { id: 12434 };
      const product: IProduct = { id: 21536 };
      productKeyword.product = product;
      const keywordType: IKeywordType = { id: 27532 };
      productKeyword.keywordType = keywordType;

      activatedRoute.data = of({ productKeyword });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.keywordTypesSharedCollection()).toContainEqual(keywordType);
      expect(comp.productKeyword).toEqual(productKeyword);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductKeyword>();
      const productKeyword = { id: 14945 };
      vi.spyOn(productKeywordFormService, 'getProductKeyword').mockReturnValue(productKeyword);
      vi.spyOn(productKeywordService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productKeyword });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productKeyword);
      saveSubject.complete();

      // THEN
      expect(productKeywordFormService.getProductKeyword).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productKeywordService.update).toHaveBeenCalledWith(expect.objectContaining(productKeyword));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductKeyword>();
      const productKeyword = { id: 14945 };
      vi.spyOn(productKeywordFormService, 'getProductKeyword').mockReturnValue({ id: null });
      vi.spyOn(productKeywordService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productKeyword: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productKeyword);
      saveSubject.complete();

      // THEN
      expect(productKeywordFormService.getProductKeyword).toHaveBeenCalled();
      expect(productKeywordService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductKeyword>();
      const productKeyword = { id: 14945 };
      vi.spyOn(productKeywordService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productKeyword });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productKeywordService.update).toHaveBeenCalled();
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

    describe('compareKeywordType', () => {
      it('should forward to keywordTypeService', () => {
        const entity = { id: 27532 };
        const entity2 = { id: 31809 };
        vi.spyOn(keywordTypeService, 'compareKeywordType');
        comp.compareKeywordType(entity, entity2);
        expect(keywordTypeService.compareKeywordType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
