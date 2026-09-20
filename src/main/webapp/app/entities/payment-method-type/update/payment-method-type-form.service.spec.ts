import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment-method-type.test-samples';

import { PaymentMethodTypeFormService } from './payment-method-type-form.service';

describe('PaymentMethodType Form Service', () => {
  let service: PaymentMethodTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(PaymentMethodTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentMethodTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentMethodType should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentMethodType', () => {
      it('should return NewPaymentMethodType for default PaymentMethodType initial value', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup(sampleWithNewData);

        const paymentMethodType = service.getPaymentMethodType(formGroup);

        expect(paymentMethodType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentMethodType for empty PaymentMethodType initial value', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup();

        const paymentMethodType = service.getPaymentMethodType(formGroup);

        expect(paymentMethodType).toMatchObject({});
      });

      it('should return IPaymentMethodType', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup(sampleWithRequiredData);

        const paymentMethodType = service.getPaymentMethodType(formGroup);

        expect(paymentMethodType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentMethodType should not enable id FormControl', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentMethodType should disable id FormControl', () => {
        const formGroup = service.createPaymentMethodTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
