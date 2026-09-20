import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party.test-samples';

import { PartyFormService } from './party-form.service';

describe('Party Form Service', () => {
  let service: PartyFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyFormService);
  });

  describe('Service methods', () => {
    describe('createPartyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isOrganisation: expect.any(Object),
            organisationName: expect.any(Object),
            organisationShortName: expect.any(Object),
            salutation: expect.any(Object),
            firstName: expect.any(Object),
            middleName: expect.any(Object),
            lastName: expect.any(Object),
            gender: expect.any(Object),
            birthDate: expect.any(Object),
            primaryPhone: expect.any(Object),
            primaryEmail: expect.any(Object),
            isTemporaryPassword: expect.any(Object),
            logoImageUrl: expect.any(Object),
            profileImageUrl: expect.any(Object),
            notes: expect.any(Object),
            postalAddress: expect.any(Object),
            user: expect.any(Object),
            partyType: expect.any(Object),
            status: expect.any(Object),
            address: expect.any(Object),
          }),
        );
      });

      it('passing IParty should create a new form with FormGroup', () => {
        const formGroup = service.createPartyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isOrganisation: expect.any(Object),
            organisationName: expect.any(Object),
            organisationShortName: expect.any(Object),
            salutation: expect.any(Object),
            firstName: expect.any(Object),
            middleName: expect.any(Object),
            lastName: expect.any(Object),
            gender: expect.any(Object),
            birthDate: expect.any(Object),
            primaryPhone: expect.any(Object),
            primaryEmail: expect.any(Object),
            isTemporaryPassword: expect.any(Object),
            logoImageUrl: expect.any(Object),
            profileImageUrl: expect.any(Object),
            notes: expect.any(Object),
            postalAddress: expect.any(Object),
            user: expect.any(Object),
            partyType: expect.any(Object),
            status: expect.any(Object),
            address: expect.any(Object),
          }),
        );
      });
    });

    describe('getParty', () => {
      it('should return NewParty for default Party initial value', () => {
        const formGroup = service.createPartyFormGroup(sampleWithNewData);

        const party = service.getParty(formGroup);

        expect(party).toMatchObject(sampleWithNewData);
      });

      it('should return NewParty for empty Party initial value', () => {
        const formGroup = service.createPartyFormGroup();

        const party = service.getParty(formGroup);

        expect(party).toMatchObject({});
      });

      it('should return IParty', () => {
        const formGroup = service.createPartyFormGroup(sampleWithRequiredData);

        const party = service.getParty(formGroup);

        expect(party).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParty should not enable id FormControl', () => {
        const formGroup = service.createPartyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParty should disable id FormControl', () => {
        const formGroup = service.createPartyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
