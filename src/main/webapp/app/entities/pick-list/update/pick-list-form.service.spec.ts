import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pick-list.test-samples';

import { PickListFormService } from './pick-list-form.service';

describe('PickList Form Service', () => {
  let service: PickListFormService;

  beforeEach(() => {
    service = TestBed.inject(PickListFormService);
  });

  describe('Service methods', () => {
    describe('createPickListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPickListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            pickListDate: expect.any(Object),
            facility: expect.any(Object),
            status: expect.any(Object),
            party: expect.any(Object),
          }),
        );
      });

      it('passing IPickList should create a new form with FormGroup', () => {
        const formGroup = service.createPickListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            pickListDate: expect.any(Object),
            facility: expect.any(Object),
            status: expect.any(Object),
            party: expect.any(Object),
          }),
        );
      });
    });

    describe('getPickList', () => {
      it('should return NewPickList for default PickList initial value', () => {
        const formGroup = service.createPickListFormGroup(sampleWithNewData);

        const pickList = service.getPickList(formGroup);

        expect(pickList).toMatchObject(sampleWithNewData);
      });

      it('should return NewPickList for empty PickList initial value', () => {
        const formGroup = service.createPickListFormGroup();

        const pickList = service.getPickList(formGroup);

        expect(pickList).toMatchObject({});
      });

      it('should return IPickList', () => {
        const formGroup = service.createPickListFormGroup(sampleWithRequiredData);

        const pickList = service.getPickList(formGroup);

        expect(pickList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPickList should not enable id FormControl', () => {
        const formGroup = service.createPickListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPickList should disable id FormControl', () => {
        const formGroup = service.createPickListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
