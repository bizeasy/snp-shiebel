import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-identification.test-samples';

import { PartyIdentificationFormService } from './party-identification-form.service';

describe('PartyIdentification Form Service', () => {
  let service: PartyIdentificationFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyIdentificationFormService);
  });

  describe('Service methods', () => {
    describe('createPartyIdentificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyIdentificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idValue: expect.any(Object),
            party: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });

      it('passing IPartyIdentification should create a new form with FormGroup', () => {
        const formGroup = service.createPartyIdentificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idValue: expect.any(Object),
            party: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyIdentification', () => {
      it('should return NewPartyIdentification for default PartyIdentification initial value', () => {
        const formGroup = service.createPartyIdentificationFormGroup(sampleWithNewData);

        const partyIdentification = service.getPartyIdentification(formGroup);

        expect(partyIdentification).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyIdentification for empty PartyIdentification initial value', () => {
        const formGroup = service.createPartyIdentificationFormGroup();

        const partyIdentification = service.getPartyIdentification(formGroup);

        expect(partyIdentification).toMatchObject({});
      });

      it('should return IPartyIdentification', () => {
        const formGroup = service.createPartyIdentificationFormGroup(sampleWithRequiredData);

        const partyIdentification = service.getPartyIdentification(formGroup);

        expect(partyIdentification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyIdentification should not enable id FormControl', () => {
        const formGroup = service.createPartyIdentificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyIdentification should disable id FormControl', () => {
        const formGroup = service.createPartyIdentificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
