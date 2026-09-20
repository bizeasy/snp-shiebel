import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGeo, NewGeo } from '../geo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGeo for edit and NewGeoFormGroupInput for create.
 */
type GeoFormGroupInput = IGeo | PartialWithRequiredKeyOf<NewGeo>;

type GeoFormDefaults = Pick<NewGeo, 'id'>;

type GeoFormGroupContent = {
  id: FormControl<IGeo['id'] | NewGeo['id']>;
  name: FormControl<IGeo['name']>;
  code: FormControl<IGeo['code']>;
  abbreviation: FormControl<IGeo['abbreviation']>;
  geoType: FormControl<IGeo['geoType']>;
};

export type GeoFormGroup = FormGroup<GeoFormGroupContent>;

@Service()
export class GeoFormService {
  createGeoFormGroup(geo?: GeoFormGroupInput): GeoFormGroup {
    const geoRawValue = {
      ...this.getFormDefaults(),
      ...(geo ?? { id: null }),
    };

    return new FormGroup<GeoFormGroupContent>({
      id: new FormControl(
        { value: geoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(geoRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      code: new FormControl(geoRawValue.code, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      abbreviation: new FormControl(geoRawValue.abbreviation, {
        validators: [Validators.maxLength(10)],
      }),
      geoType: new FormControl(geoRawValue.geoType),
    });
  }

  getGeo(form: GeoFormGroup): IGeo | NewGeo {
    return form.getRawValue();
  }

  resetForm(form: GeoFormGroup, geo: GeoFormGroupInput): void {
    const geoRawValue = { ...this.getFormDefaults(), ...geo };
    form.reset({
      ...geoRawValue,
      id: { value: geoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): GeoFormDefaults {
    return {
      id: null,
    };
  }
}
