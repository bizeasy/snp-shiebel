import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment.test-samples';

import { PaymentFormService } from './payment-form.service';

describe('Payment Form Service', () => {
  let service: PaymentFormService;

  beforeEach(() => {
    service = TestBed.inject(PaymentFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paymentRefNumber: expect.any(Object),
            effectiveDate: expect.any(Object),
            paymentDate: expect.any(Object),
            amount: expect.any(Object),
            actualAmount: expect.any(Object),
            paymentStatus: expect.any(Object),
            mihpayId: expect.any(Object),
            txnId: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            productInfo: expect.any(Object),
            paymentType: expect.any(Object),
            paymentMethodType: expect.any(Object),
            paymentMethod: expect.any(Object),
            paymentGatewayResponse: expect.any(Object),
            status: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            roleType: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });

      it('passing IPayment should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paymentRefNumber: expect.any(Object),
            effectiveDate: expect.any(Object),
            paymentDate: expect.any(Object),
            amount: expect.any(Object),
            actualAmount: expect.any(Object),
            paymentStatus: expect.any(Object),
            mihpayId: expect.any(Object),
            txnId: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            productInfo: expect.any(Object),
            paymentType: expect.any(Object),
            paymentMethodType: expect.any(Object),
            paymentMethod: expect.any(Object),
            paymentGatewayResponse: expect.any(Object),
            status: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            roleType: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getPayment', () => {
      it('should return NewPayment for default Payment initial value', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithNewData);

        const payment = service.getPayment(formGroup);

        expect(payment).toMatchObject(sampleWithNewData);
      });

      it('should return NewPayment for empty Payment initial value', () => {
        const formGroup = service.createPaymentFormGroup();

        const payment = service.getPayment(formGroup);

        expect(payment).toMatchObject({});
      });

      it('should return IPayment', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithRequiredData);

        const payment = service.getPayment(formGroup);

        expect(payment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPayment should not enable id FormControl', () => {
        const formGroup = service.createPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPayment should disable id FormControl', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
