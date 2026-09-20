import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPaymentGatewayConfig } from '../payment-gateway-config.model';
import { PaymentGatewayConfigService } from '../service/payment-gateway-config.service';

import { PaymentGatewayConfigFormService } from './payment-gateway-config-form.service';
import { PaymentGatewayConfigUpdate } from './payment-gateway-config-update';

describe('PaymentGatewayConfig Management Update Component', () => {
  let comp: PaymentGatewayConfigUpdate;
  let fixture: ComponentFixture<PaymentGatewayConfigUpdate>;
  let activatedRoute: ActivatedRoute;
  let paymentGatewayConfigFormService: PaymentGatewayConfigFormService;
  let paymentGatewayConfigService: PaymentGatewayConfigService;

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

    fixture = TestBed.createComponent(PaymentGatewayConfigUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentGatewayConfigFormService = TestBed.inject(PaymentGatewayConfigFormService);
    paymentGatewayConfigService = TestBed.inject(PaymentGatewayConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const paymentGatewayConfig: IPaymentGatewayConfig = { id: 7027 };

      activatedRoute.data = of({ paymentGatewayConfig });
      comp.ngOnInit();

      expect(comp.paymentGatewayConfig).toEqual(paymentGatewayConfig);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentGatewayConfig>();
      const paymentGatewayConfig = { id: 28126 };
      vi.spyOn(paymentGatewayConfigFormService, 'getPaymentGatewayConfig').mockReturnValue(paymentGatewayConfig);
      vi.spyOn(paymentGatewayConfigService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGatewayConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentGatewayConfig);
      saveSubject.complete();

      // THEN
      expect(paymentGatewayConfigFormService.getPaymentGatewayConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentGatewayConfigService.update).toHaveBeenCalledWith(expect.objectContaining(paymentGatewayConfig));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentGatewayConfig>();
      const paymentGatewayConfig = { id: 28126 };
      vi.spyOn(paymentGatewayConfigFormService, 'getPaymentGatewayConfig').mockReturnValue({ id: null });
      vi.spyOn(paymentGatewayConfigService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGatewayConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paymentGatewayConfig);
      saveSubject.complete();

      // THEN
      expect(paymentGatewayConfigFormService.getPaymentGatewayConfig).toHaveBeenCalled();
      expect(paymentGatewayConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaymentGatewayConfig>();
      const paymentGatewayConfig = { id: 28126 };
      vi.spyOn(paymentGatewayConfigService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGatewayConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentGatewayConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
