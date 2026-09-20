import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IFacilityGroupMember, NewFacilityGroupMember } from '../facility-group-member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityGroupMember for edit and NewFacilityGroupMemberFormGroupInput for create.
 */
type FacilityGroupMemberFormGroupInput = IFacilityGroupMember | PartialWithRequiredKeyOf<NewFacilityGroupMember>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFacilityGroupMember | NewFacilityGroupMember> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type FacilityGroupMemberFormRawValue = FormValueOf<IFacilityGroupMember>;

type NewFacilityGroupMemberFormRawValue = FormValueOf<NewFacilityGroupMember>;

type FacilityGroupMemberFormDefaults = Pick<NewFacilityGroupMember, 'id' | 'fromDate' | 'thruDate'>;

type FacilityGroupMemberFormGroupContent = {
  id: FormControl<FacilityGroupMemberFormRawValue['id'] | NewFacilityGroupMember['id']>;
  fromDate: FormControl<FacilityGroupMemberFormRawValue['fromDate']>;
  thruDate: FormControl<FacilityGroupMemberFormRawValue['thruDate']>;
  sequenceNo: FormControl<FacilityGroupMemberFormRawValue['sequenceNo']>;
  facility: FormControl<FacilityGroupMemberFormRawValue['facility']>;
  facilityGroup: FormControl<FacilityGroupMemberFormRawValue['facilityGroup']>;
};

export type FacilityGroupMemberFormGroup = FormGroup<FacilityGroupMemberFormGroupContent>;

@Service()
export class FacilityGroupMemberFormService {
  createFacilityGroupMemberFormGroup(facilityGroupMember?: FacilityGroupMemberFormGroupInput): FacilityGroupMemberFormGroup {
    const facilityGroupMemberRawValue = this.convertFacilityGroupMemberToFacilityGroupMemberRawValue({
      ...this.getFormDefaults(),
      ...(facilityGroupMember ?? { id: null }),
    });

    return new FormGroup<FacilityGroupMemberFormGroupContent>({
      id: new FormControl(
        { value: facilityGroupMemberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(facilityGroupMemberRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(facilityGroupMemberRawValue.thruDate),
      sequenceNo: new FormControl(facilityGroupMemberRawValue.sequenceNo),
      facility: new FormControl(facilityGroupMemberRawValue.facility, {
        validators: [Validators.required],
      }),
      facilityGroup: new FormControl(facilityGroupMemberRawValue.facilityGroup, {
        validators: [Validators.required],
      }),
    });
  }

  getFacilityGroupMember(form: FacilityGroupMemberFormGroup): IFacilityGroupMember | NewFacilityGroupMember {
    return this.convertFacilityGroupMemberRawValueToFacilityGroupMember(form.getRawValue());
  }

  resetForm(form: FacilityGroupMemberFormGroup, facilityGroupMember: FacilityGroupMemberFormGroupInput): void {
    const facilityGroupMemberRawValue = this.convertFacilityGroupMemberToFacilityGroupMemberRawValue({
      ...this.getFormDefaults(),
      ...facilityGroupMember,
    });
    form.reset({
      ...facilityGroupMemberRawValue,
      id: { value: facilityGroupMemberRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityGroupMemberFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertFacilityGroupMemberRawValueToFacilityGroupMember(
    rawFacilityGroupMember: FacilityGroupMemberFormRawValue | NewFacilityGroupMemberFormRawValue,
  ): IFacilityGroupMember | NewFacilityGroupMember {
    return {
      ...rawFacilityGroupMember,
      fromDate: dayjs(rawFacilityGroupMember.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawFacilityGroupMember.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertFacilityGroupMemberToFacilityGroupMemberRawValue(
    facilityGroupMember: IFacilityGroupMember | (Partial<NewFacilityGroupMember> & FacilityGroupMemberFormDefaults),
  ): FacilityGroupMemberFormRawValue | PartialWithRequiredKeyOf<NewFacilityGroupMemberFormRawValue> {
    return {
      ...facilityGroupMember,
      fromDate: facilityGroupMember.fromDate ? facilityGroupMember.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: facilityGroupMember.thruDate ? facilityGroupMember.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
