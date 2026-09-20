import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-contact-mech.test-samples';

import { FacilityContactMechFormService } from './facility-contact-mech-form.service';

describe('FacilityContactMech Form Service', () => {
  let service: FacilityContactMechFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityContactMechFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityContactMechFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityContactMechFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            facility: expect.any(Object),
            contactMech: expect.any(Object),
            contactMechPurpose: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityContactMech should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityContactMechFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            facility: expect.any(Object),
            contactMech: expect.any(Object),
            contactMechPurpose: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityContactMech', () => {
      it('should return NewFacilityContactMech for default FacilityContactMech initial value', () => {
        const formGroup = service.createFacilityContactMechFormGroup(sampleWithNewData);

        const facilityContactMech = service.getFacilityContactMech(formGroup);

        expect(facilityContactMech).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityContactMech for empty FacilityContactMech initial value', () => {
        const formGroup = service.createFacilityContactMechFormGroup();

        const facilityContactMech = service.getFacilityContactMech(formGroup);

        expect(facilityContactMech).toMatchObject({});
      });

      it('should return IFacilityContactMech', () => {
        const formGroup = service.createFacilityContactMechFormGroup(sampleWithRequiredData);

        const facilityContactMech = service.getFacilityContactMech(formGroup);

        expect(facilityContactMech).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityContactMech should not enable id FormControl', () => {
        const formGroup = service.createFacilityContactMechFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityContactMech should disable id FormControl', () => {
        const formGroup = service.createFacilityContactMechFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
