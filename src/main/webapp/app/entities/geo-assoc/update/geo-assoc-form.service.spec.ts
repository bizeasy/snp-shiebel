import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../geo-assoc.test-samples';

import { GeoAssocFormService } from './geo-assoc-form.service';

describe('GeoAssoc Form Service', () => {
  let service: GeoAssocFormService;

  beforeEach(() => {
    service = TestBed.inject(GeoAssocFormService);
  });

  describe('Service methods', () => {
    describe('createGeoAssocFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGeoAssocFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            geo: expect.any(Object),
            geoTo: expect.any(Object),
            geoAssocType: expect.any(Object),
          }),
        );
      });

      it('passing IGeoAssoc should create a new form with FormGroup', () => {
        const formGroup = service.createGeoAssocFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            geo: expect.any(Object),
            geoTo: expect.any(Object),
            geoAssocType: expect.any(Object),
          }),
        );
      });
    });

    describe('getGeoAssoc', () => {
      it('should return NewGeoAssoc for default GeoAssoc initial value', () => {
        const formGroup = service.createGeoAssocFormGroup(sampleWithNewData);

        const geoAssoc = service.getGeoAssoc(formGroup);

        expect(geoAssoc).toMatchObject(sampleWithNewData);
      });

      it('should return NewGeoAssoc for empty GeoAssoc initial value', () => {
        const formGroup = service.createGeoAssocFormGroup();

        const geoAssoc = service.getGeoAssoc(formGroup);

        expect(geoAssoc).toMatchObject({});
      });

      it('should return IGeoAssoc', () => {
        const formGroup = service.createGeoAssocFormGroup(sampleWithRequiredData);

        const geoAssoc = service.getGeoAssoc(formGroup);

        expect(geoAssoc).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGeoAssoc should not enable id FormControl', () => {
        const formGroup = service.createGeoAssocFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGeoAssoc should disable id FormControl', () => {
        const formGroup = service.createGeoAssocFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
