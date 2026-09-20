import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartyClassificationGroup, NewPartyClassificationGroup } from '../party-classification-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyClassificationGroup for edit and NewPartyClassificationGroupFormGroupInput for create.
 */
type PartyClassificationGroupFormGroupInput = IPartyClassificationGroup | PartialWithRequiredKeyOf<NewPartyClassificationGroup>;

type PartyClassificationGroupFormDefaults = Pick<NewPartyClassificationGroup, 'id'>;

type PartyClassificationGroupFormGroupContent = {
  id: FormControl<IPartyClassificationGroup['id'] | NewPartyClassificationGroup['id']>;
  name: FormControl<IPartyClassificationGroup['name']>;
  description: FormControl<IPartyClassificationGroup['description']>;
  classificationType: FormControl<IPartyClassificationGroup['classificationType']>;
};

export type PartyClassificationGroupFormGroup = FormGroup<PartyClassificationGroupFormGroupContent>;

@Service()
export class PartyClassificationGroupFormService {
  createPartyClassificationGroupFormGroup(
    partyClassificationGroup?: PartyClassificationGroupFormGroupInput,
  ): PartyClassificationGroupFormGroup {
    const partyClassificationGroupRawValue = {
      ...this.getFormDefaults(),
      ...(partyClassificationGroup ?? { id: null }),
    };

    return new FormGroup<PartyClassificationGroupFormGroupContent>({
      id: new FormControl(
        { value: partyClassificationGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(partyClassificationGroupRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(partyClassificationGroupRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
      classificationType: new FormControl(partyClassificationGroupRawValue.classificationType),
    });
  }

  getPartyClassificationGroup(form: PartyClassificationGroupFormGroup): IPartyClassificationGroup | NewPartyClassificationGroup {
    return form.getRawValue();
  }

  resetForm(form: PartyClassificationGroupFormGroup, partyClassificationGroup: PartyClassificationGroupFormGroupInput): void {
    const partyClassificationGroupRawValue = { ...this.getFormDefaults(), ...partyClassificationGroup };
    form.reset({
      ...partyClassificationGroupRawValue,
      id: { value: partyClassificationGroupRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyClassificationGroupFormDefaults {
    return {
      id: null,
    };
  }
}
