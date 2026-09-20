import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../picklist-role.test-samples';

import { PicklistRoleFormService } from './picklist-role-form.service';

describe('PicklistRole Form Service', () => {
  let service: PicklistRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(PicklistRoleFormService);
  });

  describe('Service methods', () => {
    describe('createPicklistRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPicklistRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pickList: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IPicklistRole should create a new form with FormGroup', () => {
        const formGroup = service.createPicklistRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pickList: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getPicklistRole', () => {
      it('should return NewPicklistRole for default PicklistRole initial value', () => {
        const formGroup = service.createPicklistRoleFormGroup(sampleWithNewData);

        const picklistRole = service.getPicklistRole(formGroup);

        expect(picklistRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewPicklistRole for empty PicklistRole initial value', () => {
        const formGroup = service.createPicklistRoleFormGroup();

        const picklistRole = service.getPicklistRole(formGroup);

        expect(picklistRole).toMatchObject({});
      });

      it('should return IPicklistRole', () => {
        const formGroup = service.createPicklistRoleFormGroup(sampleWithRequiredData);

        const picklistRole = service.getPicklistRole(formGroup);

        expect(picklistRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPicklistRole should not enable id FormControl', () => {
        const formGroup = service.createPicklistRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPicklistRole should disable id FormControl', () => {
        const formGroup = service.createPicklistRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
