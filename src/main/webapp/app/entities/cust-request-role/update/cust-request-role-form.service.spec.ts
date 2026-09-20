import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request-role.test-samples';

import { CustRequestRoleFormService } from './cust-request-role-form.service';

describe('CustRequestRole Form Service', () => {
  let service: CustRequestRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestRoleFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            custRequest: expect.any(Object),
            custRequestItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequestRole should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            custRequest: expect.any(Object),
            custRequestItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequestRole', () => {
      it('should return NewCustRequestRole for default CustRequestRole initial value', () => {
        const formGroup = service.createCustRequestRoleFormGroup(sampleWithNewData);

        const custRequestRole = service.getCustRequestRole(formGroup);

        expect(custRequestRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequestRole for empty CustRequestRole initial value', () => {
        const formGroup = service.createCustRequestRoleFormGroup();

        const custRequestRole = service.getCustRequestRole(formGroup);

        expect(custRequestRole).toMatchObject({});
      });

      it('should return ICustRequestRole', () => {
        const formGroup = service.createCustRequestRoleFormGroup(sampleWithRequiredData);

        const custRequestRole = service.getCustRequestRole(formGroup);

        expect(custRequestRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequestRole should not enable id FormControl', () => {
        const formGroup = service.createCustRequestRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequestRole should disable id FormControl', () => {
        const formGroup = service.createCustRequestRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
