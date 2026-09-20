import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartyRole, NewPartyRole } from '../party-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyRole for edit and NewPartyRoleFormGroupInput for create.
 */
type PartyRoleFormGroupInput = IPartyRole | PartialWithRequiredKeyOf<NewPartyRole>;

type PartyRoleFormDefaults = Pick<NewPartyRole, 'id'>;

type PartyRoleFormGroupContent = {
  id: FormControl<IPartyRole['id'] | NewPartyRole['id']>;
  party: FormControl<IPartyRole['party']>;
  roleType: FormControl<IPartyRole['roleType']>;
};

export type PartyRoleFormGroup = FormGroup<PartyRoleFormGroupContent>;

@Service()
export class PartyRoleFormService {
  createPartyRoleFormGroup(partyRole?: PartyRoleFormGroupInput): PartyRoleFormGroup {
    const partyRoleRawValue = {
      ...this.getFormDefaults(),
      ...(partyRole ?? { id: null }),
    };

    return new FormGroup<PartyRoleFormGroupContent>({
      id: new FormControl(
        { value: partyRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      party: new FormControl(partyRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(partyRoleRawValue.roleType, {
        validators: [Validators.required],
      }),
    });
  }

  getPartyRole(form: PartyRoleFormGroup): IPartyRole | NewPartyRole {
    return form.getRawValue();
  }

  resetForm(form: PartyRoleFormGroup, partyRole: PartyRoleFormGroupInput): void {
    const partyRoleRawValue = { ...this.getFormDefaults(), ...partyRole };
    form.reset({
      ...partyRoleRawValue,
      id: { value: partyRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyRoleFormDefaults {
    return {
      id: null,
    };
  }
}
