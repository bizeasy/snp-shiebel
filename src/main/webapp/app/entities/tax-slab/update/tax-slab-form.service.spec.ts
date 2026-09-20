import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tax-slab.test-samples';

import { TaxSlabFormService } from './tax-slab-form.service';

describe('TaxSlab Form Service', () => {
  let service: TaxSlabFormService;

  beforeEach(() => {
    service = TestBed.inject(TaxSlabFormService);
  });

  describe('Service methods', () => {
    describe('createTaxSlabFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTaxSlabFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            rate: expect.any(Object),
          }),
        );
      });

      it('passing ITaxSlab should create a new form with FormGroup', () => {
        const formGroup = service.createTaxSlabFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            rate: expect.any(Object),
          }),
        );
      });
    });

    describe('getTaxSlab', () => {
      it('should return NewTaxSlab for default TaxSlab initial value', () => {
        const formGroup = service.createTaxSlabFormGroup(sampleWithNewData);

        const taxSlab = service.getTaxSlab(formGroup);

        expect(taxSlab).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaxSlab for empty TaxSlab initial value', () => {
        const formGroup = service.createTaxSlabFormGroup();

        const taxSlab = service.getTaxSlab(formGroup);

        expect(taxSlab).toMatchObject({});
      });

      it('should return ITaxSlab', () => {
        const formGroup = service.createTaxSlabFormGroup(sampleWithRequiredData);

        const taxSlab = service.getTaxSlab(formGroup);

        expect(taxSlab).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaxSlab should not enable id FormControl', () => {
        const formGroup = service.createTaxSlabFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaxSlab should disable id FormControl', () => {
        const formGroup = service.createTaxSlabFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
