import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { PaymentGatewayResponseService } from 'app/entities/payment-gateway-response/service/payment-gateway-response.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentGatewayResponse } from 'app/entities/payment-gateway-response/payment-gateway-response.model';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { IPaymentType } from 'app/entities/payment-type/payment-type.model';
import { PaymentTypeService } from 'app/entities/payment-type/service/payment-type.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';

import { PaymentFormService } from './payment-form.service';
import { PaymentUpdate } from './payment-update';

describe('Payment Management Update Component', () => {
  let comp: PaymentUpdate;
  let fixture: ComponentFixture<PaymentUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentFormService: PaymentFormService;
  let paymentService: PaymentService;
  let paymentTypeService: PaymentTypeService;
  let paymentMethodTypeService: PaymentMethodTypeService;
  let paymentMethodService: PaymentMethodService;
  let paymentGatewayResponseService: PaymentGatewayResponseService;
  let statusService: StatusService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;
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

    fixture = TestBed.createComponent(PaymentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentFormService = TestBed.inject(PaymentFormService);
    paymentService = TestBed.inject(PaymentService);
    paymentTypeService = TestBed.inject(PaymentTypeService);
    paymentMethodTypeService = TestBed.inject(PaymentMethodTypeService);
    paymentMethodService = TestBed.inject(PaymentMethodService);
    paymentGatewayResponseService = TestBed.inject(PaymentGatewayResponseService);
    statusService = TestBed.inject(StatusService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);
    uomService = TestBed.inject(UomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PaymentType query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const paymentType: IPaymentType = { id: 3410 };
      payment.paymentType = paymentType;

      const paymentTypeCollection: IPaymentType[] = [{ id: 3410 }];
      vi.spyOn(paymentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentTypeCollection })));
      const additionalPaymentTypes = [paymentType];
      const expectedCollection: IPaymentType[] = [...additionalPaymentTypes, ...paymentTypeCollection];
      vi.spyOn(paymentTypeService, 'addPaymentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(paymentTypeService.query).toHaveBeenCalled();
      expect(paymentTypeService.addPaymentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        paymentTypeCollection,
        ...additionalPaymentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PaymentMethodType query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      payment.paymentMethodType = paymentMethodType;

      const paymentMethodTypeCollection: IPaymentMethodType[] = [{ id: 6662 }];
      vi.spyOn(paymentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodTypeCollection })));
      const additionalPaymentMethodTypes = [paymentMethodType];
      const expectedCollection: IPaymentMethodType[] = [...additionalPaymentMethodTypes, ...paymentMethodTypeCollection];
      vi.spyOn(paymentMethodTypeService, 'addPaymentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(paymentMethodTypeService.query).toHaveBeenCalled();
      expect(paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodTypeCollection,
        ...additionalPaymentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PaymentMethod query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const paymentMethod: IPaymentMethod = { id: 25086 };
      payment.paymentMethod = paymentMethod;

      const paymentMethodCollection: IPaymentMethod[] = [{ id: 25086 }];
      vi.spyOn(paymentMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodCollection })));
      const additionalPaymentMethods = [paymentMethod];
      const expectedCollection: IPaymentMethod[] = [...additionalPaymentMethods, ...paymentMethodCollection];
      vi.spyOn(paymentMethodService, 'addPaymentMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(paymentMethodService.query).toHaveBeenCalled();
      expect(paymentMethodService.addPaymentMethodToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodCollection,
        ...additionalPaymentMethods.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentMethodsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PaymentGatewayResponse query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const paymentGatewayResponse: IPaymentGatewayResponse = { id: 25625 };
      payment.paymentGatewayResponse = paymentGatewayResponse;

      const paymentGatewayResponseCollection: IPaymentGatewayResponse[] = [{ id: 25625 }];
      vi.spyOn(paymentGatewayResponseService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentGatewayResponseCollection })));
      const additionalPaymentGatewayResponses = [paymentGatewayResponse];
      const expectedCollection: IPaymentGatewayResponse[] = [...additionalPaymentGatewayResponses, ...paymentGatewayResponseCollection];
      vi.spyOn(paymentGatewayResponseService, 'addPaymentGatewayResponseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(paymentGatewayResponseService.query).toHaveBeenCalled();
      expect(paymentGatewayResponseService.addPaymentGatewayResponseToCollectionIfMissing).toHaveBeenCalledWith(
        paymentGatewayResponseCollection,
        ...additionalPaymentGatewayResponses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentGatewayResponsesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const status: IStatus = { id: 2927 };
      payment.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const fromParty: IParty = { id: 24527 };
      payment.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      payment.toParty = toParty;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [fromParty, toParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const roleType: IRoleType = { id: 17576 };
      payment.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Uom query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const currencyUom: IUom = { id: 6576 };
      payment.currencyUom = currencyUom;

      const uomCollection: IUom[] = [{ id: 6576 }];
      vi.spyOn(uomService, 'query').mockReturnValue(of(new HttpResponse({ body: uomCollection })));
      const additionalUoms = [currencyUom];
      const expectedCollection: IUom[] = [...additionalUoms, ...uomCollection];
      vi.spyOn(uomService, 'addUomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(uomService.query).toHaveBeenCalled();
      expect(uomService.addUomToCollectionIfMissing).toHaveBeenCalledWith(
        uomCollection,
        ...additionalUoms.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.uomsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const payment: IPayment = { id: 31232 };
      const paymentType: IPaymentType = { id: 3410 };
      payment.paymentType = paymentType;
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      payment.paymentMethodType = paymentMethodType;
      const paymentMethod: IPaymentMethod = { id: 25086 };
      payment.paymentMethod = paymentMethod;
      const paymentGatewayResponse: IPaymentGatewayResponse = { id: 25625 };
      payment.paymentGatewayResponse = paymentGatewayResponse;
      const status: IStatus = { id: 2927 };
      payment.status = status;
      const fromParty: IParty = { id: 24527 };
      payment.fromParty = fromParty;
      const toParty: IParty = { id: 24527 };
      payment.toParty = toParty;
      const roleType: IRoleType = { id: 17576 };
      payment.roleType = roleType;
      const currencyUom: IUom = { id: 6576 };
      payment.currencyUom = currencyUom;

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(comp.paymentTypesSharedCollection()).toContainEqual(paymentType);
      expect(comp.paymentMethodTypesSharedCollection()).toContainEqual(paymentMethodType);
      expect(comp.paymentMethodsSharedCollection()).toContainEqual(paymentMethod);
      expect(comp.paymentGatewayResponsesSharedCollection()).toContainEqual(paymentGatewayResponse);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.partiesSharedCollection()).toContainEqual(fromParty);
      expect(comp.partiesSharedCollection()).toContainEqual(toParty);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.uomsSharedCollection()).toContainEqual(currencyUom);
      expect(comp.payment).toEqual(payment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPayment>();
      const payment = { id: 20208 };
      vi.spyOn(paymentFormService, 'getPayment').mockReturnValue(payment);
      vi.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(payment);
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentService.update).toHaveBeenCalledWith(expect.objectContaining(payment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPayment>();
      const payment = { id: 20208 };
      vi.spyOn(paymentFormService, 'getPayment').mockReturnValue({ id: null });
      vi.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(payment);
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPayment).toHaveBeenCalled();
      expect(paymentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPayment>();
      const payment = { id: 20208 };
      vi.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaymentType', () => {
      it('should forward to paymentTypeService', () => {
        const entity = { id: 3410 };
        const entity2 = { id: 8193 };
        vi.spyOn(paymentTypeService, 'comparePaymentType');
        comp.comparePaymentType(entity, entity2);
        expect(paymentTypeService.comparePaymentType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePaymentMethodType', () => {
      it('should forward to paymentMethodTypeService', () => {
        const entity = { id: 6662 };
        const entity2 = { id: 13122 };
        vi.spyOn(paymentMethodTypeService, 'comparePaymentMethodType');
        comp.comparePaymentMethodType(entity, entity2);
        expect(paymentMethodTypeService.comparePaymentMethodType).toHaveBeenCalledWith(entity, entity2);
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

    describe('comparePaymentGatewayResponse', () => {
      it('should forward to paymentGatewayResponseService', () => {
        const entity = { id: 25625 };
        const entity2 = { id: 13199 };
        vi.spyOn(paymentGatewayResponseService, 'comparePaymentGatewayResponse');
        comp.comparePaymentGatewayResponse(entity, entity2);
        expect(paymentGatewayResponseService.comparePaymentGatewayResponse).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareRoleType', () => {
      it('should forward to roleTypeService', () => {
        const entity = { id: 17576 };
        const entity2 = { id: 27052 };
        vi.spyOn(roleTypeService, 'compareRoleType');
        comp.compareRoleType(entity, entity2);
        expect(roleTypeService.compareRoleType).toHaveBeenCalledWith(entity, entity2);
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
