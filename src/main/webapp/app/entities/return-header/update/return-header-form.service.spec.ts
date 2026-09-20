import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../return-header.test-samples';

import { ReturnHeaderFormService } from './return-header-form.service';

describe('ReturnHeader Form Service', () => {
  let service: ReturnHeaderFormService;

  beforeEach(() => {
    service = TestBed.inject(ReturnHeaderFormService);
  });

  describe('Service methods', () => {
    describe('createReturnHeaderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReturnHeaderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            needsInventoryReceive: expect.any(Object),
            supplierRmaId: expect.any(Object),
            returnType: expect.any(Object),
            status: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            paymentMethod: expect.any(Object),
            originContactMech: expect.any(Object),
            destinationFacility: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });

      it('passing IReturnHeader should create a new form with FormGroup', () => {
        const formGroup = service.createReturnHeaderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            needsInventoryReceive: expect.any(Object),
            supplierRmaId: expect.any(Object),
            returnType: expect.any(Object),
            status: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            paymentMethod: expect.any(Object),
            originContactMech: expect.any(Object),
            destinationFacility: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getReturnHeader', () => {
      it('should return NewReturnHeader for default ReturnHeader initial value', () => {
        const formGroup = service.createReturnHeaderFormGroup(sampleWithNewData);

        const returnHeader = service.getReturnHeader(formGroup);

        expect(returnHeader).toMatchObject(sampleWithNewData);
      });

      it('should return NewReturnHeader for empty ReturnHeader initial value', () => {
        const formGroup = service.createReturnHeaderFormGroup();

        const returnHeader = service.getReturnHeader(formGroup);

        expect(returnHeader).toMatchObject({});
      });

      it('should return IReturnHeader', () => {
        const formGroup = service.createReturnHeaderFormGroup(sampleWithRequiredData);

        const returnHeader = service.getReturnHeader(formGroup);

        expect(returnHeader).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReturnHeader should not enable id FormControl', () => {
        const formGroup = service.createReturnHeaderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReturnHeader should disable id FormControl', () => {
        const formGroup = service.createReturnHeaderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
