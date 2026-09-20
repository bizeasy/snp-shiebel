import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment-gateway-config.test-samples';

import { PaymentGatewayConfigFormService } from './payment-gateway-config-form.service';

describe('PaymentGatewayConfig Form Service', () => {
  let service: PaymentGatewayConfigFormService;

  beforeEach(() => {
    service = TestBed.inject(PaymentGatewayConfigFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentGatewayConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            authUrl: expect.any(Object),
            releaseUrl: expect.any(Object),
            refundUrl: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentGatewayConfig should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            authUrl: expect.any(Object),
            releaseUrl: expect.any(Object),
            refundUrl: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentGatewayConfig', () => {
      it('should return NewPaymentGatewayConfig for default PaymentGatewayConfig initial value', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup(sampleWithNewData);

        const paymentGatewayConfig = service.getPaymentGatewayConfig(formGroup);

        expect(paymentGatewayConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentGatewayConfig for empty PaymentGatewayConfig initial value', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup();

        const paymentGatewayConfig = service.getPaymentGatewayConfig(formGroup);

        expect(paymentGatewayConfig).toMatchObject({});
      });

      it('should return IPaymentGatewayConfig', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup(sampleWithRequiredData);

        const paymentGatewayConfig = service.getPaymentGatewayConfig(formGroup);

        expect(paymentGatewayConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentGatewayConfig should not enable id FormControl', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentGatewayConfig should disable id FormControl', () => {
        const formGroup = service.createPaymentGatewayConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
