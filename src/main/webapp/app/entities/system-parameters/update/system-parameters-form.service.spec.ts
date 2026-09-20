import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../system-parameters.test-samples';

import { SystemParametersFormService } from './system-parameters-form.service';

describe('SystemParameters Form Service', () => {
  let service: SystemParametersFormService;

  beforeEach(() => {
    service = TestBed.inject(SystemParametersFormService);
  });

  describe('Service methods', () => {
    describe('createSystemParametersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSystemParametersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            key: expect.any(Object),
            value: expect.any(Object),
            description: expect.any(Object),
            environment: expect.any(Object),
          }),
        );
      });

      it('passing ISystemParameters should create a new form with FormGroup', () => {
        const formGroup = service.createSystemParametersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            key: expect.any(Object),
            value: expect.any(Object),
            description: expect.any(Object),
            environment: expect.any(Object),
          }),
        );
      });
    });

    describe('getSystemParameters', () => {
      it('should return NewSystemParameters for default SystemParameters initial value', () => {
        const formGroup = service.createSystemParametersFormGroup(sampleWithNewData);

        const systemParameters = service.getSystemParameters(formGroup);

        expect(systemParameters).toMatchObject(sampleWithNewData);
      });

      it('should return NewSystemParameters for empty SystemParameters initial value', () => {
        const formGroup = service.createSystemParametersFormGroup();

        const systemParameters = service.getSystemParameters(formGroup);

        expect(systemParameters).toMatchObject({});
      });

      it('should return ISystemParameters', () => {
        const formGroup = service.createSystemParametersFormGroup(sampleWithRequiredData);

        const systemParameters = service.getSystemParameters(formGroup);

        expect(systemParameters).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISystemParameters should not enable id FormControl', () => {
        const formGroup = service.createSystemParametersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSystemParameters should disable id FormControl', () => {
        const formGroup = service.createSystemParametersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
