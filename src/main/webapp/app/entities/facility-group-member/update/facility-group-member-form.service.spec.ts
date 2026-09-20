import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-group-member.test-samples';

import { FacilityGroupMemberFormService } from './facility-group-member-form.service';

describe('FacilityGroupMember Form Service', () => {
  let service: FacilityGroupMemberFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityGroupMemberFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityGroupMemberFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            facility: expect.any(Object),
            facilityGroup: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityGroupMember should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            sequenceNo: expect.any(Object),
            facility: expect.any(Object),
            facilityGroup: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityGroupMember', () => {
      it('should return NewFacilityGroupMember for default FacilityGroupMember initial value', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup(sampleWithNewData);

        const facilityGroupMember = service.getFacilityGroupMember(formGroup);

        expect(facilityGroupMember).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityGroupMember for empty FacilityGroupMember initial value', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup();

        const facilityGroupMember = service.getFacilityGroupMember(formGroup);

        expect(facilityGroupMember).toMatchObject({});
      });

      it('should return IFacilityGroupMember', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup(sampleWithRequiredData);

        const facilityGroupMember = service.getFacilityGroupMember(formGroup);

        expect(facilityGroupMember).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityGroupMember should not enable id FormControl', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityGroupMember should disable id FormControl', () => {
        const formGroup = service.createFacilityGroupMemberFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
