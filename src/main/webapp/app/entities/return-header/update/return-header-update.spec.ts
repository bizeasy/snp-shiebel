import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IFacility } from 'app/entities/facility/facility.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IReturnHeaderType } from 'app/entities/return-header-type/return-header-type.model';
import { ReturnHeaderTypeService } from 'app/entities/return-header-type/service/return-header-type.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IReturnHeader } from '../return-header.model';
import { ReturnHeaderService } from '../service/return-header.service';

import { ReturnHeaderFormService } from './return-header-form.service';
import { ReturnHeaderUpdate } from './return-header-update';

describe('ReturnHeader Management Update Component', () => {
  let comp: ReturnHeaderUpdate;
  let fixture: ComponentFixture<ReturnHeaderUpdate>;
  let activatedRoute: ActivatedRoute;
  let returnHeaderFormService: ReturnHeaderFormService;
  let returnHeaderService: ReturnHeaderService;
  let returnHeaderTypeService: ReturnHeaderTypeService;
  let statusService: StatusService;
  let partyService: PartyService;
  let paymentMethodService: PaymentMethodService;
  let contactMechService: ContactMechService;
  let facilityService: FacilityService;
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

    fixture = TestBed.createComponent(ReturnHeaderUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    returnHeaderFormService = TestBed.inject(ReturnHeaderFormService);
    returnHeaderService = TestBed.inject(ReturnHeaderService);
    returnHeaderTypeService = TestBed.inject(ReturnHeaderTypeService);
    statusService = TestBed.inject(StatusService);
    partyService = TestBed.inject(PartyService);
    paymentMethodService = TestBed.inject(PaymentMethodService);
    contactMechService = TestBed.inject(ContactMechService);
    facilityService = TestBed.inject(FacilityService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ReturnHeaderType query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const returnType: IReturnHeaderType = { id: 14220 };
      returnHeader.returnType = returnType;

      const returnHeaderTypeCollection: IReturnHeaderType[] = [{ id: 14220 }];
      vi.spyOn(returnHeaderTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: returnHeaderTypeCollection })));
      const additionalReturnHeaderTypes = [returnType];
      const expectedCollection: IReturnHeaderType[] = [...additionalReturnHeaderTypes, ...returnHeaderTypeCollection];
      vi.spyOn(returnHeaderTypeService, 'addReturnHeaderTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(returnHeaderTypeService.query).toHaveBeenCalled();
      expect(returnHeaderTypeService.addReturnHeaderTypeToCollectionIfMissing).toHaveBeenCalledWith(
        returnHeaderTypeCollection,
        ...additionalReturnHeaderTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.returnHeaderTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const status: IStatus = { id: 2927 };
      returnHeader.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const fromParty: IParty = { id: 24527 };
      returnHeader.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      returnHeader.toParty = toParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [fromParty, toParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PaymentMethod query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const paymentMethod: IPaymentMethod = { id: 25086 };
      returnHeader.paymentMethod = paymentMethod;

      const paymentMethodCollection: IPaymentMethod[] = [{ id: 25086 }];
      vi.spyOn(paymentMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodCollection })));
      const additionalPaymentMethods = [paymentMethod];
      const expectedCollection: IPaymentMethod[] = [...additionalPaymentMethods, ...paymentMethodCollection];
      vi.spyOn(paymentMethodService, 'addPaymentMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(paymentMethodService.query).toHaveBeenCalled();
      expect(paymentMethodService.addPaymentMethodToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodCollection,
        ...additionalPaymentMethods.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentMethodsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMech query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const originContactMech: IContactMech = { id: 5019 };
      returnHeader.originContactMech = originContactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [originContactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Facility query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const destinationFacility: IFacility = { id: 4866 };
      returnHeader.destinationFacility = destinationFacility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [destinationFacility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const currencyUom: IUom = { id: 6576 };
      returnHeader.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const returnHeader: IReturnHeader = { id: 32527 };
      const returnType: IReturnHeaderType = { id: 14220 };
      returnHeader.returnType = returnType;
      const status: IStatus = { id: 2927 };
      returnHeader.status = status;
      const fromParty: IParty = { id: 24527 };
      returnHeader.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      returnHeader.toParty = toParty;
      const paymentMethod: IPaymentMethod = { id: 25086 };
      returnHeader.paymentMethod = paymentMethod;
      const originContactMech: IContactMech = { id: 5019 };
      returnHeader.originContactMech = originContactMech;
      const destinationFacility: IFacility = { id: 4866 };
      returnHeader.destinationFacility = destinationFacility;
      const currencyUom: IUom = { id: 6576 };
      returnHeader.currencyUom = currencyUom;

      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      expect(comp.returnHeaderTypesSharedCollection()).toContainEqual(returnType);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.partiesSharedCollection()).toContainEqual(fromParty);
      expect(comp.partiesSharedCollection()).toContainEqual(toParty);
      expect(comp.paymentMethodsSharedCollection()).toContainEqual(paymentMethod);
      expect(comp.contactMechesSharedCollection()).toContainEqual(originContactMech);
      expect(comp.facilitiesSharedCollection()).toContainEqual(destinationFacility);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.returnHeader).toEqual(returnHeader);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnHeader>();
      const returnHeader = { id: 1951 };
      vi.spyOn(returnHeaderFormService, 'getReturnHeader').mockReturnValue(returnHeader);
      vi.spyOn(returnHeaderService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnHeader);
      saveSubject.complete();

      // THEN
      expect(returnHeaderFormService.getReturnHeader).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(returnHeaderService.update).toHaveBeenCalledWith(expect.objectContaining(returnHeader));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnHeader>();
      const returnHeader = { id: 1951 };
      vi.spyOn(returnHeaderFormService, 'getReturnHeader').mockReturnValue({ id: null });
      vi.spyOn(returnHeaderService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnHeader: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnHeader);
      saveSubject.complete();

      // THEN
      expect(returnHeaderFormService.getReturnHeader).toHaveBeenCalled();
      expect(returnHeaderService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnHeader>();
      const returnHeader = { id: 1951 };
      vi.spyOn(returnHeaderService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnHeader });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(returnHeaderService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReturnHeaderType', () => {
      it('should forward to returnHeaderTypeService', () => {
        const entity = { id: 14220 };
        const entity2 = { id: 26920 };
        vi.spyOn(returnHeaderTypeService, 'compareReturnHeaderType');
        comp.compareReturnHeaderType(entity, entity2);
        expect(returnHeaderTypeService.compareReturnHeaderType).toHaveBeenCalledWith(entity, entity2);
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

    describe('comparePaymentMethod', () => {
      it('should forward to paymentMethodService', () => {
        const entity = { id: 25086 };
        const entity2 = { id: 19925 };
        vi.spyOn(paymentMethodService, 'comparePaymentMethod');
        comp.comparePaymentMethod(entity, entity2);
        expect(paymentMethodService.comparePaymentMethod).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
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
