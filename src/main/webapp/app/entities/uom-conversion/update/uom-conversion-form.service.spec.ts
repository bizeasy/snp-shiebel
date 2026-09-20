import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../uom-conversion.test-samples';

import { UomConversionFormService } from './uom-conversion-form.service';

describe('UomConversion Form Service', () => {
  let service: UomConversionFormService;

  beforeEach(() => {
    service = TestBed.inject(UomConversionFormService);
  });

  describe('Service methods', () => {
    describe('createUomConversionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUomConversionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            conversionFactor: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            fromUom: expect.any(Object),
            toUom: expect.any(Object),
          }),
        );
      });

      it('passing IUomConversion should create a new form with FormGroup', () => {
        const formGroup = service.createUomConversionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            conversionFactor: expect.any(Object),
            fromDate: expect.any(Object),
            toDate: expect.any(Object),
            fromUom: expect.any(Object),
            toUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getUomConversion', () => {
      it('should return NewUomConversion for default UomConversion initial value', () => {
        const formGroup = service.createUomConversionFormGroup(sampleWithNewData);

        const uomConversion = service.getUomConversion(formGroup);

        expect(uomConversion).toMatchObject(sampleWithNewData);
      });

      it('should return NewUomConversion for empty UomConversion initial value', () => {
        const formGroup = service.createUomConversionFormGroup();

        const uomConversion = service.getUomConversion(formGroup);

        expect(uomConversion).toMatchObject({});
      });

      it('should return IUomConversion', () => {
        const formGroup = service.createUomConversionFormGroup(sampleWithRequiredData);

        const uomConversion = service.getUomConversion(formGroup);

        expect(uomConversion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUomConversion should not enable id FormControl', () => {
        const formGroup = service.createUomConversionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUomConversion should disable id FormControl', () => {
        const formGroup = service.createUomConversionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
