import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';

import { PaymentMethodFormService } from './payment-method-form.service';
import { PaymentMethodUpdate } from './payment-method-update';

describe('PaymentMethod Management Update Component', () => {
  let comp: PaymentMethodUpdate;
  let fixture: ComponentFixture<PaymentMethodUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentMethodFormService: PaymentMethodFormService;
  let paymentMethodService: PaymentMethodService;
  let paymentMethodTypeService: PaymentMethodTypeService;
  let partyService: PartyService;

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

    fixture = TestBed.createComponent(PaymentMethodUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentMethodFormService = TestBed.inject(PaymentMethodFormService);
    paymentMethodService = TestBed.inject(PaymentMethodService);
    paymentMethodTypeService = TestBed.inject(PaymentMethodTypeService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PaymentMethodType query and add missing value', () => {
      const paymentMethod: IPaymentMethod = { id: 19925 };
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      paymentMethod.paymentMethodType = paymentMethodType;

      const paymentMethodTypeCollection: IPaymentMethodType[] = [{ id: 6662 }];
      vi.spyOn(paymentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodTypeCollection })));
      const additionalPaymentMethodTypes = [paymentMethodType];
      const expectedCollection: IPaymentMethodType[] = [...additionalPaymentMethodTypes, ...paymentMethodTypeCollection];
      vi.spyOn(paymentMethodTypeService, 'addPaymentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentMethod });
      comp.ngOnInit();

      expect(paymentMethodTypeService.query).toHaveBeenCalled();
      expect(paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodTypeCollection,
        ...additionalPaymentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const paymentMethod: IPaymentMethod = { id: 19925 };
      const party: IParty = { id: 24527 };
      paymentMethod.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentMethod });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const paymentMethod: IPaymentMethod = { id: 19925 };
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      paymentMethod.paymentMethodType = paymentMethodType;
      const party: IParty = { id: 24527 };
      paymentMethod.party = party;

      activatedRoute.data = of({ paymentMethod });
      comp.ngOnInit();

      expect(comp.paymentMethodTypesSharedCollection()).toContainEqual(paymentMethodType);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.paymentMethod).toEqual(paymentMethod);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentMethod>();
      const paymentMethod = { id: 25086 };
      vi.spyOn(paymentMethodFormService, 'getPaymentMethod').mockReturnValue(paymentMethod);
      vi.spyOn(paymentMethodService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentMethod);
      saveSubject.complete();

      // THEN
      expect(paymentMethodFormService.getPaymentMethod).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentMethodService.update).toHaveBeenCalledWith(expect.objectContaining(paymentMethod));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentMethod>();
      const paymentMethod = { id: 25086 };
      vi.spyOn(paymentMethodFormService, 'getPaymentMethod').mockReturnValue({ id: null });
      vi.spyOn(paymentMethodService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentMethod: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentMethod);
      saveSubject.complete();

      // THEN
      expect(paymentMethodFormService.getPaymentMethod).toHaveBeenCalled();
      expect(paymentMethodService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentMethod>();
      const paymentMethod = { id: 25086 };
      vi.spyOn(paymentMethodService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentMethod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentMethodService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaymentMethodType', () => {
      it('should forward to paymentMethodTypeService', () => {
        const entity = { id: 6662 };
        const entity2 = { id: 13122 };
        vi.spyOn(paymentMethodTypeService, 'comparePaymentMethodType');
        comp.comparePaymentMethodType(entity, entity2);
        expect(paymentMethodTypeService.comparePaymentMethodType).toHaveBeenCalledWith(entity, entity2);
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
  });
});
