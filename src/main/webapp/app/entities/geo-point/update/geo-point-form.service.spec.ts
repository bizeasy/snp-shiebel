import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../geo-point.test-samples';

import { GeoPointFormService } from './geo-point-form.service';

describe('GeoPoint Form Service', () => {
  let service: GeoPointFormService;

  beforeEach(() => {
    service = TestBed.inject(GeoPointFormService);
  });

  describe('Service methods', () => {
    describe('createGeoPointFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGeoPointFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
          }),
        );
      });

      it('passing IGeoPoint should create a new form with FormGroup', () => {
        const formGroup = service.createGeoPointFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
          }),
        );
      });
    });

    describe('getGeoPoint', () => {
      it('should return NewGeoPoint for default GeoPoint initial value', () => {
        const formGroup = service.createGeoPointFormGroup(sampleWithNewData);

        const geoPoint = service.getGeoPoint(formGroup);

        expect(geoPoint).toMatchObject(sampleWithNewData);
      });

      it('should return NewGeoPoint for empty GeoPoint initial value', () => {
        const formGroup = service.createGeoPointFormGroup();

        const geoPoint = service.getGeoPoint(formGroup);

        expect(geoPoint).toMatchObject({});
      });

      it('should return IGeoPoint', () => {
        const formGroup = service.createGeoPointFormGroup(sampleWithRequiredData);

        const geoPoint = service.getGeoPoint(formGroup);

        expect(geoPoint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGeoPoint should not enable id FormControl', () => {
        const formGroup = service.createGeoPointFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGeoPoint should disable id FormControl', () => {
        const formGroup = service.createGeoPointFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
