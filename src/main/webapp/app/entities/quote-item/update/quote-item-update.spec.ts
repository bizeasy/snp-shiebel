import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UomService } from 'app/entities/uom/service/uom.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { CustRequestItemService } from 'app/entities/cust-request-item/service/cust-request-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IQuoteItem } from '../quote-item.model';
import { QuoteItemService } from '../service/quote-item.service';

import { QuoteItemFormService } from './quote-item-form.service';
import { QuoteItemUpdate } from './quote-item-update';

describe('QuoteItem Management Update Component', () => {
  let comp: QuoteItemUpdate;
  let fixture: ComponentFixture<QuoteItemUpdate>;
  let activatedRoute: ActivatedRoute;
  let quoteItemFormService: QuoteItemFormService;
  let quoteItemService: QuoteItemService;
  let quoteService: QuoteService;
  let productService: ProductService;
  let productFeatureService: ProductFeatureService;
  let uomService: UomService;
  let statusService: StatusService;
  let custRequestService: CustRequestService;
  let custRequestItemService: CustRequestItemService;

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

    fixture = TestBed.createComponent(QuoteItemUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteItemFormService = TestBed.inject(QuoteItemFormService);
    quoteItemService = TestBed.inject(QuoteItemService);
    quoteService = TestBed.inject(QuoteService);
    productService = TestBed.inject(ProductService);
    productFeatureService = TestBed.inject(ProductFeatureService);
    uomService = TestBed.inject(UomService);
    statusService = TestBed.inject(StatusService);
    custRequestService = TestBed.inject(CustRequestService);
    custRequestItemService = TestBed.inject(CustRequestItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Quote query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const quote: IQuote = { id: 10209 };
      quoteItem.quote = quote;

      const quoteCollection: IQuote[] = [{ id: 10209 }];
      vi.spyOn(quoteService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteCollection })));
      const additionalQuotes = [quote];
      const expectedCollection: IQuote[] = [...additionalQuotes, ...quoteCollection];
      vi.spyOn(quoteService, 'addQuoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(quoteService.query).toHaveBeenCalled();
      expect(quoteService.addQuoteToCollectionIfMissing).toHaveBeenCalledWith(
        quoteCollection,
        ...additionalQuotes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Product query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const product: IProduct = { id: 21536 };
      quoteItem.product = product;

      const productCollection: IProduct[] = [{ id: 21536 }];
      vi.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      vi.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const productFeature: IProductFeature = { id: 30957 };
      quoteItem.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const uom: IUom = { id: 6576 };
      quoteItem.uom = uom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [uom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const status: IStatus = { id: 2927 };
      quoteItem.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustRequest query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const custRequest: ICustRequest = { id: 17760 };
      quoteItem.custRequest = custRequest;

      const custRequestCollection: ICustRequest[] = [{ id: 17760 }];
      vi.spyOn(custRequestService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestCollection })));
      const additionalCustRequests = [custRequest];
      const expectedCollection: ICustRequest[] = [...additionalCustRequests, ...custRequestCollection];
      vi.spyOn(custRequestService, 'addCustRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(custRequestService.query).toHaveBeenCalled();
      expect(custRequestService.addCustRequestToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestCollection,
        ...additionalCustRequests.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustRequestItem query and add missing value', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const custRequestItem: ICustRequestItem = { id: 17328 };
      quoteItem.custRequestItem = custRequestItem;

      const custRequestItemCollection: ICustRequestItem[] = [{ id: 17328 }];
      vi.spyOn(custRequestItemService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestItemCollection })));
      const additionalCustRequestItems = [custRequestItem];
      const expectedCollection: ICustRequestItem[] = [...additionalCustRequestItems, ...custRequestItemCollection];
      vi.spyOn(custRequestItemService, 'addCustRequestItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(custRequestItemService.query).toHaveBeenCalled();
      expect(custRequestItemService.addCustRequestItemToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestItemCollection,
        ...additionalCustRequestItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quoteItem: IQuoteItem = { id: 30045 };
      const quote: IQuote = { id: 10209 };
      quoteItem.quote = quote;
      const product: IProduct = { id: 21536 };
      quoteItem.product = product;
      const productFeature: IProductFeature = { id: 30957 };
      quoteItem.productFeature = productFeature;
      const uom: IUom = { id: 6576 };
      quoteItem.uom = uom;
      const status: IStatus = { id: 2927 };
      quoteItem.status = status;
      const custRequest: ICustRequest = { id: 17760 };
      quoteItem.custRequest = custRequest;
      const custRequestItem: ICustRequestItem = { id: 17328 };
      quoteItem.custRequestItem = custRequestItem;

      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      expect(comp.quotesSharedCollection()).toContainEqual(quote);
      expect(comp.productsSharedCollection()).toContainEqual(product);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.uomsSharedCollection()).toContainEqual(uom);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.custRequestsSharedCollection()).toContainEqual(custRequest);
      expect(comp.custRequestItemsSharedCollection()).toContainEqual(custRequestItem);
      expect(comp.quoteItem).toEqual(quoteItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteItem>();
      const quoteItem = { id: 28072 };
      vi.spyOn(quoteItemFormService, 'getQuoteItem').mockReturnValue(quoteItem);
      vi.spyOn(quoteItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteItem);
      saveSubject.complete();

      // THEN
      expect(quoteItemFormService.getQuoteItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteItemService.update).toHaveBeenCalledWith(expect.objectContaining(quoteItem));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteItem>();
      const quoteItem = { id: 28072 };
      vi.spyOn(quoteItemFormService, 'getQuoteItem').mockReturnValue({ id: null });
      vi.spyOn(quoteItemService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteItem);
      saveSubject.complete();

      // THEN
      expect(quoteItemFormService.getQuoteItem).toHaveBeenCalled();
      expect(quoteItemService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteItem>();
      const quoteItem = { id: 28072 };
      vi.spyOn(quoteItemService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteItemService.update).toHaveBeenCalled();
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

    describe('compareUom', () => {
      it('should forward to uomService', () => {
        const entity = { id: 6576 };
        const entity2 = { id: 20168 };
        vi.spyOn(uomService, 'compareUom');
        comp.compareUom(entity, entity2);
        expect(uomService.compareUom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustRequest', () => {
      it('should forward to custRequestService', () => {
        const entity = { id: 17760 };
        const entity2 = { id: 10199 };
        vi.spyOn(custRequestService, 'compareCustRequest');
        comp.compareCustRequest(entity, entity2);
        expect(custRequestService.compareCustRequest).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustRequestItem', () => {
      it('should forward to custRequestItemService', () => {
        const entity = { id: 17328 };
        const entity2 = { id: 23718 };
        vi.spyOn(custRequestItemService, 'compareCustRequestItem');
        comp.compareCustRequestItem(entity, entity2);
        expect(custRequestItemService.compareCustRequestItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
