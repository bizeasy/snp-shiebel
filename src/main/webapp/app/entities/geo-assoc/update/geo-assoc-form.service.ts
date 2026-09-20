import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGeoAssoc, NewGeoAssoc } from '../geo-assoc.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGeoAssoc for edit and NewGeoAssocFormGroupInput for create.
 */
type GeoAssocFormGroupInput = IGeoAssoc | PartialWithRequiredKeyOf<NewGeoAssoc>;

type GeoAssocFormDefaults = Pick<NewGeoAssoc, 'id'>;

type GeoAssocFormGroupContent = {
  id: FormControl<IGeoAssoc['id'] | NewGeoAssoc['id']>;
  geo: FormControl<IGeoAssoc['geo']>;
  geoTo: FormControl<IGeoAssoc['geoTo']>;
  geoAssocType: FormControl<IGeoAssoc['geoAssocType']>;
};

export type GeoAssocFormGroup = FormGroup<GeoAssocFormGroupContent>;

@Service()
export class GeoAssocFormService {
  createGeoAssocFormGroup(geoAssoc?: GeoAssocFormGroupInput): GeoAssocFormGroup {
    const geoAssocRawValue = {
      ...this.getFormDefaults(),
      ...(geoAssoc ?? { id: null }),
    };

    return new FormGroup<GeoAssocFormGroupContent>({
      id: new FormControl(
        { value: geoAssocRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      geo: new FormControl(geoAssocRawValue.geo, {
        validators: [Validators.required],
      }),
      geoTo: new FormControl(geoAssocRawValue.geoTo, {
        validators: [Validators.required],
      }),
      geoAssocType: new FormControl(geoAssocRawValue.geoAssocType),
    });
  }

  getGeoAssoc(form: GeoAssocFormGroup): IGeoAssoc | NewGeoAssoc {
    return form.getRawValue();
  }

  resetForm(form: GeoAssocFormGroup, geoAssoc: GeoAssocFormGroupInput): void {
    const geoAssocRawValue = { ...this.getFormDefaults(), ...geoAssoc };
    form.reset({
      ...geoAssocRawValue,
      id: { value: geoAssocRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): GeoAssocFormDefaults {
    return {
      id: null,
    };
  }
}
