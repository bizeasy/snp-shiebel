import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../return-item-billing.test-samples';

import { ReturnItemBillingFormService } from './return-item-billing-form.service';

describe('ReturnItemBilling Form Service', () => {
  let service: ReturnItemBillingFormService;

  beforeEach(() => {
    service = TestBed.inject(ReturnItemBillingFormService);
  });

  describe('Service methods', () => {
    describe('createReturnItemBillingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReturnItemBillingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            amount: expect.any(Object),
            returnItem: expect.any(Object),
            orderItem: expect.any(Object),
            invoiceItem: expect.any(Object),
            shipmentReceipt: expect.any(Object),
          }),
        );
      });

      it('passing IReturnItemBilling should create a new form with FormGroup', () => {
        const formGroup = service.createReturnItemBillingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            amount: expect.any(Object),
            returnItem: expect.any(Object),
            orderItem: expect.any(Object),
            invoiceItem: expect.any(Object),
            shipmentReceipt: expect.any(Object),
          }),
        );
      });
    });

    describe('getReturnItemBilling', () => {
      it('should return NewReturnItemBilling for default ReturnItemBilling initial value', () => {
        const formGroup = service.createReturnItemBillingFormGroup(sampleWithNewData);

        const returnItemBilling = service.getReturnItemBilling(formGroup);

        expect(returnItemBilling).toMatchObject(sampleWithNewData);
      });

      it('should return NewReturnItemBilling for empty ReturnItemBilling initial value', () => {
        const formGroup = service.createReturnItemBillingFormGroup();

        const returnItemBilling = service.getReturnItemBilling(formGroup);

        expect(returnItemBilling).toMatchObject({});
      });

      it('should return IReturnItemBilling', () => {
        const formGroup = service.createReturnItemBillingFormGroup(sampleWithRequiredData);

        const returnItemBilling = service.getReturnItemBilling(formGroup);

        expect(returnItemBilling).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReturnItemBilling should not enable id FormControl', () => {
        const formGroup = service.createReturnItemBillingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReturnItemBilling should disable id FormControl', () => {
        const formGroup = service.createReturnItemBillingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
