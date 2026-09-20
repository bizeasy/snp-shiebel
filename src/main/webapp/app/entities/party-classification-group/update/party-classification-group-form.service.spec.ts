import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../party-classification-group.test-samples';

import { PartyClassificationGroupFormService } from './party-classification-group-form.service';

describe('PartyClassificationGroup Form Service', () => {
  let service: PartyClassificationGroupFormService;

  beforeEach(() => {
    service = TestBed.inject(PartyClassificationGroupFormService);
  });

  describe('Service methods', () => {
    describe('createPartyClassificationGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            classificationType: expect.any(Object),
          }),
        );
      });

      it('passing IPartyClassificationGroup should create a new form with FormGroup', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            classificationType: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartyClassificationGroup', () => {
      it('should return NewPartyClassificationGroup for default PartyClassificationGroup initial value', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup(sampleWithNewData);

        const partyClassificationGroup = service.getPartyClassificationGroup(formGroup);

        expect(partyClassificationGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartyClassificationGroup for empty PartyClassificationGroup initial value', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup();

        const partyClassificationGroup = service.getPartyClassificationGroup(formGroup);

        expect(partyClassificationGroup).toMatchObject({});
      });

      it('should return IPartyClassificationGroup', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup(sampleWithRequiredData);

        const partyClassificationGroup = service.getPartyClassificationGroup(formGroup);

        expect(partyClassificationGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartyClassificationGroup should not enable id FormControl', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartyClassificationGroup should disable id FormControl', () => {
        const formGroup = service.createPartyClassificationGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
