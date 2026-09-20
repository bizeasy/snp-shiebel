import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustRequestRole, NewCustRequestRole } from '../cust-request-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequestRole for edit and NewCustRequestRoleFormGroupInput for create.
 */
type CustRequestRoleFormGroupInput = ICustRequestRole | PartialWithRequiredKeyOf<NewCustRequestRole>;

type CustRequestRoleFormDefaults = Pick<NewCustRequestRole, 'id'>;

type CustRequestRoleFormGroupContent = {
  id: FormControl<ICustRequestRole['id'] | NewCustRequestRole['id']>;
  custRequest: FormControl<ICustRequestRole['custRequest']>;
  custRequestItem: FormControl<ICustRequestRole['custRequestItem']>;
  party: FormControl<ICustRequestRole['party']>;
  roleType: FormControl<ICustRequestRole['roleType']>;
};

export type CustRequestRoleFormGroup = FormGroup<CustRequestRoleFormGroupContent>;

@Service()
export class CustRequestRoleFormService {
  createCustRequestRoleFormGroup(custRequestRole?: CustRequestRoleFormGroupInput): CustRequestRoleFormGroup {
    const custRequestRoleRawValue = {
      ...this.getFormDefaults(),
      ...(custRequestRole ?? { id: null }),
    };

    return new FormGroup<CustRequestRoleFormGroupContent>({
      id: new FormControl(
        { value: custRequestRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      custRequest: new FormControl(custRequestRoleRawValue.custRequest),
      custRequestItem: new FormControl(custRequestRoleRawValue.custRequestItem),
      party: new FormControl(custRequestRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(custRequestRoleRawValue.roleType),
    });
  }

  getCustRequestRole(form: CustRequestRoleFormGroup): ICustRequestRole | NewCustRequestRole {
    return form.getRawValue();
  }

  resetForm(form: CustRequestRoleFormGroup, custRequestRole: CustRequestRoleFormGroupInput): void {
    const custRequestRoleRawValue = { ...this.getFormDefaults(), ...custRequestRole };
    form.reset({
      ...custRequestRoleRawValue,
      id: { value: custRequestRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestRoleFormDefaults {
    return {
      id: null,
    };
  }
}
