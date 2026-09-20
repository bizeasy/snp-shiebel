import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartyRelationshipType, NewPartyRelationshipType } from '../party-relationship-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyRelationshipType for edit and NewPartyRelationshipTypeFormGroupInput for create.
 */
type PartyRelationshipTypeFormGroupInput = IPartyRelationshipType | PartialWithRequiredKeyOf<NewPartyRelationshipType>;

type PartyRelationshipTypeFormDefaults = Pick<NewPartyRelationshipType, 'id'>;

type PartyRelationshipTypeFormGroupContent = {
  id: FormControl<IPartyRelationshipType['id'] | NewPartyRelationshipType['id']>;
  name: FormControl<IPartyRelationshipType['name']>;
  description: FormControl<IPartyRelationshipType['description']>;
  roleTypeFrom: FormControl<IPartyRelationshipType['roleTypeFrom']>;
  roleTypeTo: FormControl<IPartyRelationshipType['roleTypeTo']>;
};

export type PartyRelationshipTypeFormGroup = FormGroup<PartyRelationshipTypeFormGroupContent>;

@Service()
export class PartyRelationshipTypeFormService {
  createPartyRelationshipTypeFormGroup(partyRelationshipType?: PartyRelationshipTypeFormGroupInput): PartyRelationshipTypeFormGroup {
    const partyRelationshipTypeRawValue = {
      ...this.getFormDefaults(),
      ...(partyRelationshipType ?? { id: null }),
    };

    return new FormGroup<PartyRelationshipTypeFormGroupContent>({
      id: new FormControl(
        { value: partyRelationshipTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(partyRelationshipTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(partyRelationshipTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
      roleTypeFrom: new FormControl(partyRelationshipTypeRawValue.roleTypeFrom),
      roleTypeTo: new FormControl(partyRelationshipTypeRawValue.roleTypeTo),
    });
  }

  getPartyRelationshipType(form: PartyRelationshipTypeFormGroup): IPartyRelationshipType | NewPartyRelationshipType {
    return form.getRawValue();
  }

  resetForm(form: PartyRelationshipTypeFormGroup, partyRelationshipType: PartyRelationshipTypeFormGroupInput): void {
    const partyRelationshipTypeRawValue = { ...this.getFormDefaults(), ...partyRelationshipType };
    form.reset({
      ...partyRelationshipTypeRawValue,
      id: { value: partyRelationshipTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyRelationshipTypeFormDefaults {
    return {
      id: null,
    };
  }
}
