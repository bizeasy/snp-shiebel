import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../uom.test-samples';

import { UomFormService } from './uom-form.service';

describe('Uom Form Service', () => {
  let service: UomFormService;

  beforeEach(() => {
    service = TestBed.inject(UomFormService);
  });

  describe('Service methods', () => {
    describe('createUomFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUomFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            sequenceNo: expect.any(Object),
            abbreviation: expect.any(Object),
            uomType: expect.any(Object),
          }),
        );
      });

      it('passing IUom should create a new form with FormGroup', () => {
        const formGroup = service.createUomFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            sequenceNo: expect.any(Object),
            abbreviation: expect.any(Object),
            uomType: expect.any(Object),
          }),
        );
      });
    });

    describe('getUom', () => {
      it('should return NewUom for default Uom initial value', () => {
        const formGroup = service.createUomFormGroup(sampleWithNewData);

        const uom = service.getUom(formGroup);

        expect(uom).toMatchObject(sampleWithNewData);
      });

      it('should return NewUom for empty Uom initial value', () => {
        const formGroup = service.createUomFormGroup();

        const uom = service.getUom(formGroup);

        expect(uom).toMatchObject({});
      });

      it('should return IUom', () => {
        const formGroup = service.createUomFormGroup(sampleWithRequiredData);

        const uom = service.getUom(formGroup);

        expect(uom).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUom should not enable id FormControl', () => {
        const formGroup = service.createUomFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUom should disable id FormControl', () => {
        const formGroup = service.createUomFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
