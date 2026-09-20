import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../custom-time-period.test-samples';

import { CustomTimePeriodFormService } from './custom-time-period-form.service';

describe('CustomTimePeriod Form Service', () => {
  let service: CustomTimePeriodFormService;

  beforeEach(() => {
    service = TestBed.inject(CustomTimePeriodFormService);
  });

  describe('Service methods', () => {
    describe('createCustomTimePeriodFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomTimePeriodFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
          }),
        );
      });

      it('passing ICustomTimePeriod should create a new form with FormGroup', () => {
        const formGroup = service.createCustomTimePeriodFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fromDate: expect.any(Object),
            thruDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomTimePeriod', () => {
      it('should return NewCustomTimePeriod for default CustomTimePeriod initial value', () => {
        const formGroup = service.createCustomTimePeriodFormGroup(sampleWithNewData);

        const customTimePeriod = service.getCustomTimePeriod(formGroup);

        expect(customTimePeriod).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomTimePeriod for empty CustomTimePeriod initial value', () => {
        const formGroup = service.createCustomTimePeriodFormGroup();

        const customTimePeriod = service.getCustomTimePeriod(formGroup);

        expect(customTimePeriod).toMatchObject({});
      });

      it('should return ICustomTimePeriod', () => {
        const formGroup = service.createCustomTimePeriodFormGroup(sampleWithRequiredData);

        const customTimePeriod = service.getCustomTimePeriod(formGroup);

        expect(customTimePeriod).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomTimePeriod should not enable id FormControl', () => {
        const formGroup = service.createCustomTimePeriodFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomTimePeriod should disable id FormControl', () => {
        const formGroup = service.createCustomTimePeriodFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
