import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFacilityType, NewFacilityType } from '../facility-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityType for edit and NewFacilityTypeFormGroupInput for create.
 */
type FacilityTypeFormGroupInput = IFacilityType | PartialWithRequiredKeyOf<NewFacilityType>;

type FacilityTypeFormDefaults = Pick<NewFacilityType, 'id'>;

type FacilityTypeFormGroupContent = {
  id: FormControl<IFacilityType['id'] | NewFacilityType['id']>;
  name: FormControl<IFacilityType['name']>;
  description: FormControl<IFacilityType['description']>;
};

export type FacilityTypeFormGroup = FormGroup<FacilityTypeFormGroupContent>;

@Service()
export class FacilityTypeFormService {
  createFacilityTypeFormGroup(facilityType?: FacilityTypeFormGroupInput): FacilityTypeFormGroup {
    const facilityTypeRawValue = {
      ...this.getFormDefaults(),
      ...(facilityType ?? { id: null }),
    };

    return new FormGroup<FacilityTypeFormGroupContent>({
      id: new FormControl(
        { value: facilityTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(facilityTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(facilityTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getFacilityType(form: FacilityTypeFormGroup): IFacilityType | NewFacilityType {
    return form.getRawValue();
  }

  resetForm(form: FacilityTypeFormGroup, facilityType: FacilityTypeFormGroupInput): void {
    const facilityTypeRawValue = { ...this.getFormDefaults(), ...facilityType };
    form.reset({
      ...facilityTypeRawValue,
      id: { value: facilityTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityTypeFormDefaults {
    return {
      id: null,
    };
  }
}
