import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOrderAdjustmentType } from 'app/entities/order-adjustment-type/order-adjustment-type.model';
import { OrderAdjustmentTypeService } from 'app/entities/order-adjustment-type/service/order-adjustment-type.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { QuoteItemService } from 'app/entities/quote-item/service/quote-item.service';
import { TaxAuthorityService } from 'app/entities/tax-authority/service/tax-authority.service';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { IQuoteAdjustment } from '../quote-adjustment.model';
import { QuoteAdjustmentService } from '../service/quote-adjustment.service';

import { QuoteAdjustmentFormService } from './quote-adjustment-form.service';
import { QuoteAdjustmentUpdate } from './quote-adjustment-update';

describe('QuoteAdjustment Management Update Component', () => {
  let comp: QuoteAdjustmentUpdate;
  let fixture: ComponentFixture<QuoteAdjustmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let quoteAdjustmentFormService: QuoteAdjustmentFormService;
  let quoteAdjustmentService: QuoteAdjustmentService;
  let quoteService: QuoteService;
  let quoteItemService: QuoteItemService;
  let orderAdjustmentTypeService: OrderAdjustmentTypeService;
  let productService: ProductService;
  let productFeatureService: ProductFeatureService;
  let taxAuthorityService: TaxAuthorityService;

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

    fixture = TestBed.createComponent(QuoteAdjustmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteAdjustmentFormService = TestBed.inject(QuoteAdjustmentFormService);
    quoteAdjustmentService = TestBed.inject(QuoteAdjustmentService);
    quoteService = TestBed.inject(QuoteService);
    quoteItemService = TestBed.inject(QuoteItemService);
    orderAdjustmentTypeService = TestBed.inject(OrderAdjustmentTypeService);
    productService = TestBed.inject(ProductService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    taxAuthorityService = TestBed.inject(TaxAuthorityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Quote query and add missing value', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const quote: IQuote = { id: 10209 };
      quoteAdjustment.quote = quote;

      const quoteCollection: IQuote[] = [{ id: 10209 }];
      vi.spyOn(quoteService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteCollection })));
      const additionalQuotes = [quote];
      const expectedCollection: IQuote[] = [...additionalQuotes, ...quoteCollection];
      vi.spyOn(quoteService, 'addQuoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(quoteService.query).toHaveBeenCalled();
      expect(quoteService.addQuoteToCollectionIfMissing).toHaveBeenCalledWith(
        quoteCollection,
        ...additionalQuotes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call QuoteItem query and add missing value', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const quoteItem: IQuoteItem = { id: 28072 };
      quoteAdjustment.quoteItem = quoteItem;

      const quoteItemCollection: IQuoteItem[] = [{ id: 28072 }];
      vi.spyOn(quoteItemService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteItemCollection })));
      const additionalQuoteItems = [quoteItem];
      const expectedCollection: IQuoteItem[] = [...additionalQuoteItems, ...quoteItemCollection];
      vi.spyOn(quoteItemService, 'addQuoteItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(quoteItemService.query).toHaveBeenCalled();
      expect(quoteItemService.addQuoteItemToCollectionIfMissing).toHaveBeenCalledWith(
        quoteItemCollection,
        ...additionalQuoteItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quoteItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call OrderAdjustmentType query and add missing value', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const adjustmentType: IOrderAdjustmentType = { id: 1453 };
      quoteAdjustment.adjustmentType = adjustmentType;

      const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [{ id: 1453 }];
      vi.spyOn(orderAdjustmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: orderAdjustmentTypeCollection })));
      const additionalOrderAdjustmentTypes = [adjustmentType];
      const expectedCollection: IOrderAdjustmentType[] = [...additionalOrderAdjustmentTypes, ...orderAdjustmentTypeCollection];
      vi.spyOn(orderAdjustmentTypeService, 'addOrderAdjustmentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(orderAdjustmentTypeService.query).toHaveBeenCalled();
      expect(orderAdjustmentTypeService.addOrderAdjustmentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        orderAdjustmentTypeCollection,
        ...additionalOrderAdjustmentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderAdjustmentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const product: IProduct = { id: 21536 };
      quoteAdjustment.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const productFeature: IProductFeature = { id: 30957 };
      quoteAdjustment.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TaxAuthority query and add missing value', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const taxAuthority: ITaxAuthority = { id: 14408 };
      quoteAdjustment.taxAuthority = taxAuthority;

      const taxAuthorityCollection: ITaxAuthority[] = [{ id: 14408 }];
      vi.spyOn(taxAuthorityService, 'query').mockReturnValue(of(new HttpResponse({ body: taxAuthorityCollection })));
      const additionalTaxAuthorities = [taxAuthority];
      const expectedCollection: ITaxAuthority[] = [...additionalTaxAuthorities, ...taxAuthorityCollection];
      vi.spyOn(taxAuthorityService, 'addTaxAuthorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(taxAuthorityService.query).toHaveBeenCalled();
      expect(taxAuthorityService.addTaxAuthorityToCollectionIfMissing).toHaveBeenCalledWith(
        taxAuthorityCollection,
        ...additionalTaxAuthorities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.taxAuthoritiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quoteAdjustment: IQuoteAdjustment = { id: 6610 };
      const quote: IQuote = { id: 10209 };
      quoteAdjustment.quote = quote;
      const quoteItem: IQuoteItem = { id: 28072 };
      quoteAdjustment.quoteItem = quoteItem;
      const adjustmentType: IOrderAdjustmentType = { id: 1453 };
      quoteAdjustment.adjustmentType = adjustmentType;
      const product: IProduct = { id: 21536 };
      quoteAdjustment.product = product;
      const productFeature: IProductFeature = { id: 30957 };
      quoteAdjustment.productFeature = productFeature;
      const taxAuthority: ITaxAuthority = { id: 14408 };
      quoteAdjustment.taxAuthority = taxAuthority;

      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      expect(comp.quotesSharedCollection()).toContainEqual(quote);
      expect(comp.quoteItemsSharedCollection()).toContainEqual(quoteItem);
      expect(comp.orderAdjustmentTypesSharedCollection()).toContainEqual(adjustmentType);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.taxAuthoritiesSharedCollection()).toContainEqual(taxAuthority);
      expect(comp.quoteAdjustment).toEqual(quoteAdjustment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteAdjustment>();
      const quoteAdjustment = { id: 2656 };
      vi.spyOn(quoteAdjustmentFormService, 'getQuoteAdjustment').mockReturnValue(quoteAdjustment);
      vi.spyOn(quoteAdjustmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteAdjustment);
      saveSubject.complete();

      // THEN
      expect(quoteAdjustmentFormService.getQuoteAdjustment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteAdjustmentService.update).toHaveBeenCalledWith(expect.objectContaining(quoteAdjustment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteAdjustment>();
      const quoteAdjustment = { id: 2656 };
      vi.spyOn(quoteAdjustmentFormService, 'getQuoteAdjustment').mockReturnValue({ id: null });
      vi.spyOn(quoteAdjustmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteAdjustment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteAdjustment);
      saveSubject.complete();

      // THEN
      expect(quoteAdjustmentFormService.getQuoteAdjustment).toHaveBeenCalled();
      expect(quoteAdjustmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteAdjustment>();
      const quoteAdjustment = { id: 2656 };
      vi.spyOn(quoteAdjustmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteAdjustment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteAdjustmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuote', () => {
      it('should forward to quoteService', () => {
        const entity = { id: 10209 };
        const entity2 = { id: 23928 };
        vi.spyOn(quoteService, 'compareQuote');
        comp.compareQuote(entity, entity2);
        expect(quoteService.compareQuote).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuoteItem', () => {
      it('should forward to quoteItemService', () => {
        const entity = { id: 28072 };
        const entity2 = { id: 30045 };
        vi.spyOn(quoteItemService, 'compareQuoteItem');
        comp.compareQuoteItem(entity, entity2);
        expect(quoteItemService.compareQuoteItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrderAdjustmentType', () => {
      it('should forward to orderAdjustmentTypeService', () => {
        const entity = { id: 1453 };
        const entity2 = { id: 11072 };
        vi.spyOn(orderAdjustmentTypeService, 'compareOrderAdjustmentType');
        comp.compareOrderAdjustmentType(entity, entity2);
        expect(orderAdjustmentTypeService.compareOrderAdjustmentType).toHaveBeenCalledWith(entity, entity2);
      });
    });

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

    describe('compareTaxAuthority', () => {
      it('should forward to taxAuthorityService', () => {
        const entity = { id: 14408 };
        const entity2 = { id: 27648 };
        vi.spyOn(taxAuthorityService, 'compareTaxAuthority');
        comp.compareTaxAuthority(entity, entity2);
        expect(taxAuthorityService.compareTaxAuthority).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
