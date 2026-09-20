import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../uom-type.test-samples';

import { UomTypeFormService } from './uom-type-form.service';

describe('UomType Form Service', () => {
  let service: UomTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(UomTypeFormService);
  });

  describe('Service methods', () => {
    describe('createUomTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUomTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IUomType should create a new form with FormGroup', () => {
        const formGroup = service.createUomTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getUomType', () => {
      it('should return NewUomType for default UomType initial value', () => {
        const formGroup = service.createUomTypeFormGroup(sampleWithNewData);

        const uomType = service.getUomType(formGroup);

        expect(uomType).toMatchObject(sampleWithNewData);
      });

      it('should return NewUomType for empty UomType initial value', () => {
        const formGroup = service.createUomTypeFormGroup();

        const uomType = service.getUomType(formGroup);

        expect(uomType).toMatchObject({});
      });

      it('should return IUomType', () => {
        const formGroup = service.createUomTypeFormGroup(sampleWithRequiredData);

        const uomType = service.getUomType(formGroup);

        expect(uomType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUomType should not enable id FormControl', () => {
        const formGroup = service.createUomTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUomType should disable id FormControl', () => {
        const formGroup = service.createUomTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
