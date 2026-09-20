import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-relationship.test-samples';

import { PartyRelationshipFormService } from './party-relationship-form.service';

describe('PartyRelationship Form Service', () => {
  let service: PartyRelationshipFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyRelationshipFormService);
  });

  describe('Service methods', () => {
    describe('createPartyRelationshipFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyRelationshipFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            relationshipName: expect.any(Object),
            partyFrom: expect.any(Object),
            partyTo: expect.any(Object),
            roleTypeFrom: expect.any(Object),
            roleTypeTo: expect.any(Object),
            relationshipType: expect.any(Object),
          }),
        );
      });

      it('passing IPartyRelationship should create a new form with FormGroup', () => {
        const formGroup = service.createPartyRelationshipFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            relationshipName: expect.any(Object),
            partyFrom: expect.any(Object),
            partyTo: expect.any(Object),
            roleTypeFrom: expect.any(Object),
            roleTypeTo: expect.any(Object),
            relationshipType: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyRelationship', () => {
      it('should return NewPartyRelationship for default PartyRelationship initial value', () => {
        const formGroup = service.createPartyRelationshipFormGroup(sampleWithNewData);

        const partyRelationship = service.getPartyRelationship(formGroup);

        expect(partyRelationship).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyRelationship for empty PartyRelationship initial value', () => {
        const formGroup = service.createPartyRelationshipFormGroup();

        const partyRelationship = service.getPartyRelationship(formGroup);

        expect(partyRelationship).toMatchObject({});
      });

      it('should return IPartyRelationship', () => {
        const formGroup = service.createPartyRelationshipFormGroup(sampleWithRequiredData);

        const partyRelationship = service.getPartyRelationship(formGroup);

        expect(partyRelationship).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyRelationship should not enable id FormControl', () => {
        const formGroup = service.createPartyRelationshipFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyRelationship should disable id FormControl', () => {
        const formGroup = service.createPartyRelationshipFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
