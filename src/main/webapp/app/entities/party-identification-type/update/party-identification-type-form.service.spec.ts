import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-identification-type.test-samples';

import { PartyIdentificationTypeFormService } from './party-identification-type-form.service';

describe('PartyIdentificationType Form Service', () => {
  let service: PartyIdentificationTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyIdentificationTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPartyIdentificationTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IPartyIdentificationType should create a new form with FormGroup', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyIdentificationType', () => {
      it('should return NewPartyIdentificationType for default PartyIdentificationType initial value', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup(sampleWithNewData);

        const partyIdentificationType = service.getPartyIdentificationType(formGroup);

        expect(partyIdentificationType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyIdentificationType for empty PartyIdentificationType initial value', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup();

        const partyIdentificationType = service.getPartyIdentificationType(formGroup);

        expect(partyIdentificationType).toMatchObject({});
      });

      it('should return IPartyIdentificationType', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup(sampleWithRequiredData);

        const partyIdentificationType = service.getPartyIdentificationType(formGroup);

        expect(partyIdentificationType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyIdentificationType should not enable id FormControl', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyIdentificationType should disable id FormControl', () => {
        const formGroup = service.createPartyIdentificationTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
