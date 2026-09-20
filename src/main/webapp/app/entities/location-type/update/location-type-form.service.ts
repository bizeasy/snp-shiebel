import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILocationType, NewLocationType } from '../location-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocationType for edit and NewLocationTypeFormGroupInput for create.
 */
type LocationTypeFormGroupInput = ILocationType | PartialWithRequiredKeyOf<NewLocationType>;

type LocationTypeFormDefaults = Pick<NewLocationType, 'id'>;

type LocationTypeFormGroupContent = {
  id: FormControl<ILocationType['id'] | NewLocationType['id']>;
  name: FormControl<ILocationType['name']>;
  description: FormControl<ILocationType['description']>;
};

export type LocationTypeFormGroup = FormGroup<LocationTypeFormGroupContent>;

@Service()
export class LocationTypeFormService {
  createLocationTypeFormGroup(locationType?: LocationTypeFormGroupInput): LocationTypeFormGroup {
    const locationTypeRawValue = {
      ...this.getFormDefaults(),
      ...(locationType ?? { id: null }),
    };

    return new FormGroup<LocationTypeFormGroupContent>({
      id: new FormControl(
        { value: locationTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(locationTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(locationTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getLocationType(form: LocationTypeFormGroup): ILocationType | NewLocationType {
    return form.getRawValue();
  }

  resetForm(form: LocationTypeFormGroup, locationType: LocationTypeFormGroupInput): void {
    const locationTypeRawValue = { ...this.getFormDefaults(), ...locationType };
    form.reset({
      ...locationTypeRawValue,
      id: { value: locationTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): LocationTypeFormDefaults {
    return {
      id: null,
    };
  }
}
