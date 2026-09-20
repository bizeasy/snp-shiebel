import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustomTimePeriod } from 'app/entities/custom-time-period/custom-time-period.model';
import { CustomTimePeriodService } from 'app/entities/custom-time-period/service/custom-time-period.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IOrderType } from 'app/entities/order-type/order-type.model';
import { OrderTypeService } from 'app/entities/order-type/service/order-type.service';
import { IParty } from 'app/entities/party/party.model';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { SalesChannelService } from 'app/entities/sales-channel/service/sales-channel.service';
import { PartyService } from 'app/entities/party/service/party.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { UomConversionService } from 'app/entities/uom-conversion/service/uom-conversion.service';
import { IUomConversion } from 'app/entities/uom-conversion/uom-conversion.model';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';

import { OrderFormService } from './order-form.service';
import { OrderUpdate } from './order-update';

describe('Order Management Update Component', () => {
  let comp: OrderUpdate;
  let fixture: ComponentFixture<OrderUpdate>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let orderTypeService: OrderTypeService;
  let salesChannelService: SalesChannelService;
  let partyService: PartyService;
  let statusService: StatusService;
  let uomService: UomService;
  let uomConversionService: UomConversionService;
  let quoteService: QuoteService;
  let facilityService: FacilityService;
  let productStoreService: ProductStoreService;
  let departmentService: DepartmentService;
  let customTimePeriodService: CustomTimePeriodService;

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

    fixture = TestBed.createComponent(OrderUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    orderTypeService = TestBed.inject(OrderTypeService);
    salesChannelService = TestBed.inject(SalesChannelService);
    partyService = TestBed.inject(PartyService);
    statusService = TestBed.inject(StatusService);
    uomService = TestBed.inject(UomService);
    uomConversionService = TestBed.inject(UomConversionService);
    quoteService = TestBed.inject(QuoteService);
    facilityService = TestBed.inject(FacilityService);
    productStoreService = TestBed.inject(ProductStoreService);
    departmentService = TestBed.inject(DepartmentService);
    customTimePeriodService = TestBed.inject(CustomTimePeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OrderType query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const orderType: IOrderType = { id: 10129 };
      order.orderType = orderType;

      const orderTypeCollection: IOrderType[] = [{ id: 10129 }];
      vi.spyOn(orderTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: orderTypeCollection })));
      const additionalOrderTypes = [orderType];
      const expectedCollection: IOrderType[] = [...additionalOrderTypes, ...orderTypeCollection];
      vi.spyOn(orderTypeService, 'addOrderTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(orderTypeService.query).toHaveBeenCalled();
      expect(orderTypeService.addOrderTypeToCollectionIfMissing).toHaveBeenCalledWith(
        orderTypeCollection,
        ...additionalOrderTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.orderTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call SalesChannel query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const salesChannel: ISalesChannel = { id: 25587 };
      order.salesChannel = salesChannel;

      const salesChannelCollection: ISalesChannel[] = [{ id: 25587 }];
      vi.spyOn(salesChannelService, 'query').mockReturnValue(of(new HttpResponse({ body: salesChannelCollection })));
      const additionalSalesChannels = [salesChannel];
      const expectedCollection: ISalesChannel[] = [...additionalSalesChannels, ...salesChannelCollection];
      vi.spyOn(salesChannelService, 'addSalesChannelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(salesChannelService.query).toHaveBeenCalled();
      expect(salesChannelService.addSalesChannelToCollectionIfMissing).toHaveBeenCalledWith(
        salesChannelCollection,
        ...additionalSalesChannels.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.salesChannelsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const party: IParty = { id: 24527 };
      order.party = party;
      const supplier: IParty = { id: 24527 };
      order.supplier = supplier;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party, supplier];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const status: IStatus = { id: 2927 };
      order.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const currencyUom: IUom = { id: 6576 };
      order.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call UomConversion query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const currencyConversion: IUomConversion = { id: 13785 };
      order.currencyConversion = currencyConversion;

      const uomConversionCollection: IUomConversion[] = [{ id: 13785 }];
      vi.spyOn(uomConversionService, 'query').mockReturnValue(of(new HttpResponse({ body: uomConversionCollection })));
      const additionalUomConversions = [currencyConversion];
      const expectedCollection: IUomConversion[] = [...additionalUomConversions, ...uomConversionCollection];
      vi.spyOn(uomConversionService, 'addUomConversionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(uomConversionService.query).toHaveBeenCalled();
      expect(uomConversionService.addUomConversionToCollectionIfMissing).toHaveBeenCalledWith(
        uomConversionCollection,
        ...additionalUomConversions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomConversionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Quote query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const quote: IQuote = { id: 10209 };
      order.quote = quote;

      const quoteCollection: IQuote[] = [{ id: 10209 }];
      vi.spyOn(quoteService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteCollection })));
      const additionalQuotes = [quote];
      const expectedCollection: IQuote[] = [...additionalQuotes, ...quoteCollection];
      vi.spyOn(quoteService, 'addQuoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(quoteService.query).toHaveBeenCalled();
      expect(quoteService.addQuoteToCollectionIfMissing).toHaveBeenCalledWith(
        quoteCollection,
        ...additionalQuotes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const facility: IFacility = { id: 4866 };
      order.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductStore query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const productStore: IProductStore = { id: 30593 };
      order.productStore = productStore;

      const productStoreCollection: IProductStore[] = [{ id: 30593 }];
      vi.spyOn(productStoreService, 'query').mockReturnValue(of(new HttpResponse({ body: productStoreCollection })));
      const additionalProductStores = [productStore];
      const expectedCollection: IProductStore[] = [...additionalProductStores, ...productStoreCollection];
      vi.spyOn(productStoreService, 'addProductStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(productStoreService.query).toHaveBeenCalled();
      expect(productStoreService.addProductStoreToCollectionIfMissing).toHaveBeenCalledWith(
        productStoreCollection,
        ...additionalProductStores.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productStoresSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Department query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const department: IDepartment = { id: 29518 };
      order.department = department;

      const departmentCollection: IDepartment[] = [{ id: 29518 }];
      vi.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      vi.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.departmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CustomTimePeriod query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const customTimePeriod: ICustomTimePeriod = { id: 19478 };
      order.customTimePeriod = customTimePeriod;

      const customTimePeriodCollection: ICustomTimePeriod[] = [{ id: 19478 }];
      vi.spyOn(customTimePeriodService, 'query').mockReturnValue(of(new HttpResponse({ body: customTimePeriodCollection })));
      const additionalCustomTimePeriods = [customTimePeriod];
      const expectedCollection: ICustomTimePeriod[] = [...additionalCustomTimePeriods, ...customTimePeriodCollection];
      vi.spyOn(customTimePeriodService, 'addCustomTimePeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(customTimePeriodService.query).toHaveBeenCalled();
      expect(customTimePeriodService.addCustomTimePeriodToCollectionIfMissing).toHaveBeenCalledWith(
        customTimePeriodCollection,
        ...additionalCustomTimePeriods.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.customTimePeriodsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const order: IOrder = { id: 4253 };
      const orderType: IOrderType = { id: 10129 };
      order.orderType = orderType;
      const salesChannel: ISalesChannel = { id: 25587 };
      order.salesChannel = salesChannel;
      const party: IParty = { id: 24527 };
      order.party = party;
      const supplier: IParty = { id: 24527 };
      order.supplier = supplier;
      const status: IStatus = { id: 2927 };
      order.status = status;
      const currencyUom: IUom = { id: 6576 };
      order.currencyUom = currencyUom;
      const currencyConversion: IUomConversion = { id: 13785 };
      order.currencyConversion = currencyConversion;
      const quote: IQuote = { id: 10209 };
      order.quote = quote;
      const facility: IFacility = { id: 4866 };
      order.facility = facility;
      const productStore: IProductStore = { id: 30593 };
      order.productStore = productStore;
      const department: IDepartment = { id: 29518 };
      order.department = department;
      const customTimePeriod: ICustomTimePeriod = { id: 19478 };
      order.customTimePeriod = customTimePeriod;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.orderTypesSharedCollection()).toContainEqual(orderType);
      expect(comp.salesChannelsSharedCollection()).toContainEqual(salesChannel);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.partiesSharedCollection()).toContainEqual(supplier);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.uomConversionsSharedCollection()).toContainEqual(currencyConversion);
      expect(comp.quotesSharedCollection()).toContainEqual(quote);
      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.productStoresSharedCollection()).toContainEqual(productStore);
      expect(comp.departmentsSharedCollection()).toContainEqual(department);
      expect(comp.customTimePeriodsSharedCollection()).toContainEqual(customTimePeriod);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrder>();
      const order = { id: 14644 };
      vi.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      vi.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(order);
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrder>();
      const order = { id: 14644 };
      vi.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      vi.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(order);
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrder>();
      const order = { id: 14644 };
      vi.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrderType', () => {
      it('should forward to orderTypeService', () => {
        const entity = { id: 10129 };
        const entity2 = { id: 28377 };
        vi.spyOn(orderTypeService, 'compareOrderType');
        comp.compareOrderType(entity, entity2);
        expect(orderTypeService.compareOrderType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareQuote', () => {
      it('should forward to quoteService', () => {
        const entity = { id: 10209 };
        const entity2 = { id: 23928 };
        vi.spyOn(quoteService, 'compareQuote');
        comp.compareQuote(entity, entity2);
        expect(quoteService.compareQuote).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFacility', () => {
      it('should forward to facilityService', () => {
        const entity = { id: 4866 };
        const entity2 = { id: 6958 };
        vi.spyOn(facilityService, 'compareFacility');
        comp.compareFacility(entity, entity2);
        expect(facilityService.compareFacility).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareDepartment', () => {
      it('should forward to departmentService', () => {
        const entity = { id: 29518 };
        const entity2 = { id: 15970 };
        vi.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomTimePeriod', () => {
      it('should forward to customTimePeriodService', () => {
        const entity = { id: 19478 };
        const entity2 = { id: 17611 };
        vi.spyOn(customTimePeriodService, 'compareCustomTimePeriod');
        comp.compareCustomTimePeriod(entity, entity2);
        expect(customTimePeriodService.compareCustomTimePeriod).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
