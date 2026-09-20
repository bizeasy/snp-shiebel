import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductPricePurpose } from 'app/entities/product-price-purpose/product-price-purpose.model';
import { ProductPricePurposeService } from 'app/entities/product-price-purpose/service/product-price-purpose.service';
import { IProductPriceType } from 'app/entities/product-price-type/product-price-type.model';
import { ProductPriceTypeService } from 'app/entities/product-price-type/service/product-price-type.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IProductPrice } from '../product-price.model';
import { ProductPriceService } from '../service/product-price.service';

import { ProductPriceFormService } from './product-price-form.service';
import { ProductPriceUpdate } from './product-price-update';

describe('ProductPrice Management Update Component', () => {
  let comp: ProductPriceUpdate;
  let fixture: ComponentFixture<ProductPriceUpdate>;
  let activatedRoute: ActivatedRoute;
  let productPriceFormService: ProductPriceFormService;
  let productPriceService: ProductPriceService;
  let productService: ProductService;
  let productPriceTypeService: ProductPriceTypeService;
  let productPricePurposeService: ProductPricePurposeService;
  let uomService: UomService;

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

    fixture = TestBed.createComponent(ProductPriceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productPriceFormService = TestBed.inject(ProductPriceFormService);
    productPriceService = TestBed.inject(ProductPriceService);
    productService = TestBed.inject(ProductService);
    productPriceTypeService = TestBed.inject(ProductPriceTypeService);
    productPricePurposeService = TestBed.inject(ProductPricePurposeService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productPrice: IProductPrice = { id: 1931 };
      const product: IProduct = { id: 21536 };
      productPrice.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductPriceType query and add missing value', () => {
      const productPrice: IProductPrice = { id: 1931 };
      const productPriceType: IProductPriceType = { id: 18810 };
      productPrice.productPriceType = productPriceType;

      const productPriceTypeCollection: IProductPriceType[] = [{ id: 18810 }];
      vi.spyOn(productPriceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: productPriceTypeCollection })));
      const additionalProductPriceTypes = [productPriceType];
      const expectedCollection: IProductPriceType[] = [...additionalProductPriceTypes, ...productPriceTypeCollection];
      vi.spyOn(productPriceTypeService, 'addProductPriceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(productPriceTypeService.query).toHaveBeenCalled();
      expect(productPriceTypeService.addProductPriceTypeToCollectionIfMissing).toHaveBeenCalledWith(
        productPriceTypeCollection,
        ...additionalProductPriceTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productPriceTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductPricePurpose query and add missing value', () => {
      const productPrice: IProductPrice = { id: 1931 };
      const productPricePurpose: IProductPricePurpose = { id: 8537 };
      productPrice.productPricePurpose = productPricePurpose;

      const productPricePurposeCollection: IProductPricePurpose[] = [{ id: 8537 }];
      vi.spyOn(productPricePurposeService, 'query').mockReturnValue(of(new HttpResponse({ body: productPricePurposeCollection })));
      const additionalProductPricePurposes = [productPricePurpose];
      const expectedCollection: IProductPricePurpose[] = [...additionalProductPricePurposes, ...productPricePurposeCollection];
      vi.spyOn(productPricePurposeService, 'addProductPricePurposeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(productPricePurposeService.query).toHaveBeenCalled();
      expect(productPricePurposeService.addProductPricePurposeToCollectionIfMissing).toHaveBeenCalledWith(
        productPricePurposeCollection,
        ...additionalProductPricePurposes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productPricePurposesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const productPrice: IProductPrice = { id: 1931 };
      const currencyUom: IUom = { id: 6576 };
      productPrice.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productPrice: IProductPrice = { id: 1931 };
      const product: IProduct = { id: 21536 };
      productPrice.product = product;
      const productPriceType: IProductPriceType = { id: 18810 };
      productPrice.productPriceType = productPriceType;
      const productPricePurpose: IProductPricePurpose = { id: 8537 };
      productPrice.productPricePurpose = productPricePurpose;
      const currencyUom: IUom = { id: 6576 };
      productPrice.currencyUom = currencyUom;

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productPriceTypesSharedCollection()).toContainEqual(productPriceType);
      expect(comp.productPricePurposesSharedCollection()).toContainEqual(productPricePurpose);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.productPrice).toEqual(productPrice);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPrice>();
      const productPrice = { id: 28388 };
      vi.spyOn(productPriceFormService, 'getProductPrice').mockReturnValue(productPrice);
      vi.spyOn(productPriceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productPrice);
      saveSubject.complete();

      // THEN
      expect(productPriceFormService.getProductPrice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productPriceService.update).toHaveBeenCalledWith(expect.objectContaining(productPrice));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPrice>();
      const productPrice = { id: 28388 };
      vi.spyOn(productPriceFormService, 'getProductPrice').mockReturnValue({ id: null });
      vi.spyOn(productPriceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPrice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productPrice);
      saveSubject.complete();

      // THEN
      expect(productPriceFormService.getProductPrice).toHaveBeenCalled();
      expect(productPriceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPrice>();
      const productPrice = { id: 28388 };
      vi.spyOn(productPriceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productPriceService.update).toHaveBeenCalled();
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

    describe('compareProductPriceType', () => {
      it('should forward to productPriceTypeService', () => {
        const entity = { id: 18810 };
        const entity2 = { id: 31296 };
        vi.spyOn(productPriceTypeService, 'compareProductPriceType');
        comp.compareProductPriceType(entity, entity2);
        expect(productPriceTypeService.compareProductPriceType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductPricePurpose', () => {
      it('should forward to productPricePurposeService', () => {
        const entity = { id: 8537 };
        const entity2 = { id: 15993 };
        vi.spyOn(productPricePurposeService, 'compareProductPricePurpose');
        comp.compareProductPricePurpose(entity, entity2);
        expect(productPricePurposeService.compareProductPricePurpose).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
