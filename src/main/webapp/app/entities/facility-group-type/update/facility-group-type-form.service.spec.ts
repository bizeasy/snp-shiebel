import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-group-type.test-samples';

import { FacilityGroupTypeFormService } from './facility-group-type-form.service';

describe('FacilityGroupType Form Service', () => {
  let service: FacilityGroupTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityGroupTypeFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityGroupTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityGroupType should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityGroupType', () => {
      it('should return NewFacilityGroupType for default FacilityGroupType initial value', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup(sampleWithNewData);

        const facilityGroupType = service.getFacilityGroupType(formGroup);

        expect(facilityGroupType).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityGroupType for empty FacilityGroupType initial value', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup();

        const facilityGroupType = service.getFacilityGroupType(formGroup);

        expect(facilityGroupType).toMatchObject({});
      });

      it('should return IFacilityGroupType', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup(sampleWithRequiredData);

        const facilityGroupType = service.getFacilityGroupType(formGroup);

        expect(facilityGroupType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityGroupType should not enable id FormControl', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityGroupType should disable id FormControl', () => {
        const formGroup = service.createFacilityGroupTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
