import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request.test-samples';

import { CustRequestFormService } from './cust-request-form.service';

describe('CustRequest Form Service', () => {
  let service: CustRequestFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            priority: expect.any(Object),
            custRequestDate: expect.any(Object),
            responseRequiredDate: expect.any(Object),
            openDateTime: expect.any(Object),
            closedDateTime: expect.any(Object),
            internalComment: expect.any(Object),
            billed: expect.any(Object),
            custEstimatedDurationDays: expect.any(Object),
            custRequestType: expect.any(Object),
            custRequestCategory: expect.any(Object),
            status: expect.any(Object),
            fromParty: expect.any(Object),
            productStore: expect.any(Object),
            salesChannel: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequest should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            priority: expect.any(Object),
            custRequestDate: expect.any(Object),
            responseRequiredDate: expect.any(Object),
            openDateTime: expect.any(Object),
            closedDateTime: expect.any(Object),
            internalComment: expect.any(Object),
            billed: expect.any(Object),
            custEstimatedDurationDays: expect.any(Object),
            custRequestType: expect.any(Object),
            custRequestCategory: expect.any(Object),
            status: expect.any(Object),
            fromParty: expect.any(Object),
            productStore: expect.any(Object),
            salesChannel: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequest', () => {
      it('should return NewCustRequest for default CustRequest initial value', () => {
        const formGroup = service.createCustRequestFormGroup(sampleWithNewData);

        const custRequest = service.getCustRequest(formGroup);

        expect(custRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequest for empty CustRequest initial value', () => {
        const formGroup = service.createCustRequestFormGroup();

        const custRequest = service.getCustRequest(formGroup);

        expect(custRequest).toMatchObject({});
      });

      it('should return ICustRequest', () => {
        const formGroup = service.createCustRequestFormGroup(sampleWithRequiredData);

        const custRequest = service.getCustRequest(formGroup);

        expect(custRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequest should not enable id FormControl', () => {
        const formGroup = service.createCustRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequest should disable id FormControl', () => {
        const formGroup = service.createCustRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
