import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../otp-auth.test-samples';

import { OtpAuthFormService } from './otp-auth-form.service';

describe('OtpAuth Form Service', () => {
  let service: OtpAuthFormService;

  beforeEach(() => {
    service = TestBed.inject(OtpAuthFormService);
  });

  describe('Service methods', () => {
    describe('createOtpAuthFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOtpAuthFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            otp: expect.any(Object),
            otpSent: expect.any(Object),
            verificationSuccess: expect.any(Object),
            otpExpired: expect.any(Object),
            otpSentTime: expect.any(Object),
            sentCounter: expect.any(Object),
            failCounter: expect.any(Object),
            otpResetCounter: expect.any(Object),
            maxResend: expect.any(Object),
            maxReset: expect.any(Object),
            maxFailures: expect.any(Object),
            otpActiveTime: expect.any(Object),
            party: expect.any(Object),
          }),
        );
      });

      it('passing IOtpAuth should create a new form with FormGroup', () => {
        const formGroup = service.createOtpAuthFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            otp: expect.any(Object),
            otpSent: expect.any(Object),
            verificationSuccess: expect.any(Object),
            otpExpired: expect.any(Object),
            otpSentTime: expect.any(Object),
            sentCounter: expect.any(Object),
            failCounter: expect.any(Object),
            otpResetCounter: expect.any(Object),
            maxResend: expect.any(Object),
            maxReset: expect.any(Object),
            maxFailures: expect.any(Object),
            otpActiveTime: expect.any(Object),
            party: expect.any(Object),
          }),
        );
      });
    });

    describe('getOtpAuth', () => {
      it('should return NewOtpAuth for default OtpAuth initial value', () => {
        const formGroup = service.createOtpAuthFormGroup(sampleWithNewData);

        const otpAuth = service.getOtpAuth(formGroup);

        expect(otpAuth).toMatchObject(sampleWithNewData);
      });

      it('should return NewOtpAuth for empty OtpAuth initial value', () => {
        const formGroup = service.createOtpAuthFormGroup();

        const otpAuth = service.getOtpAuth(formGroup);

        expect(otpAuth).toMatchObject({});
      });

      it('should return IOtpAuth', () => {
        const formGroup = service.createOtpAuthFormGroup(sampleWithRequiredData);

        const otpAuth = service.getOtpAuth(formGroup);

        expect(otpAuth).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOtpAuth should not enable id FormControl', () => {
        const formGroup = service.createOtpAuthFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOtpAuth should disable id FormControl', () => {
        const formGroup = service.createOtpAuthFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
