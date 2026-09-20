import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGeoAssocType, NewGeoAssocType } from '../geo-assoc-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGeoAssocType for edit and NewGeoAssocTypeFormGroupInput for create.
 */
type GeoAssocTypeFormGroupInput = IGeoAssocType | PartialWithRequiredKeyOf<NewGeoAssocType>;

type GeoAssocTypeFormDefaults = Pick<NewGeoAssocType, 'id'>;

type GeoAssocTypeFormGroupContent = {
  id: FormControl<IGeoAssocType['id'] | NewGeoAssocType['id']>;
  name: FormControl<IGeoAssocType['name']>;
};

export type GeoAssocTypeFormGroup = FormGroup<GeoAssocTypeFormGroupContent>;

@Service()
export class GeoAssocTypeFormService {
  createGeoAssocTypeFormGroup(geoAssocType?: GeoAssocTypeFormGroupInput): GeoAssocTypeFormGroup {
    const geoAssocTypeRawValue = {
      ...this.getFormDefaults(),
      ...(geoAssocType ?? { id: null }),
    };

    return new FormGroup<GeoAssocTypeFormGroupContent>({
      id: new FormControl(
        { value: geoAssocTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(geoAssocTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
    });
  }

  getGeoAssocType(form: GeoAssocTypeFormGroup): IGeoAssocType | NewGeoAssocType {
    return form.getRawValue();
  }

  resetForm(form: GeoAssocTypeFormGroup, geoAssocType: GeoAssocTypeFormGroupInput): void {
    const geoAssocTypeRawValue = { ...this.getFormDefaults(), ...geoAssocType };
    form.reset({
      ...geoAssocTypeRawValue,
      id: { value: geoAssocTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): GeoAssocTypeFormDefaults {
    return {
      id: null,
    };
  }
}
