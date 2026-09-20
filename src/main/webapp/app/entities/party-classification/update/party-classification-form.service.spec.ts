import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-classification.test-samples';

import { PartyClassificationFormService } from './party-classification-form.service';

describe('PartyClassification Form Service', () => {
  let service: PartyClassificationFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyClassificationFormService);
  });

  describe('Service methods', () => {
    describe('createPartyClassificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyClassificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            party: expect.any(Object),
            classificationGroup: expect.any(Object),
          }),
        );
      });

      it('passing IPartyClassification should create a new form with FormGroup', () => {
        const formGroup = service.createPartyClassificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
            party: expect.any(Object),
            classificationGroup: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyClassification', () => {
      it('should return NewPartyClassification for default PartyClassification initial value', () => {
        const formGroup = service.createPartyClassificationFormGroup(sampleWithNewData);

        const partyClassification = service.getPartyClassification(formGroup);

        expect(partyClassification).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyClassification for empty PartyClassification initial value', () => {
        const formGroup = service.createPartyClassificationFormGroup();

        const partyClassification = service.getPartyClassification(formGroup);

        expect(partyClassification).toMatchObject({});
      });

      it('should return IPartyClassification', () => {
        const formGroup = service.createPartyClassificationFormGroup(sampleWithRequiredData);

        const partyClassification = service.getPartyClassification(formGroup);

        expect(partyClassification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyClassification should not enable id FormControl', () => {
        const formGroup = service.createPartyClassificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyClassification should disable id FormControl', () => {
        const formGroup = service.createPartyClassificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
