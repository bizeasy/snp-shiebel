import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-location.test-samples';

import { FacilityLocationFormService } from './facility-location-form.service';

describe('FacilityLocation Form Service', () => {
  let service: FacilityLocationFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityLocationFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            locationSeqId: expect.any(Object),
            areaId: expect.any(Object),
            aisleId: expect.any(Object),
            sectionId: expect.any(Object),
            levelId: expect.any(Object),
            positionId: expect.any(Object),
            facility: expect.any(Object),
            locationType: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityLocation should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            locationSeqId: expect.any(Object),
            areaId: expect.any(Object),
            aisleId: expect.any(Object),
            sectionId: expect.any(Object),
            levelId: expect.any(Object),
            positionId: expect.any(Object),
            facility: expect.any(Object),
            locationType: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityLocation', () => {
      it('should return NewFacilityLocation for default FacilityLocation initial value', () => {
        const formGroup = service.createFacilityLocationFormGroup(sampleWithNewData);

        const facilityLocation = service.getFacilityLocation(formGroup);

        expect(facilityLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityLocation for empty FacilityLocation initial value', () => {
        const formGroup = service.createFacilityLocationFormGroup();

        const facilityLocation = service.getFacilityLocation(formGroup);

        expect(facilityLocation).toMatchObject({});
      });

      it('should return IFacilityLocation', () => {
        const formGroup = service.createFacilityLocationFormGroup(sampleWithRequiredData);

        const facilityLocation = service.getFacilityLocation(formGroup);

        expect(facilityLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityLocation should not enable id FormControl', () => {
        const formGroup = service.createFacilityLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityLocation should disable id FormControl', () => {
        const formGroup = service.createFacilityLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
