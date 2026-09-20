import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-contact-mech.test-samples';

import { PartyContactMechFormService } from './party-contact-mech-form.service';

describe('PartyContactMech Form Service', () => {
  let service: PartyContactMechFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyContactMechFormService);
  });

  describe('Service methods', () => {
    describe('createPartyContactMechFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyContactMechFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            party: expect.any(Object),
            contactMech: expect.any(Object),
            contactMechPurpose: expect.any(Object),
          }),
        );
      });

      it('passing IPartyContactMech should create a new form with FormGroup', () => {
        const formGroup = service.createPartyContactMechFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            party: expect.any(Object),
            contactMech: expect.any(Object),
            contactMechPurpose: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyContactMech', () => {
      it('should return NewPartyContactMech for default PartyContactMech initial value', () => {
        const formGroup = service.createPartyContactMechFormGroup(sampleWithNewData);

        const partyContactMech = service.getPartyContactMech(formGroup);

        expect(partyContactMech).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyContactMech for empty PartyContactMech initial value', () => {
        const formGroup = service.createPartyContactMechFormGroup();

        const partyContactMech = service.getPartyContactMech(formGroup);

        expect(partyContactMech).toMatchObject({});
      });

      it('should return IPartyContactMech', () => {
        const formGroup = service.createPartyContactMechFormGroup(sampleWithRequiredData);

        const partyContactMech = service.getPartyContactMech(formGroup);

        expect(partyContactMech).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyContactMech should not enable id FormControl', () => {
        const formGroup = service.createPartyContactMechFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyContactMech should disable id FormControl', () => {
        const formGroup = service.createPartyContactMechFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
