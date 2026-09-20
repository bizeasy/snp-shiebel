import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-classification-type.test-samples';

import { PartyClassificationTypeFormService } from './party-classification-type-form.service';

describe('PartyClassificationType Form Service', () => {
  let service: PartyClassificationTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyClassificationTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPartyClassificationTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IPartyClassificationType should create a new form with FormGroup', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyClassificationType', () => {
      it('should return NewPartyClassificationType for default PartyClassificationType initial value', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup(sampleWithNewData);

        const partyClassificationType = service.getPartyClassificationType(formGroup);

        expect(partyClassificationType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyClassificationType for empty PartyClassificationType initial value', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup();

        const partyClassificationType = service.getPartyClassificationType(formGroup);

        expect(partyClassificationType).toMatchObject({});
      });

      it('should return IPartyClassificationType', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup(sampleWithRequiredData);

        const partyClassificationType = service.getPartyClassificationType(formGroup);

        expect(partyClassificationType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyClassificationType should not enable id FormControl', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyClassificationType should disable id FormControl', () => {
        const formGroup = service.createPartyClassificationTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
