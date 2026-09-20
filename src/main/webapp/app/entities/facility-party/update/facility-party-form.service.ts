import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IFacilityParty, NewFacilityParty } from '../facility-party.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityParty for edit and NewFacilityPartyFormGroupInput for create.
 */
type FacilityPartyFormGroupInput = IFacilityParty | PartialWithRequiredKeyOf<NewFacilityParty>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFacilityParty | NewFacilityParty> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type FacilityPartyFormRawValue = FormValueOf<IFacilityParty>;

type NewFacilityPartyFormRawValue = FormValueOf<NewFacilityParty>;

type FacilityPartyFormDefaults = Pick<NewFacilityParty, 'id' | 'fromDate' | 'thruDate'>;

type FacilityPartyFormGroupContent = {
  id: FormControl<FacilityPartyFormRawValue['id'] | NewFacilityParty['id']>;
  fromDate: FormControl<FacilityPartyFormRawValue['fromDate']>;
  thruDate: FormControl<FacilityPartyFormRawValue['thruDate']>;
  facility: FormControl<FacilityPartyFormRawValue['facility']>;
  party: FormControl<FacilityPartyFormRawValue['party']>;
  roleType: FormControl<FacilityPartyFormRawValue['roleType']>;
};

export type FacilityPartyFormGroup = FormGroup<FacilityPartyFormGroupContent>;

@Service()
export class FacilityPartyFormService {
  createFacilityPartyFormGroup(facilityParty?: FacilityPartyFormGroupInput): FacilityPartyFormGroup {
    const facilityPartyRawValue = this.convertFacilityPartyToFacilityPartyRawValue({
      ...this.getFormDefaults(),
      ...(facilityParty ?? { id: null }),
    });

    return new FormGroup<FacilityPartyFormGroupContent>({
      id: new FormControl(
        { value: facilityPartyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(facilityPartyRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(facilityPartyRawValue.thruDate),
      facility: new FormControl(facilityPartyRawValue.facility, {
        validators: [Validators.required],
      }),
      party: new FormControl(facilityPartyRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(facilityPartyRawValue.roleType),
    });
  }

  getFacilityParty(form: FacilityPartyFormGroup): IFacilityParty | NewFacilityParty {
    return this.convertFacilityPartyRawValueToFacilityParty(form.getRawValue());
  }

  resetForm(form: FacilityPartyFormGroup, facilityParty: FacilityPartyFormGroupInput): void {
    const facilityPartyRawValue = this.convertFacilityPartyToFacilityPartyRawValue({ ...this.getFormDefaults(), ...facilityParty });
    form.reset({
      ...facilityPartyRawValue,
      id: { value: facilityPartyRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityPartyFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertFacilityPartyRawValueToFacilityParty(
    rawFacilityParty: FacilityPartyFormRawValue | NewFacilityPartyFormRawValue,
  ): IFacilityParty | NewFacilityParty {
    return {
      ...rawFacilityParty,
      fromDate: dayjs(rawFacilityParty.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawFacilityParty.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertFacilityPartyToFacilityPartyRawValue(
    facilityParty: IFacilityParty | (Partial<NewFacilityParty> & FacilityPartyFormDefaults),
  ): FacilityPartyFormRawValue | PartialWithRequiredKeyOf<NewFacilityPartyFormRawValue> {
    return {
      ...facilityParty,
      fromDate: facilityParty.fromDate ? facilityParty.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: facilityParty.thruDate ? facilityParty.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
