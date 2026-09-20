import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment-application.test-samples';

import { PaymentApplicationFormService } from './payment-application-form.service';

describe('PaymentApplication Form Service', () => {
  let service: PaymentApplicationFormService;

  beforeEach(() => {
    service = TestBed.inject(PaymentApplicationFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentApplicationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentApplicationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amountApplied: expect.any(Object),
            payment: expect.any(Object),
            toPayment: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentApplication should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentApplicationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amountApplied: expect.any(Object),
            payment: expect.any(Object),
            toPayment: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentApplication', () => {
      it('should return NewPaymentApplication for default PaymentApplication initial value', () => {
        const formGroup = service.createPaymentApplicationFormGroup(sampleWithNewData);

        const paymentApplication = service.getPaymentApplication(formGroup);

        expect(paymentApplication).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentApplication for empty PaymentApplication initial value', () => {
        const formGroup = service.createPaymentApplicationFormGroup();

        const paymentApplication = service.getPaymentApplication(formGroup);

        expect(paymentApplication).toMatchObject({});
      });

      it('should return IPaymentApplication', () => {
        const formGroup = service.createPaymentApplicationFormGroup(sampleWithRequiredData);

        const paymentApplication = service.getPaymentApplication(formGroup);

        expect(paymentApplication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentApplication should not enable id FormControl', () => {
        const formGroup = service.createPaymentApplicationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentApplication should disable id FormControl', () => {
        const formGroup = service.createPaymentApplicationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
