import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFacilityGroup, NewFacilityGroup } from '../facility-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityGroup for edit and NewFacilityGroupFormGroupInput for create.
 */
type FacilityGroupFormGroupInput = IFacilityGroup | PartialWithRequiredKeyOf<NewFacilityGroup>;

type FacilityGroupFormDefaults = Pick<NewFacilityGroup, 'id'>;

type FacilityGroupFormGroupContent = {
  id: FormControl<IFacilityGroup['id'] | NewFacilityGroup['id']>;
  name: FormControl<IFacilityGroup['name']>;
  description: FormControl<IFacilityGroup['description']>;
  facilityGroupType: FormControl<IFacilityGroup['facilityGroupType']>;
  parentFacilityGroup: FormControl<IFacilityGroup['parentFacilityGroup']>;
};

export type FacilityGroupFormGroup = FormGroup<FacilityGroupFormGroupContent>;

@Service()
export class FacilityGroupFormService {
  createFacilityGroupFormGroup(facilityGroup?: FacilityGroupFormGroupInput): FacilityGroupFormGroup {
    const facilityGroupRawValue = {
      ...this.getFormDefaults(),
      ...(facilityGroup ?? { id: null }),
    };

    return new FormGroup<FacilityGroupFormGroupContent>({
      id: new FormControl(
        { value: facilityGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(facilityGroupRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(facilityGroupRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      facilityGroupType: new FormControl(facilityGroupRawValue.facilityGroupType),
      parentFacilityGroup: new FormControl(facilityGroupRawValue.parentFacilityGroup),
    });
  }

  getFacilityGroup(form: FacilityGroupFormGroup): IFacilityGroup | NewFacilityGroup {
    return form.getRawValue();
  }

  resetForm(form: FacilityGroupFormGroup, facilityGroup: FacilityGroupFormGroupInput): void {
    const facilityGroupRawValue = { ...this.getFormDefaults(), ...facilityGroup };
    form.reset({
      ...facilityGroupRawValue,
      id: { value: facilityGroupRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityGroupFormDefaults {
    return {
      id: null,
    };
  }
}
