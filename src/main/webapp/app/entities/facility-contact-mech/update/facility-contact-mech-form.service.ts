import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IFacilityContactMech, NewFacilityContactMech } from '../facility-contact-mech.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityContactMech for edit and NewFacilityContactMechFormGroupInput for create.
 */
type FacilityContactMechFormGroupInput = IFacilityContactMech | PartialWithRequiredKeyOf<NewFacilityContactMech>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFacilityContactMech | NewFacilityContactMech> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type FacilityContactMechFormRawValue = FormValueOf<IFacilityContactMech>;

type NewFacilityContactMechFormRawValue = FormValueOf<NewFacilityContactMech>;

type FacilityContactMechFormDefaults = Pick<NewFacilityContactMech, 'id' | 'fromDate' | 'thruDate'>;

type FacilityContactMechFormGroupContent = {
  id: FormControl<FacilityContactMechFormRawValue['id'] | NewFacilityContactMech['id']>;
  fromDate: FormControl<FacilityContactMechFormRawValue['fromDate']>;
  thruDate: FormControl<FacilityContactMechFormRawValue['thruDate']>;
  facility: FormControl<FacilityContactMechFormRawValue['facility']>;
  contactMech: FormControl<FacilityContactMechFormRawValue['contactMech']>;
  contactMechPurpose: FormControl<FacilityContactMechFormRawValue['contactMechPurpose']>;
};

export type FacilityContactMechFormGroup = FormGroup<FacilityContactMechFormGroupContent>;

@Service()
export class FacilityContactMechFormService {
  createFacilityContactMechFormGroup(facilityContactMech?: FacilityContactMechFormGroupInput): FacilityContactMechFormGroup {
    const facilityContactMechRawValue = this.convertFacilityContactMechToFacilityContactMechRawValue({
      ...this.getFormDefaults(),
      ...(facilityContactMech ?? { id: null }),
    });

    return new FormGroup<FacilityContactMechFormGroupContent>({
      id: new FormControl(
        { value: facilityContactMechRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(facilityContactMechRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(facilityContactMechRawValue.thruDate),
      facility: new FormControl(facilityContactMechRawValue.facility, {
        validators: [Validators.required],
      }),
      contactMech: new FormControl(facilityContactMechRawValue.contactMech, {
        validators: [Validators.required],
      }),
      contactMechPurpose: new FormControl(facilityContactMechRawValue.contactMechPurpose),
    });
  }

  getFacilityContactMech(form: FacilityContactMechFormGroup): IFacilityContactMech | NewFacilityContactMech {
    return this.convertFacilityContactMechRawValueToFacilityContactMech(form.getRawValue());
  }

  resetForm(form: FacilityContactMechFormGroup, facilityContactMech: FacilityContactMechFormGroupInput): void {
    const facilityContactMechRawValue = this.convertFacilityContactMechToFacilityContactMechRawValue({
      ...this.getFormDefaults(),
      ...facilityContactMech,
    });
    form.reset({
      ...facilityContactMechRawValue,
      id: { value: facilityContactMechRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityContactMechFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertFacilityContactMechRawValueToFacilityContactMech(
    rawFacilityContactMech: FacilityContactMechFormRawValue | NewFacilityContactMechFormRawValue,
  ): IFacilityContactMech | NewFacilityContactMech {
    return {
      ...rawFacilityContactMech,
      fromDate: dayjs(rawFacilityContactMech.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawFacilityContactMech.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertFacilityContactMechToFacilityContactMechRawValue(
    facilityContactMech: IFacilityContactMech | (Partial<NewFacilityContactMech> & FacilityContactMechFormDefaults),
  ): FacilityContactMechFormRawValue | PartialWithRequiredKeyOf<NewFacilityContactMechFormRawValue> {
    return {
      ...facilityContactMech,
      fromDate: facilityContactMech.fromDate ? facilityContactMech.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: facilityContactMech.thruDate ? facilityContactMech.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
