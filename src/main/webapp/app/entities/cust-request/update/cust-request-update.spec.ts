import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustRequestCategory } from 'app/entities/cust-request-category/cust-request-category.model';
import { CustRequestCategoryService } from 'app/entities/cust-request-category/service/cust-request-category.service';
import { ICustRequestType } from 'app/entities/cust-request-type/cust-request-type.model';
import { CustRequestTypeService } from 'app/entities/cust-request-type/service/cust-request-type.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { SalesChannelService } from 'app/entities/sales-channel/service/sales-channel.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { ICustRequest } from '../cust-request.model';
import { CustRequestService } from '../service/cust-request.service';

import { CustRequestFormService } from './cust-request-form.service';
import { CustRequestUpdate } from './cust-request-update';

describe('CustRequest Management Update Component', () => {
  let comp: CustRequestUpdate;
  let fixture: ComponentFixture<CustRequestUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestFormService: CustRequestFormService;
  let custRequestService: CustRequestService;
  let custRequestTypeService: CustRequestTypeService;
  let custRequestCategoryService: CustRequestCategoryService;
  let statusService: StatusService;
  let partyService: PartyService;
  let productStoreService: ProductStoreService;
  let salesChannelService: SalesChannelService;
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

    fixture = TestBed.createComponent(CustRequestUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestFormService = TestBed.inject(CustRequestFormService);
    custRequestService = TestBed.inject(CustRequestService);
    custRequestTypeService = TestBed.inject(CustRequestTypeService);
    custRequestCategoryService = TestBed.inject(CustRequestCategoryService);
    statusService = TestBed.inject(StatusService);
    partyService = TestBed.inject(PartyService);
    productStoreService = TestBed.inject(ProductStoreService);
    salesChannelService = TestBed.inject(SalesChannelService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequestType query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const custRequestType: ICustRequestType = { id: 4424 };
      custRequest.custRequestType = custRequestType;

      const custRequestTypeCollection: ICustRequestType[] = [{ id: 4424 }];
      vi.spyOn(custRequestTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestTypeCollection })));
      const additionalCustRequestTypes = [custRequestType];
      const expectedCollection: ICustRequestType[] = [...additionalCustRequestTypes, ...custRequestTypeCollection];
      vi.spyOn(custRequestTypeService, 'addCustRequestTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(custRequestTypeService.query).toHaveBeenCalled();
      expect(custRequestTypeService.addCustRequestTypeToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestTypeCollection,
        ...additionalCustRequestTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustRequestCategory query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const custRequestCategory: ICustRequestCategory = { id: 30459 };
      custRequest.custRequestCategory = custRequestCategory;

      const custRequestCategoryCollection: ICustRequestCategory[] = [{ id: 30459 }];
      vi.spyOn(custRequestCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestCategoryCollection })));
      const additionalCustRequestCategories = [custRequestCategory];
      const expectedCollection: ICustRequestCategory[] = [...additionalCustRequestCategories, ...custRequestCategoryCollection];
      vi.spyOn(custRequestCategoryService, 'addCustRequestCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(custRequestCategoryService.query).toHaveBeenCalled();
      expect(custRequestCategoryService.addCustRequestCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestCategoryCollection,
        ...additionalCustRequestCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const status: IStatus = { id: 2927 };
      custRequest.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const fromParty: IParty = { id: 24527 };
      custRequest.fromParty = fromParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [fromParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductStore query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const productStore: IProductStore = { id: 30593 };
      custRequest.productStore = productStore;

      const productStoreCollection: IProductStore[] = [{ id: 30593 }];
      vi.spyOn(productStoreService, 'query').mockReturnValue(of(new HttpResponse({ body: productStoreCollection })));
      const additionalProductStores = [productStore];
      const expectedCollection: IProductStore[] = [...additionalProductStores, ...productStoreCollection];
      vi.spyOn(productStoreService, 'addProductStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(productStoreService.query).toHaveBeenCalled();
      expect(productStoreService.addProductStoreToCollectionIfMissing).toHaveBeenCalledWith(
        productStoreCollection,
        ...additionalProductStores.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productStoresSharedCollection()).toEqual(expectedCollection);
    });

    it('should call SalesChannel query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const salesChannel: ISalesChannel = { id: 25587 };
      custRequest.salesChannel = salesChannel;

      const salesChannelCollection: ISalesChannel[] = [{ id: 25587 }];
      vi.spyOn(salesChannelService, 'query').mockReturnValue(of(new HttpResponse({ body: salesChannelCollection })));
      const additionalSalesChannels = [salesChannel];
      const expectedCollection: ISalesChannel[] = [...additionalSalesChannels, ...salesChannelCollection];
      vi.spyOn(salesChannelService, 'addSalesChannelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(salesChannelService.query).toHaveBeenCalled();
      expect(salesChannelService.addSalesChannelToCollectionIfMissing).toHaveBeenCalledWith(
        salesChannelCollection,
        ...additionalSalesChannels.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.salesChannelsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const currencyUom: IUom = { id: 6576 };
      custRequest.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequest: ICustRequest = { id: 10199 };
      const custRequestType: ICustRequestType = { id: 4424 };
      custRequest.custRequestType = custRequestType;
      const custRequestCategory: ICustRequestCategory = { id: 30459 };
      custRequest.custRequestCategory = custRequestCategory;
      const status: IStatus = { id: 2927 };
      custRequest.status = status;
      const fromParty: IParty = { id: 24527 };
      custRequest.fromParty = fromParty;
      const productStore: IProductStore = { id: 30593 };
      custRequest.productStore = productStore;
      const salesChannel: ISalesChannel = { id: 25587 };
      custRequest.salesChannel = salesChannel;
      const currencyUom: IUom = { id: 6576 };
      custRequest.currencyUom = currencyUom;

      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      expect(comp.custRequestTypesSharedCollection()).toContainEqual(custRequestType);
      expect(comp.custRequestCategoriesSharedCollection()).toContainEqual(custRequestCategory);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.partiesSharedCollection()).toContainEqual(fromParty);
      expect(comp.productStoresSharedCollection()).toContainEqual(productStore);
      expect(comp.salesChannelsSharedCollection()).toContainEqual(salesChannel);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.custRequest).toEqual(custRequest);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequest>();
      const custRequest = { id: 17760 };
      vi.spyOn(custRequestFormService, 'getCustRequest').mockReturnValue(custRequest);
      vi.spyOn(custRequestService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequest);
      saveSubject.complete();

      // THEN
      expect(custRequestFormService.getCustRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestService.update).toHaveBeenCalledWith(expect.objectContaining(custRequest));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequest>();
      const custRequest = { id: 17760 };
      vi.spyOn(custRequestFormService, 'getCustRequest').mockReturnValue({ id: null });
      vi.spyOn(custRequestService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequest);
      saveSubject.complete();

      // THEN
      expect(custRequestFormService.getCustRequest).toHaveBeenCalled();
      expect(custRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequest>();
      const custRequest = { id: 17760 };
      vi.spyOn(custRequestService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequestType', () => {
      it('should forward to custRequestTypeService', () => {
        const entity = { id: 4424 };
        const entity2 = { id: 951 };
        vi.spyOn(custRequestTypeService, 'compareCustRequestType');
        comp.compareCustRequestType(entity, entity2);
        expect(custRequestTypeService.compareCustRequestType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustRequestCategory', () => {
      it('should forward to custRequestCategoryService', () => {
        const entity = { id: 30459 };
        const entity2 = { id: 7246 };
        vi.spyOn(custRequestCategoryService, 'compareCustRequestCategory');
        comp.compareCustRequestCategory(entity, entity2);
        expect(custRequestCategoryService.compareCustRequestCategory).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
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
  });
});
