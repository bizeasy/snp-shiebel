import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../geo.test-samples';

import { GeoFormService } from './geo-form.service';

describe('Geo Form Service', () => {
  let service: GeoFormService;

  beforeEach(() => {
    service = TestBed.inject(GeoFormService);
  });

  describe('Service methods', () => {
    describe('createGeoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGeoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            abbreviation: expect.any(Object),
            geoType: expect.any(Object),
          }),
        );
      });

      it('passing IGeo should create a new form with FormGroup', () => {
        const formGroup = service.createGeoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            abbreviation: expect.any(Object),
            geoType: expect.any(Object),
          }),
        );
      });
    });

    describe('getGeo', () => {
      it('should return NewGeo for default Geo initial value', () => {
        const formGroup = service.createGeoFormGroup(sampleWithNewData);

        const geo = service.getGeo(formGroup);

        expect(geo).toMatchObject(sampleWithNewData);
      });

      it('should return NewGeo for empty Geo initial value', () => {
        const formGroup = service.createGeoFormGroup();

        const geo = service.getGeo(formGroup);

        expect(geo).toMatchObject({});
      });

      it('should return IGeo', () => {
        const formGroup = service.createGeoFormGroup(sampleWithRequiredData);

        const geo = service.getGeo(formGroup);

        expect(geo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGeo should not enable id FormControl', () => {
        const formGroup = service.createGeoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGeo should disable id FormControl', () => {
        const formGroup = service.createGeoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
