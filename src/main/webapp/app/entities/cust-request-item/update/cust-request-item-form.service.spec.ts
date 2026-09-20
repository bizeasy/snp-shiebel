import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request-item.test-samples';

import { CustRequestItemFormService } from './cust-request-item-form.service';

describe('CustRequestItem Form Service', () => {
  let service: CustRequestItemFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestItemFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            priority: expect.any(Object),
            requiredByDate: expect.any(Object),
            quantity: expect.any(Object),
            selectedAmount: expect.any(Object),
            maxAmount: expect.any(Object),
            description: expect.any(Object),
            story: expect.any(Object),
            custRequest: expect.any(Object),
            status: expect.any(Object),
            product: expect.any(Object),
            uom: expect.any(Object),
            shoppingListItem: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequestItem should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            priority: expect.any(Object),
            requiredByDate: expect.any(Object),
            quantity: expect.any(Object),
            selectedAmount: expect.any(Object),
            maxAmount: expect.any(Object),
            description: expect.any(Object),
            story: expect.any(Object),
            custRequest: expect.any(Object),
            status: expect.any(Object),
            product: expect.any(Object),
            uom: expect.any(Object),
            shoppingListItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequestItem', () => {
      it('should return NewCustRequestItem for default CustRequestItem initial value', () => {
        const formGroup = service.createCustRequestItemFormGroup(sampleWithNewData);

        const custRequestItem = service.getCustRequestItem(formGroup);

        expect(custRequestItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequestItem for empty CustRequestItem initial value', () => {
        const formGroup = service.createCustRequestItemFormGroup();

        const custRequestItem = service.getCustRequestItem(formGroup);

        expect(custRequestItem).toMatchObject({});
      });

      it('should return ICustRequestItem', () => {
        const formGroup = service.createCustRequestItemFormGroup(sampleWithRequiredData);

        const custRequestItem = service.getCustRequestItem(formGroup);

        expect(custRequestItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequestItem should not enable id FormControl', () => {
        const formGroup = service.createCustRequestItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequestItem should disable id FormControl', () => {
        const formGroup = service.createCustRequestItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
