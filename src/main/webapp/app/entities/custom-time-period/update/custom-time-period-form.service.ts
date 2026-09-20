import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomTimePeriod, NewCustomTimePeriod } from '../custom-time-period.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomTimePeriod for edit and NewCustomTimePeriodFormGroupInput for create.
 */
type CustomTimePeriodFormGroupInput = ICustomTimePeriod | PartialWithRequiredKeyOf<NewCustomTimePeriod>;

type CustomTimePeriodFormDefaults = Pick<NewCustomTimePeriod, 'id'>;

type CustomTimePeriodFormGroupContent = {
  id: FormControl<ICustomTimePeriod['id'] | NewCustomTimePeriod['id']>;
  name: FormControl<ICustomTimePeriod['name']>;
  fromDate: FormControl<ICustomTimePeriod['fromDate']>;
  thruDate: FormControl<ICustomTimePeriod['thruDate']>;
};

export type CustomTimePeriodFormGroup = FormGroup<CustomTimePeriodFormGroupContent>;

@Service()
export class CustomTimePeriodFormService {
  createCustomTimePeriodFormGroup(customTimePeriod?: CustomTimePeriodFormGroupInput): CustomTimePeriodFormGroup {
    const customTimePeriodRawValue = {
      ...this.getFormDefaults(),
      ...(customTimePeriod ?? { id: null }),
    };

    return new FormGroup<CustomTimePeriodFormGroupContent>({
      id: new FormControl(
        { value: customTimePeriodRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(customTimePeriodRawValue.name, {
        validators: [Validators.maxLength(25)],
      }),
      fromDate: new FormControl(customTimePeriodRawValue.fromDate),
      thruDate: new FormControl(customTimePeriodRawValue.thruDate),
    });
  }

  getCustomTimePeriod(form: CustomTimePeriodFormGroup): ICustomTimePeriod | NewCustomTimePeriod {
    return form.getRawValue();
  }

  resetForm(form: CustomTimePeriodFormGroup, customTimePeriod: CustomTimePeriodFormGroupInput): void {
    const customTimePeriodRawValue = { ...this.getFormDefaults(), ...customTimePeriod };
    form.reset({
      ...customTimePeriodRawValue,
      id: { value: customTimePeriodRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustomTimePeriodFormDefaults {
    return {
      id: null,
    };
  }
}
