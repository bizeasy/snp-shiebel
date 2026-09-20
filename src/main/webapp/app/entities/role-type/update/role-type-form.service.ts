import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRoleType, NewRoleType } from '../role-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoleType for edit and NewRoleTypeFormGroupInput for create.
 */
type RoleTypeFormGroupInput = IRoleType | PartialWithRequiredKeyOf<NewRoleType>;

type RoleTypeFormDefaults = Pick<NewRoleType, 'id'>;

type RoleTypeFormGroupContent = {
  id: FormControl<IRoleType['id'] | NewRoleType['id']>;
  name: FormControl<IRoleType['name']>;
  description: FormControl<IRoleType['description']>;
};

export type RoleTypeFormGroup = FormGroup<RoleTypeFormGroupContent>;

@Service()
export class RoleTypeFormService {
  createRoleTypeFormGroup(roleType?: RoleTypeFormGroupInput): RoleTypeFormGroup {
    const roleTypeRawValue = {
      ...this.getFormDefaults(),
      ...(roleType ?? { id: null }),
    };

    return new FormGroup<RoleTypeFormGroupContent>({
      id: new FormControl(
        { value: roleTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(roleTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(roleTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
    });
  }

  getRoleType(form: RoleTypeFormGroup): IRoleType | NewRoleType {
    return form.getRawValue();
  }

  resetForm(form: RoleTypeFormGroup, roleType: RoleTypeFormGroupInput): void {
    const roleTypeRawValue = { ...this.getFormDefaults(), ...roleType };
    form.reset({
      ...roleTypeRawValue,
      id: { value: roleTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RoleTypeFormDefaults {
    return {
      id: null,
    };
  }
}
