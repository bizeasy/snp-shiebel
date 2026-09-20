import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGeoPoint, NewGeoPoint } from '../geo-point.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGeoPoint for edit and NewGeoPointFormGroupInput for create.
 */
type GeoPointFormGroupInput = IGeoPoint | PartialWithRequiredKeyOf<NewGeoPoint>;

type GeoPointFormDefaults = Pick<NewGeoPoint, 'id'>;

type GeoPointFormGroupContent = {
  id: FormControl<IGeoPoint['id'] | NewGeoPoint['id']>;
  latitude: FormControl<IGeoPoint['latitude']>;
  longitude: FormControl<IGeoPoint['longitude']>;
};

export type GeoPointFormGroup = FormGroup<GeoPointFormGroupContent>;

@Service()
export class GeoPointFormService {
  createGeoPointFormGroup(geoPoint?: GeoPointFormGroupInput): GeoPointFormGroup {
    const geoPointRawValue = {
      ...this.getFormDefaults(),
      ...(geoPoint ?? { id: null }),
    };

    return new FormGroup<GeoPointFormGroupContent>({
      id: new FormControl(
        { value: geoPointRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      latitude: new FormControl(geoPointRawValue.latitude),
      longitude: new FormControl(geoPointRawValue.longitude),
    });
  }

  getGeoPoint(form: GeoPointFormGroup): IGeoPoint | NewGeoPoint {
    return form.getRawValue();
  }

  resetForm(form: GeoPointFormGroup, geoPoint: GeoPointFormGroupInput): void {
    const geoPointRawValue = { ...this.getFormDefaults(), ...geoPoint };
    form.reset({
      ...geoPointRawValue,
      id: { value: geoPointRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): GeoPointFormDefaults {
    return {
      id: null,
    };
  }
}
