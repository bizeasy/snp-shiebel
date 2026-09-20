import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request-category.test-samples';

import { CustRequestCategoryFormService } from './cust-request-category-form.service';

describe('CustRequestCategory Form Service', () => {
  let service: CustRequestCategoryFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            custRequestType: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequestCategory should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            custRequestType: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequestCategory', () => {
      it('should return NewCustRequestCategory for default CustRequestCategory initial value', () => {
        const formGroup = service.createCustRequestCategoryFormGroup(sampleWithNewData);

        const custRequestCategory = service.getCustRequestCategory(formGroup);

        expect(custRequestCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequestCategory for empty CustRequestCategory initial value', () => {
        const formGroup = service.createCustRequestCategoryFormGroup();

        const custRequestCategory = service.getCustRequestCategory(formGroup);

        expect(custRequestCategory).toMatchObject({});
      });

      it('should return ICustRequestCategory', () => {
        const formGroup = service.createCustRequestCategoryFormGroup(sampleWithRequiredData);

        const custRequestCategory = service.getCustRequestCategory(formGroup);

        expect(custRequestCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequestCategory should not enable id FormControl', () => {
        const formGroup = service.createCustRequestCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequestCategory should disable id FormControl', () => {
        const formGroup = service.createCustRequestCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
