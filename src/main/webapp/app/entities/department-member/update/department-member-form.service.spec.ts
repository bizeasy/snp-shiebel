import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../department-member.test-samples';

import { DepartmentMemberFormService } from './department-member-form.service';

describe('DepartmentMember Form Service', () => {
  let service: DepartmentMemberFormService;

  beforeEach(() => {
    service = TestBed.inject(DepartmentMemberFormService);
  });

  describe('Service methods', () => {
    describe('createDepartmentMemberFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDepartmentMemberFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            isPrimary: expect.any(Object),
            department: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IDepartmentMember should create a new form with FormGroup', () => {
        const formGroup = service.createDepartmentMemberFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            isPrimary: expect.any(Object),
            department: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getDepartmentMember', () => {
      it('should return NewDepartmentMember for default DepartmentMember initial value', () => {
        const formGroup = service.createDepartmentMemberFormGroup(sampleWithNewData);

        const departmentMember = service.getDepartmentMember(formGroup);

        expect(departmentMember).toMatchObject(sampleWithNewData);
      });

      it('should return NewDepartmentMember for empty DepartmentMember initial value', () => {
        const formGroup = service.createDepartmentMemberFormGroup();

        const departmentMember = service.getDepartmentMember(formGroup);

        expect(departmentMember).toMatchObject({});
      });

      it('should return IDepartmentMember', () => {
        const formGroup = service.createDepartmentMemberFormGroup(sampleWithRequiredData);

        const departmentMember = service.getDepartmentMember(formGroup);

        expect(departmentMember).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDepartmentMember should not enable id FormControl', () => {
        const formGroup = service.createDepartmentMemberFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDepartmentMember should disable id FormControl', () => {
        const formGroup = service.createDepartmentMemberFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
