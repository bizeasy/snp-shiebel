import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../return-header-type.test-samples';

import { ReturnHeaderTypeFormService } from './return-header-type-form.service';

describe('ReturnHeaderType Form Service', () => {
  let service: ReturnHeaderTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ReturnHeaderTypeFormService);
  });

  describe('Service methods', () => {
    describe('createReturnHeaderTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IReturnHeaderType should create a new form with FormGroup', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getReturnHeaderType', () => {
      it('should return NewReturnHeaderType for default ReturnHeaderType initial value', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup(sampleWithNewData);

        const returnHeaderType = service.getReturnHeaderType(formGroup);

        expect(returnHeaderType).toMatchObject(sampleWithNewData);
      });

      it('should return NewReturnHeaderType for empty ReturnHeaderType initial value', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup();

        const returnHeaderType = service.getReturnHeaderType(formGroup);

        expect(returnHeaderType).toMatchObject({});
      });

      it('should return IReturnHeaderType', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup(sampleWithRequiredData);

        const returnHeaderType = service.getReturnHeaderType(formGroup);

        expect(returnHeaderType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReturnHeaderType should not enable id FormControl', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReturnHeaderType should disable id FormControl', () => {
        const formGroup = service.createReturnHeaderTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
