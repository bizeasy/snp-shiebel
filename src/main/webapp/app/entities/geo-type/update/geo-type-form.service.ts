import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGeoType, NewGeoType } from '../geo-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGeoType for edit and NewGeoTypeFormGroupInput for create.
 */
type GeoTypeFormGroupInput = IGeoType | PartialWithRequiredKeyOf<NewGeoType>;

type GeoTypeFormDefaults = Pick<NewGeoType, 'id'>;

type GeoTypeFormGroupContent = {
  id: FormControl<IGeoType['id'] | NewGeoType['id']>;
  name: FormControl<IGeoType['name']>;
};

export type GeoTypeFormGroup = FormGroup<GeoTypeFormGroupContent>;

@Service()
export class GeoTypeFormService {
  createGeoTypeFormGroup(geoType?: GeoTypeFormGroupInput): GeoTypeFormGroup {
    const geoTypeRawValue = {
      ...this.getFormDefaults(),
      ...(geoType ?? { id: null }),
    };

    return new FormGroup<GeoTypeFormGroupContent>({
      id: new FormControl(
        { value: geoTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(geoTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
    });
  }

  getGeoType(form: GeoTypeFormGroup): IGeoType | NewGeoType {
    return form.getRawValue();
  }

  resetForm(form: GeoTypeFormGroup, geoType: GeoTypeFormGroupInput): void {
    const geoTypeRawValue = { ...this.getFormDefaults(), ...geoType };
    form.reset({
      ...geoTypeRawValue,
      id: { value: geoTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): GeoTypeFormDefaults {
    return {
      id: null,
    };
  }
}
