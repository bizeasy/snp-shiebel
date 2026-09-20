import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../status-valid-change.test-samples';

import { StatusValidChangeFormService } from './status-valid-change-form.service';

describe('StatusValidChange Form Service', () => {
  let service: StatusValidChangeFormService;

  beforeEach(() => {
    service = TestBed.inject(StatusValidChangeFormService);
  });

  describe('Service methods', () => {
    describe('createStatusValidChangeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStatusValidChangeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transitionName: expect.any(Object),
            status: expect.any(Object),
            statusTo: expect.any(Object),
          }),
        );
      });

      it('passing IStatusValidChange should create a new form with FormGroup', () => {
        const formGroup = service.createStatusValidChangeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transitionName: expect.any(Object),
            status: expect.any(Object),
            statusTo: expect.any(Object),
          }),
        );
      });
    });

    describe('getStatusValidChange', () => {
      it('should return NewStatusValidChange for default StatusValidChange initial value', () => {
        const formGroup = service.createStatusValidChangeFormGroup(sampleWithNewData);

        const statusValidChange = service.getStatusValidChange(formGroup);

        expect(statusValidChange).toMatchObject(sampleWithNewData);
      });

      it('should return NewStatusValidChange for empty StatusValidChange initial value', () => {
        const formGroup = service.createStatusValidChangeFormGroup();

        const statusValidChange = service.getStatusValidChange(formGroup);

        expect(statusValidChange).toMatchObject({});
      });

      it('should return IStatusValidChange', () => {
        const formGroup = service.createStatusValidChangeFormGroup(sampleWithRequiredData);

        const statusValidChange = service.getStatusValidChange(formGroup);

        expect(statusValidChange).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStatusValidChange should not enable id FormControl', () => {
        const formGroup = service.createStatusValidChangeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStatusValidChange should disable id FormControl', () => {
        const formGroup = service.createStatusValidChangeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
