import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPicklistRole, NewPicklistRole } from '../picklist-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPicklistRole for edit and NewPicklistRoleFormGroupInput for create.
 */
type PicklistRoleFormGroupInput = IPicklistRole | PartialWithRequiredKeyOf<NewPicklistRole>;

type PicklistRoleFormDefaults = Pick<NewPicklistRole, 'id'>;

type PicklistRoleFormGroupContent = {
  id: FormControl<IPicklistRole['id'] | NewPicklistRole['id']>;
  pickList: FormControl<IPicklistRole['pickList']>;
  party: FormControl<IPicklistRole['party']>;
  roleType: FormControl<IPicklistRole['roleType']>;
};

export type PicklistRoleFormGroup = FormGroup<PicklistRoleFormGroupContent>;

@Service()
export class PicklistRoleFormService {
  createPicklistRoleFormGroup(picklistRole?: PicklistRoleFormGroupInput): PicklistRoleFormGroup {
    const picklistRoleRawValue = {
      ...this.getFormDefaults(),
      ...(picklistRole ?? { id: null }),
    };

    return new FormGroup<PicklistRoleFormGroupContent>({
      id: new FormControl(
        { value: picklistRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pickList: new FormControl(picklistRoleRawValue.pickList, {
        validators: [Validators.required],
      }),
      party: new FormControl(picklistRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(picklistRoleRawValue.roleType),
    });
  }

  getPicklistRole(form: PicklistRoleFormGroup): IPicklistRole | NewPicklistRole {
    return form.getRawValue();
  }

  resetForm(form: PicklistRoleFormGroup, picklistRole: PicklistRoleFormGroupInput): void {
    const picklistRoleRawValue = { ...this.getFormDefaults(), ...picklistRole };
    form.reset({
      ...picklistRoleRawValue,
      id: { value: picklistRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PicklistRoleFormDefaults {
    return {
      id: null,
    };
  }
}
