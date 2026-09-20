import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../role-type.test-samples';

import { RoleTypeFormService } from './role-type-form.service';

describe('RoleType Form Service', () => {
  let service: RoleTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(RoleTypeFormService);
  });

  describe('Service methods', () => {
    describe('createRoleTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoleTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IRoleType should create a new form with FormGroup', () => {
        const formGroup = service.createRoleTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getRoleType', () => {
      it('should return NewRoleType for default RoleType initial value', () => {
        const formGroup = service.createRoleTypeFormGroup(sampleWithNewData);

        const roleType = service.getRoleType(formGroup);

        expect(roleType).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoleType for empty RoleType initial value', () => {
        const formGroup = service.createRoleTypeFormGroup();

        const roleType = service.getRoleType(formGroup);

        expect(roleType).toMatchObject({});
      });

      it('should return IRoleType', () => {
        const formGroup = service.createRoleTypeFormGroup(sampleWithRequiredData);

        const roleType = service.getRoleType(formGroup);

        expect(roleType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoleType should not enable id FormControl', () => {
        const formGroup = service.createRoleTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoleType should disable id FormControl', () => {
        const formGroup = service.createRoleTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
