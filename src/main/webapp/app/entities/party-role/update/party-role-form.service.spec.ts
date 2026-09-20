import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-role.test-samples';

import { PartyRoleFormService } from './party-role-form.service';

describe('PartyRole Form Service', () => {
  let service: PartyRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyRoleFormService);
  });

  describe('Service methods', () => {
    describe('createPartyRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IPartyRole should create a new form with FormGroup', () => {
        const formGroup = service.createPartyRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyRole', () => {
      it('should return NewPartyRole for default PartyRole initial value', () => {
        const formGroup = service.createPartyRoleFormGroup(sampleWithNewData);

        const partyRole = service.getPartyRole(formGroup);

        expect(partyRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyRole for empty PartyRole initial value', () => {
        const formGroup = service.createPartyRoleFormGroup();

        const partyRole = service.getPartyRole(formGroup);

        expect(partyRole).toMatchObject({});
      });

      it('should return IPartyRole', () => {
        const formGroup = service.createPartyRoleFormGroup(sampleWithRequiredData);

        const partyRole = service.getPartyRole(formGroup);

        expect(partyRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyRole should not enable id FormControl', () => {
        const formGroup = service.createPartyRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyRole should disable id FormControl', () => {
        const formGroup = service.createPartyRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
