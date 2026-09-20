import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPaymentGatewayConfig } from 'app/entities/payment-gateway-config/payment-gateway-config.model';
import { PaymentGatewayConfigService } from 'app/entities/payment-gateway-config/service/payment-gateway-config.service';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { IPaymentGatewayResponse } from '../payment-gateway-response.model';
import { PaymentGatewayResponseService } from '../service/payment-gateway-response.service';

import { PaymentGatewayResponseFormService } from './payment-gateway-response-form.service';
import { PaymentGatewayResponseUpdate } from './payment-gateway-response-update';

describe('PaymentGatewayResponse Management Update Component', () => {
  let comp: PaymentGatewayResponseUpdate;
  let fixture: ComponentFixture<PaymentGatewayResponseUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentGatewayResponseFormService: PaymentGatewayResponseFormService;
  let paymentGatewayResponseService: PaymentGatewayResponseService;
  let paymentGatewayConfigService: PaymentGatewayConfigService;
  let paymentMethodTypeService: PaymentMethodTypeService;

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

    fixture = TestBed.createComponent(PaymentGatewayResponseUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentGatewayResponseFormService = TestBed.inject(PaymentGatewayResponseFormService);
    paymentGatewayResponseService = TestBed.inject(PaymentGatewayResponseService);
    paymentGatewayConfigService = TestBed.inject(PaymentGatewayConfigService);
    paymentMethodTypeService = TestBed.inject(PaymentMethodTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PaymentGatewayConfig query and add missing value', () => {
      const paymentGatewayResponse: IPaymentGatewayResponse = { id: 13199 };
      const paymentGatewayConfig: IPaymentGatewayConfig = { id: 28126 };
      paymentGatewayResponse.paymentGatewayConfig = paymentGatewayConfig;

      const paymentGatewayConfigCollection: IPaymentGatewayConfig[] = [{ id: 28126 }];
      vi.spyOn(paymentGatewayConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentGatewayConfigCollection })));
      const additionalPaymentGatewayConfigs = [paymentGatewayConfig];
      const expectedCollection: IPaymentGatewayConfig[] = [...additionalPaymentGatewayConfigs, ...paymentGatewayConfigCollection];
      vi.spyOn(paymentGatewayConfigService, 'addPaymentGatewayConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentGatewayResponse });
      comp.ngOnInit();

      expect(paymentGatewayConfigService.query).toHaveBeenCalled();
      expect(paymentGatewayConfigService.addPaymentGatewayConfigToCollectionIfMissing).toHaveBeenCalledWith(
        paymentGatewayConfigCollection,
        ...additionalPaymentGatewayConfigs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentGatewayConfigsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PaymentMethodType query and add missing value', () => {
      const paymentGatewayResponse: IPaymentGatewayResponse = { id: 13199 };
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      paymentGatewayResponse.paymentMethodType = paymentMethodType;

      const paymentMethodTypeCollection: IPaymentMethodType[] = [{ id: 6662 }];
      vi.spyOn(paymentMethodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodTypeCollection })));
      const additionalPaymentMethodTypes = [paymentMethodType];
      const expectedCollection: IPaymentMethodType[] = [...additionalPaymentMethodTypes, ...paymentMethodTypeCollection];
      vi.spyOn(paymentMethodTypeService, 'addPaymentMethodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentGatewayResponse });
      comp.ngOnInit();

      expect(paymentMethodTypeService.query).toHaveBeenCalled();
      expect(paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodTypeCollection,
        ...additionalPaymentMethodTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paymentMethodTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const paymentGatewayResponse: IPaymentGatewayResponse = { id: 13199 };
      const paymentGatewayConfig: IPaymentGatewayConfig = { id: 28126 };
      paymentGatewayResponse.paymentGatewayConfig = paymentGatewayConfig;
      const paymentMethodType: IPaymentMethodType = { id: 6662 };
      paymentGatewayResponse.paymentMethodType = paymentMethodType;

      activatedRoute.data = of({ paymentGatewayResponse });
      comp.ngOnInit();

      expect(comp.paymentGatewayConfigsSharedCollection()).toContainEqual(paymentGatewayConfig);
      expect(comp.paymentMethodTypesSharedCollection()).toContainEqual(paymentMethodType);
      expect(comp.paymentGatewayResponse).toEqual(paymentGatewayResponse);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentGatewayResponse>();
      const paymentGatewayResponse = { id: 25625 };
      vi.spyOn(paymentGatewayResponseFormService, 'getPaymentGatewayResponse').mockReturnValue(paymentGatewayResponse);
      vi.spyOn(paymentGatewayResponseService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGatewayResponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentGatewayResponse);
      saveSubject.complete();

      // THEN
      expect(paymentGatewayResponseFormService.getPaymentGatewayResponse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentGatewayResponseService.update).toHaveBeenCalledWith(expect.objectContaining(paymentGatewayResponse));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentGatewayResponse>();
      const paymentGatewayResponse = { id: 25625 };
      vi.spyOn(paymentGatewayResponseFormService, 'getPaymentGatewayResponse').mockReturnValue({ id: null });
      vi.spyOn(paymentGatewayResponseService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGatewayResponse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentGatewayResponse);
      saveSubject.complete();

      // THEN
      expect(paymentGatewayResponseFormService.getPaymentGatewayResponse).toHaveBeenCalled();
      expect(paymentGatewayResponseService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentGatewayResponse>();
      const paymentGatewayResponse = { id: 25625 };
      vi.spyOn(paymentGatewayResponseService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGatewayResponse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentGatewayResponseService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaymentGatewayConfig', () => {
      it('should forward to paymentGatewayConfigService', () => {
        const entity = { id: 28126 };
        const entity2 = { id: 7027 };
        vi.spyOn(paymentGatewayConfigService, 'comparePaymentGatewayConfig');
        comp.comparePaymentGatewayConfig(entity, entity2);
        expect(paymentGatewayConfigService.comparePaymentGatewayConfig).toHaveBeenCalledWith(entity, entity2);
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
  });
});
