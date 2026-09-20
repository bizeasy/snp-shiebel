import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request-type.test-samples';

import { CustRequestTypeFormService } from './cust-request-type-form.service';

describe('CustRequestType Form Service', () => {
  let service: CustRequestTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            parent: expect.any(Object),
            party: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequestType should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            parent: expect.any(Object),
            party: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequestType', () => {
      it('should return NewCustRequestType for default CustRequestType initial value', () => {
        const formGroup = service.createCustRequestTypeFormGroup(sampleWithNewData);

        const custRequestType = service.getCustRequestType(formGroup);

        expect(custRequestType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequestType for empty CustRequestType initial value', () => {
        const formGroup = service.createCustRequestTypeFormGroup();

        const custRequestType = service.getCustRequestType(formGroup);

        expect(custRequestType).toMatchObject({});
      });

      it('should return ICustRequestType', () => {
        const formGroup = service.createCustRequestTypeFormGroup(sampleWithRequiredData);

        const custRequestType = service.getCustRequestType(formGroup);

        expect(custRequestType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequestType should not enable id FormControl', () => {
        const formGroup = service.createCustRequestTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequestType should disable id FormControl', () => {
        const formGroup = service.createCustRequestTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
