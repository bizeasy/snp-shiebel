import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOtpAuth, NewOtpAuth } from '../otp-auth.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOtpAuth for edit and NewOtpAuthFormGroupInput for create.
 */
type OtpAuthFormGroupInput = IOtpAuth | PartialWithRequiredKeyOf<NewOtpAuth>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOtpAuth | NewOtpAuth> = Omit<T, 'otpSentTime'> & {
  otpSentTime?: string | null;
};

type OtpAuthFormRawValue = FormValueOf<IOtpAuth>;

type NewOtpAuthFormRawValue = FormValueOf<NewOtpAuth>;

type OtpAuthFormDefaults = Pick<NewOtpAuth, 'id' | 'otpSent' | 'verificationSuccess' | 'otpExpired' | 'otpSentTime'>;

type OtpAuthFormGroupContent = {
  id: FormControl<OtpAuthFormRawValue['id'] | NewOtpAuth['id']>;
  otp: FormControl<OtpAuthFormRawValue['otp']>;
  otpSent: FormControl<OtpAuthFormRawValue['otpSent']>;
  verificationSuccess: FormControl<OtpAuthFormRawValue['verificationSuccess']>;
  otpExpired: FormControl<OtpAuthFormRawValue['otpExpired']>;
  otpSentTime: FormControl<OtpAuthFormRawValue['otpSentTime']>;
  sentCounter: FormControl<OtpAuthFormRawValue['sentCounter']>;
  failCounter: FormControl<OtpAuthFormRawValue['failCounter']>;
  otpResetCounter: FormControl<OtpAuthFormRawValue['otpResetCounter']>;
  maxResend: FormControl<OtpAuthFormRawValue['maxResend']>;
  maxReset: FormControl<OtpAuthFormRawValue['maxReset']>;
  maxFailures: FormControl<OtpAuthFormRawValue['maxFailures']>;
  otpActiveTime: FormControl<OtpAuthFormRawValue['otpActiveTime']>;
  party: FormControl<OtpAuthFormRawValue['party']>;
};

export type OtpAuthFormGroup = FormGroup<OtpAuthFormGroupContent>;

@Service()
export class OtpAuthFormService {
  createOtpAuthFormGroup(otpAuth?: OtpAuthFormGroupInput): OtpAuthFormGroup {
    const otpAuthRawValue = this.convertOtpAuthToOtpAuthRawValue({
      ...this.getFormDefaults(),
      ...(otpAuth ?? { id: null }),
    });

    return new FormGroup<OtpAuthFormGroupContent>({
      id: new FormControl(
        { value: otpAuthRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      otp: new FormControl(otpAuthRawValue.otp, {
        validators: [Validators.maxLength(10)],
      }),
      otpSent: new FormControl(otpAuthRawValue.otpSent),
      verificationSuccess: new FormControl(otpAuthRawValue.verificationSuccess),
      otpExpired: new FormControl(otpAuthRawValue.otpExpired),
      otpSentTime: new FormControl(otpAuthRawValue.otpSentTime),
      sentCounter: new FormControl(otpAuthRawValue.sentCounter),
      failCounter: new FormControl(otpAuthRawValue.failCounter),
      otpResetCounter: new FormControl(otpAuthRawValue.otpResetCounter),
      maxResend: new FormControl(otpAuthRawValue.maxResend),
      maxReset: new FormControl(otpAuthRawValue.maxReset),
      maxFailures: new FormControl(otpAuthRawValue.maxFailures),
      otpActiveTime: new FormControl(otpAuthRawValue.otpActiveTime),
      party: new FormControl(otpAuthRawValue.party),
    });
  }

  getOtpAuth(form: OtpAuthFormGroup): IOtpAuth | NewOtpAuth {
    return this.convertOtpAuthRawValueToOtpAuth(form.getRawValue());
  }

  resetForm(form: OtpAuthFormGroup, otpAuth: OtpAuthFormGroupInput): void {
    const otpAuthRawValue = this.convertOtpAuthToOtpAuthRawValue({ ...this.getFormDefaults(), ...otpAuth });
    form.reset({
      ...otpAuthRawValue,
      id: { value: otpAuthRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OtpAuthFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      otpSent: false,
      verificationSuccess: false,
      otpExpired: false,
      otpSentTime: currentTime,
    };
  }

  private convertOtpAuthRawValueToOtpAuth(rawOtpAuth: OtpAuthFormRawValue | NewOtpAuthFormRawValue): IOtpAuth | NewOtpAuth {
    return {
      ...rawOtpAuth,
      otpSentTime: dayjs(rawOtpAuth.otpSentTime, DATE_TIME_FORMAT),
    };
  }

  private convertOtpAuthToOtpAuthRawValue(
    otpAuth: IOtpAuth | (Partial<NewOtpAuth> & OtpAuthFormDefaults),
  ): OtpAuthFormRawValue | PartialWithRequiredKeyOf<NewOtpAuthFormRawValue> {
    return {
      ...otpAuth,
      otpSentTime: otpAuth.otpSentTime ? otpAuth.otpSentTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
