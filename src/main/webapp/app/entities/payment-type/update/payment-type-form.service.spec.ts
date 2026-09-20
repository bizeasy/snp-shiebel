import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment-type.test-samples';

import { PaymentTypeFormService } from './payment-type-form.service';

describe('PaymentType Form Service', () => {
  let service: PaymentTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(PaymentTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentType should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentType', () => {
      it('should return NewPaymentType for default PaymentType initial value', () => {
        const formGroup = service.createPaymentTypeFormGroup(sampleWithNewData);

        const paymentType = service.getPaymentType(formGroup);

        expect(paymentType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentType for empty PaymentType initial value', () => {
        const formGroup = service.createPaymentTypeFormGroup();

        const paymentType = service.getPaymentType(formGroup);

        expect(paymentType).toMatchObject({});
      });

      it('should return IPaymentType', () => {
        const formGroup = service.createPaymentTypeFormGroup(sampleWithRequiredData);

        const paymentType = service.getPaymentType(formGroup);

        expect(paymentType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentType should not enable id FormControl', () => {
        const formGroup = service.createPaymentTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentType should disable id FormControl', () => {
        const formGroup = service.createPaymentTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
