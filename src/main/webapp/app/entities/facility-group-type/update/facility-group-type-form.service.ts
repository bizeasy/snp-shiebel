import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFacilityGroupType, NewFacilityGroupType } from '../facility-group-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityGroupType for edit and NewFacilityGroupTypeFormGroupInput for create.
 */
type FacilityGroupTypeFormGroupInput = IFacilityGroupType | PartialWithRequiredKeyOf<NewFacilityGroupType>;

type FacilityGroupTypeFormDefaults = Pick<NewFacilityGroupType, 'id'>;

type FacilityGroupTypeFormGroupContent = {
  id: FormControl<IFacilityGroupType['id'] | NewFacilityGroupType['id']>;
  name: FormControl<IFacilityGroupType['name']>;
  description: FormControl<IFacilityGroupType['description']>;
};

export type FacilityGroupTypeFormGroup = FormGroup<FacilityGroupTypeFormGroupContent>;

@Service()
export class FacilityGroupTypeFormService {
  createFacilityGroupTypeFormGroup(facilityGroupType?: FacilityGroupTypeFormGroupInput): FacilityGroupTypeFormGroup {
    const facilityGroupTypeRawValue = {
      ...this.getFormDefaults(),
      ...(facilityGroupType ?? { id: null }),
    };

    return new FormGroup<FacilityGroupTypeFormGroupContent>({
      id: new FormControl(
        { value: facilityGroupTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(facilityGroupTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(facilityGroupTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getFacilityGroupType(form: FacilityGroupTypeFormGroup): IFacilityGroupType | NewFacilityGroupType {
    return form.getRawValue();
  }

  resetForm(form: FacilityGroupTypeFormGroup, facilityGroupType: FacilityGroupTypeFormGroupInput): void {
    const facilityGroupTypeRawValue = { ...this.getFormDefaults(), ...facilityGroupType };
    form.reset({
      ...facilityGroupTypeRawValue,
      id: { value: facilityGroupTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityGroupTypeFormDefaults {
    return {
      id: null,
    };
  }
}
