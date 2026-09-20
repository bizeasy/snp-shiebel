import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { IQuoteType } from 'app/entities/quote-type/quote-type.model';
import { QuoteTypeService } from 'app/entities/quote-type/service/quote-type.service';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { SalesChannelService } from 'app/entities/sales-channel/service/sales-channel.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { UomConversionService } from 'app/entities/uom-conversion/service/uom-conversion.service';
import { IUomConversion } from 'app/entities/uom-conversion/uom-conversion.model';
import { IQuote } from '../quote.model';
import { QuoteService } from '../service/quote.service';

import { QuoteFormService } from './quote-form.service';
import { QuoteUpdate } from './quote-update';

describe('Quote Management Update Component', () => {
  let comp: QuoteUpdate;
  let fixture: ComponentFixture<QuoteUpdate>;
  let activatedRoute: ActivatedRoute;
  let quoteFormService: QuoteFormService;
  let quoteService: QuoteService;
  let quoteTypeService: QuoteTypeService;
  let custRequestService: CustRequestService;
  let partyService: PartyService;
  let statusService: StatusService;
  let productStoreService: ProductStoreService;
  let salesChannelService: SalesChannelService;
  let uomService: UomService;
  let uomConversionService: UomConversionService;

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

    fixture = TestBed.createComponent(QuoteUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteFormService = TestBed.inject(QuoteFormService);
    quoteService = TestBed.inject(QuoteService);
    quoteTypeService = TestBed.inject(QuoteTypeService);
    custRequestService = TestBed.inject(CustRequestService);
    partyService = TestBed.inject(PartyService);
    statusService = TestBed.inject(StatusService);
    productStoreService = TestBed.inject(ProductStoreService);
    salesChannelService = TestBed.inject(SalesChannelService);
    uomService = TestBed.inject(UomService);
    uomConversionService = TestBed.inject(UomConversionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call QuoteType query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const quoteType: IQuoteType = { id: 19838 };
      quote.quoteType = quoteType;

      const quoteTypeCollection: IQuoteType[] = [{ id: 19838 }];
      vi.spyOn(quoteTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteTypeCollection })));
      const additionalQuoteTypes = [quoteType];
      const expectedCollection: IQuoteType[] = [...additionalQuoteTypes, ...quoteTypeCollection];
      vi.spyOn(quoteTypeService, 'addQuoteTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(quoteTypeService.query).toHaveBeenCalled();
      expect(quoteTypeService.addQuoteTypeToCollectionIfMissing).toHaveBeenCalledWith(
        quoteTypeCollection,
        ...additionalQuoteTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quoteTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustRequest query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const custRequest: ICustRequest = { id: 17760 };
      quote.custRequest = custRequest;

      const custRequestCollection: ICustRequest[] = [{ id: 17760 }];
      vi.spyOn(custRequestService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestCollection })));
      const additionalCustRequests = [custRequest];
      const expectedCollection: ICustRequest[] = [...additionalCustRequests, ...custRequestCollection];
      vi.spyOn(custRequestService, 'addCustRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(custRequestService.query).toHaveBeenCalled();
      expect(custRequestService.addCustRequestToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestCollection,
        ...additionalCustRequests.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const party: IParty = { id: 24527 };
      quote.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const status: IStatus = { id: 2927 };
      quote.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductStore query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const productStore: IProductStore = { id: 30593 };
      quote.productStore = productStore;

      const productStoreCollection: IProductStore[] = [{ id: 30593 }];
      vi.spyOn(productStoreService, 'query').mockReturnValue(of(new HttpResponse({ body: productStoreCollection })));
      const additionalProductStores = [productStore];
      const expectedCollection: IProductStore[] = [...additionalProductStores, ...productStoreCollection];
      vi.spyOn(productStoreService, 'addProductStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(productStoreService.query).toHaveBeenCalled();
      expect(productStoreService.addProductStoreToCollectionIfMissing).toHaveBeenCalledWith(
        productStoreCollection,
        ...additionalProductStores.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productStoresSharedCollection()).toEqual(expectedCollection);
    });

    it('should call SalesChannel query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const salesChannel: ISalesChannel = { id: 25587 };
      quote.salesChannel = salesChannel;

      const salesChannelCollection: ISalesChannel[] = [{ id: 25587 }];
      vi.spyOn(salesChannelService, 'query').mockReturnValue(of(new HttpResponse({ body: salesChannelCollection })));
      const additionalSalesChannels = [salesChannel];
      const expectedCollection: ISalesChannel[] = [...additionalSalesChannels, ...salesChannelCollection];
      vi.spyOn(salesChannelService, 'addSalesChannelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(salesChannelService.query).toHaveBeenCalled();
      expect(salesChannelService.addSalesChannelToCollectionIfMissing).toHaveBeenCalledWith(
        salesChannelCollection,
        ...additionalSalesChannels.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.salesChannelsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const currencyUom: IUom = { id: 6576 };
      quote.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call UomConversion query and add missing value', () => {
      const quote: IQuote = { id: 23928 };
      const currencyConversion: IUomConversion = { id: 13785 };
      quote.currencyConversion = currencyConversion;

      const uomConversionCollection: IUomConversion[] = [{ id: 13785 }];
      vi.spyOn(uomConversionService, 'query').mockReturnValue(of(new HttpResponse({ body: uomConversionCollection })));
      const additionalUomConversions = [currencyConversion];
      const expectedCollection: IUomConversion[] = [...additionalUomConversions, ...uomConversionCollection];
      vi.spyOn(uomConversionService, 'addUomConversionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(uomConversionService.query).toHaveBeenCalled();
      expect(uomConversionService.addUomConversionToCollectionIfMissing).toHaveBeenCalledWith(
        uomConversionCollection,
        ...additionalUomConversions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomConversionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quote: IQuote = { id: 23928 };
      const quoteType: IQuoteType = { id: 19838 };
      quote.quoteType = quoteType;
      const custRequest: ICustRequest = { id: 17760 };
      quote.custRequest = custRequest;
      const party: IParty = { id: 24527 };
      quote.party = party;
      const status: IStatus = { id: 2927 };
      quote.status = status;
      const productStore: IProductStore = { id: 30593 };
      quote.productStore = productStore;
      const salesChannel: ISalesChannel = { id: 25587 };
      quote.salesChannel = salesChannel;
      const currencyUom: IUom = { id: 6576 };
      quote.currencyUom = currencyUom;
      const currencyConversion: IUomConversion = { id: 13785 };
      quote.currencyConversion = currencyConversion;

      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      expect(comp.quoteTypesSharedCollection()).toContainEqual(quoteType);
      expect(comp.custRequestsSharedCollection()).toContainEqual(custRequest);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.productStoresSharedCollection()).toContainEqual(productStore);
      expect(comp.salesChannelsSharedCollection()).toContainEqual(salesChannel);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.uomConversionsSharedCollection()).toContainEqual(currencyConversion);
      expect(comp.quote).toEqual(quote);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuote>();
      const quote = { id: 10209 };
      vi.spyOn(quoteFormService, 'getQuote').mockReturnValue(quote);
      vi.spyOn(quoteService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quote);
      saveSubject.complete();

      // THEN
      expect(quoteFormService.getQuote).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteService.update).toHaveBeenCalledWith(expect.objectContaining(quote));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuote>();
      const quote = { id: 10209 };
      vi.spyOn(quoteFormService, 'getQuote').mockReturnValue({ id: null });
      vi.spyOn(quoteService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quote);
      saveSubject.complete();

      // THEN
      expect(quoteFormService.getQuote).toHaveBeenCalled();
      expect(quoteService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuote>();
      const quote = { id: 10209 };
      vi.spyOn(quoteService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuoteType', () => {
      it('should forward to quoteTypeService', () => {
        const entity = { id: 19838 };
        const entity2 = { id: 5628 };
        vi.spyOn(quoteTypeService, 'compareQuoteType');
        comp.compareQuoteType(entity, entity2);
        expect(quoteTypeService.compareQuoteType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareProductStore', () => {
      it('should forward to productStoreService', () => {
        const entity = { id: 30593 };
        const entity2 = { id: 24662 };
        vi.spyOn(productStoreService, 'compareProductStore');
        comp.compareProductStore(entity, entity2);
        expect(productStoreService.compareProductStore).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSalesChannel', () => {
      it('should forward to salesChannelService', () => {
        const entity = { id: 25587 };
        const entity2 = { id: 32514 };
        vi.spyOn(salesChannelService, 'compareSalesChannel');
        comp.compareSalesChannel(entity, entity2);
        expect(salesChannelService.compareSalesChannel).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareUomConversion', () => {
      it('should forward to uomConversionService', () => {
        const entity = { id: 13785 };
        const entity2 = { id: 18422 };
        vi.spyOn(uomConversionService, 'compareUomConversion');
        comp.compareUomConversion(entity, entity2);
        expect(uomConversionService.compareUomConversion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
