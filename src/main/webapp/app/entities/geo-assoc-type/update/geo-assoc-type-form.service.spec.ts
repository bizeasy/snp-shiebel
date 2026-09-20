import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../geo-assoc-type.test-samples';

import { GeoAssocTypeFormService } from './geo-assoc-type-form.service';

describe('GeoAssocType Form Service', () => {
  let service: GeoAssocTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(GeoAssocTypeFormService);
  });

  describe('Service methods', () => {
    describe('createGeoAssocTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGeoAssocTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IGeoAssocType should create a new form with FormGroup', () => {
        const formGroup = service.createGeoAssocTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getGeoAssocType', () => {
      it('should return NewGeoAssocType for default GeoAssocType initial value', () => {
        const formGroup = service.createGeoAssocTypeFormGroup(sampleWithNewData);

        const geoAssocType = service.getGeoAssocType(formGroup);

        expect(geoAssocType).toMatchObject(sampleWithNewData);
      });

      it('should return NewGeoAssocType for empty GeoAssocType initial value', () => {
        const formGroup = service.createGeoAssocTypeFormGroup();

        const geoAssocType = service.getGeoAssocType(formGroup);

        expect(geoAssocType).toMatchObject({});
      });

      it('should return IGeoAssocType', () => {
        const formGroup = service.createGeoAssocTypeFormGroup(sampleWithRequiredData);

        const geoAssocType = service.getGeoAssocType(formGroup);

        expect(geoAssocType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGeoAssocType should not enable id FormControl', () => {
        const formGroup = service.createGeoAssocTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGeoAssocType should disable id FormControl', () => {
        const formGroup = service.createGeoAssocTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
