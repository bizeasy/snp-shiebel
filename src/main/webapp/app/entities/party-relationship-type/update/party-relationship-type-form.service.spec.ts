import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-relationship-type.test-samples';

import { PartyRelationshipTypeFormService } from './party-relationship-type-form.service';

describe('PartyRelationshipType Form Service', () => {
  let service: PartyRelationshipTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyRelationshipTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPartyRelationshipTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            roleTypeFrom: expect.any(Object),
            roleTypeTo: expect.any(Object),
          }),
        );
      });

      it('passing IPartyRelationshipType should create a new form with FormGroup', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            roleTypeFrom: expect.any(Object),
            roleTypeTo: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyRelationshipType', () => {
      it('should return NewPartyRelationshipType for default PartyRelationshipType initial value', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup(sampleWithNewData);

        const partyRelationshipType = service.getPartyRelationshipType(formGroup);

        expect(partyRelationshipType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyRelationshipType for empty PartyRelationshipType initial value', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup();

        const partyRelationshipType = service.getPartyRelationshipType(formGroup);

        expect(partyRelationshipType).toMatchObject({});
      });

      it('should return IPartyRelationshipType', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup(sampleWithRequiredData);

        const partyRelationshipType = service.getPartyRelationshipType(formGroup);

        expect(partyRelationshipType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyRelationshipType should not enable id FormControl', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyRelationshipType should disable id FormControl', () => {
        const formGroup = service.createPartyRelationshipTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
