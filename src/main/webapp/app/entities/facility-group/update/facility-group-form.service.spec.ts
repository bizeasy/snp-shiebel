import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-group.test-samples';

import { FacilityGroupFormService } from './facility-group-form.service';

describe('FacilityGroup Form Service', () => {
  let service: FacilityGroupFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityGroupFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            facilityGroupType: expect.any(Object),
            parentFacilityGroup: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityGroup should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            facilityGroupType: expect.any(Object),
            parentFacilityGroup: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityGroup', () => {
      it('should return NewFacilityGroup for default FacilityGroup initial value', () => {
        const formGroup = service.createFacilityGroupFormGroup(sampleWithNewData);

        const facilityGroup = service.getFacilityGroup(formGroup);

        expect(facilityGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityGroup for empty FacilityGroup initial value', () => {
        const formGroup = service.createFacilityGroupFormGroup();

        const facilityGroup = service.getFacilityGroup(formGroup);

        expect(facilityGroup).toMatchObject({});
      });

      it('should return IFacilityGroup', () => {
        const formGroup = service.createFacilityGroupFormGroup(sampleWithRequiredData);

        const facilityGroup = service.getFacilityGroup(formGroup);

        expect(facilityGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityGroup should not enable id FormControl', () => {
        const formGroup = service.createFacilityGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityGroup should disable id FormControl', () => {
        const formGroup = service.createFacilityGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
