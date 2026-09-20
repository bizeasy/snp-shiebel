import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tax-authority-rate-type.test-samples';

import { TaxAuthorityRateTypeFormService } from './tax-authority-rate-type-form.service';

describe('TaxAuthorityRateType Form Service', () => {
  let service: TaxAuthorityRateTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(TaxAuthorityRateTypeFormService);
  });

  describe('Service methods', () => {
    describe('createTaxAuthorityRateTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            taxAuthority: expect.any(Object),
            taxSlab: expect.any(Object),
          }),
        );
      });

      it('passing ITaxAuthorityRateType should create a new form with FormGroup', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            taxAuthority: expect.any(Object),
            taxSlab: expect.any(Object),
          }),
        );
      });
    });

    describe('getTaxAuthorityRateType', () => {
      it('should return NewTaxAuthorityRateType for default TaxAuthorityRateType initial value', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup(sampleWithNewData);

        const taxAuthorityRateType = service.getTaxAuthorityRateType(formGroup);

        expect(taxAuthorityRateType).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaxAuthorityRateType for empty TaxAuthorityRateType initial value', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup();

        const taxAuthorityRateType = service.getTaxAuthorityRateType(formGroup);

        expect(taxAuthorityRateType).toMatchObject({});
      });

      it('should return ITaxAuthorityRateType', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup(sampleWithRequiredData);

        const taxAuthorityRateType = service.getTaxAuthorityRateType(formGroup);

        expect(taxAuthorityRateType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaxAuthorityRateType should not enable id FormControl', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaxAuthorityRateType should disable id FormControl', () => {
        const formGroup = service.createTaxAuthorityRateTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
