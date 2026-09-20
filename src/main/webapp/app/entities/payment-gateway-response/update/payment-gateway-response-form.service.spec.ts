import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment-gateway-response.test-samples';

import { PaymentGatewayResponseFormService } from './payment-gateway-response-form.service';

describe('PaymentGatewayResponse Form Service', () => {
  let service: PaymentGatewayResponseFormService;

  beforeEach(() => {
    service = TestBed.inject(PaymentGatewayResponseFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentGatewayResponseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referenceNumber: expect.any(Object),
            amount: expect.any(Object),
            gatewayMessage: expect.any(Object),
            paymentGatewayConfig: expect.any(Object),
            paymentMethodType: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentGatewayResponse should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            referenceNumber: expect.any(Object),
            amount: expect.any(Object),
            gatewayMessage: expect.any(Object),
            paymentGatewayConfig: expect.any(Object),
            paymentMethodType: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentGatewayResponse', () => {
      it('should return NewPaymentGatewayResponse for default PaymentGatewayResponse initial value', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup(sampleWithNewData);

        const paymentGatewayResponse = service.getPaymentGatewayResponse(formGroup);

        expect(paymentGatewayResponse).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentGatewayResponse for empty PaymentGatewayResponse initial value', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup();

        const paymentGatewayResponse = service.getPaymentGatewayResponse(formGroup);

        expect(paymentGatewayResponse).toMatchObject({});
      });

      it('should return IPaymentGatewayResponse', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup(sampleWithRequiredData);

        const paymentGatewayResponse = service.getPaymentGatewayResponse(formGroup);

        expect(paymentGatewayResponse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentGatewayResponse should not enable id FormControl', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentGatewayResponse should disable id FormControl', () => {
        const formGroup = service.createPaymentGatewayResponseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
