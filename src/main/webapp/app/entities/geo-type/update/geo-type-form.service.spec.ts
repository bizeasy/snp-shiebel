import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../geo-type.test-samples';

import { GeoTypeFormService } from './geo-type-form.service';

describe('GeoType Form Service', () => {
  let service: GeoTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(GeoTypeFormService);
  });

  describe('Service methods', () => {
    describe('createGeoTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGeoTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IGeoType should create a new form with FormGroup', () => {
        const formGroup = service.createGeoTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getGeoType', () => {
      it('should return NewGeoType for default GeoType initial value', () => {
        const formGroup = service.createGeoTypeFormGroup(sampleWithNewData);

        const geoType = service.getGeoType(formGroup);

        expect(geoType).toMatchObject(sampleWithNewData);
      });

      it('should return NewGeoType for empty GeoType initial value', () => {
        const formGroup = service.createGeoTypeFormGroup();

        const geoType = service.getGeoType(formGroup);

        expect(geoType).toMatchObject({});
      });

      it('should return IGeoType', () => {
        const formGroup = service.createGeoTypeFormGroup(sampleWithRequiredData);

        const geoType = service.getGeoType(formGroup);

        expect(geoType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGeoType should not enable id FormControl', () => {
        const formGroup = service.createGeoTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGeoType should disable id FormControl', () => {
        const formGroup = service.createGeoTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
