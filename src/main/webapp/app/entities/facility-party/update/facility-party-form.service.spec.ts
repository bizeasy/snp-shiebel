import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-party.test-samples';

import { FacilityPartyFormService } from './facility-party-form.service';

describe('FacilityParty Form Service', () => {
  let service: FacilityPartyFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityPartyFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityPartyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityPartyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            facility: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityParty should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityPartyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            facility: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityParty', () => {
      it('should return NewFacilityParty for default FacilityParty initial value', () => {
        const formGroup = service.createFacilityPartyFormGroup(sampleWithNewData);

        const facilityParty = service.getFacilityParty(formGroup);

        expect(facilityParty).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityParty for empty FacilityParty initial value', () => {
        const formGroup = service.createFacilityPartyFormGroup();

        const facilityParty = service.getFacilityParty(formGroup);

        expect(facilityParty).toMatchObject({});
      });

      it('should return IFacilityParty', () => {
        const formGroup = service.createFacilityPartyFormGroup(sampleWithRequiredData);

        const facilityParty = service.getFacilityParty(formGroup);

        expect(facilityParty).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityParty should not enable id FormControl', () => {
        const formGroup = service.createFacilityPartyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityParty should disable id FormControl', () => {
        const formGroup = service.createFacilityPartyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
