import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../location-type.test-samples';

import { LocationTypeFormService } from './location-type-form.service';

describe('LocationType Form Service', () => {
  let service: LocationTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(LocationTypeFormService);
  });

  describe('Service methods', () => {
    describe('createLocationTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocationTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ILocationType should create a new form with FormGroup', () => {
        const formGroup = service.createLocationTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getLocationType', () => {
      it('should return NewLocationType for default LocationType initial value', () => {
        const formGroup = service.createLocationTypeFormGroup(sampleWithNewData);

        const locationType = service.getLocationType(formGroup);

        expect(locationType).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocationType for empty LocationType initial value', () => {
        const formGroup = service.createLocationTypeFormGroup();

        const locationType = service.getLocationType(formGroup);

        expect(locationType).toMatchObject({});
      });

      it('should return ILocationType', () => {
        const formGroup = service.createLocationTypeFormGroup(sampleWithRequiredData);

        const locationType = service.getLocationType(formGroup);

        expect(locationType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocationType should not enable id FormControl', () => {
        const formGroup = service.createLocationTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocationType should disable id FormControl', () => {
        const formGroup = service.createLocationTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
