import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartyIdentificationType, NewPartyIdentificationType } from '../party-identification-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyIdentificationType for edit and NewPartyIdentificationTypeFormGroupInput for create.
 */
type PartyIdentificationTypeFormGroupInput = IPartyIdentificationType | PartialWithRequiredKeyOf<NewPartyIdentificationType>;

type PartyIdentificationTypeFormDefaults = Pick<NewPartyIdentificationType, 'id'>;

type PartyIdentificationTypeFormGroupContent = {
  id: FormControl<IPartyIdentificationType['id'] | NewPartyIdentificationType['id']>;
  name: FormControl<IPartyIdentificationType['name']>;
  description: FormControl<IPartyIdentificationType['description']>;
};

export type PartyIdentificationTypeFormGroup = FormGroup<PartyIdentificationTypeFormGroupContent>;

@Service()
export class PartyIdentificationTypeFormService {
  createPartyIdentificationTypeFormGroup(
    partyIdentificationType?: PartyIdentificationTypeFormGroupInput,
  ): PartyIdentificationTypeFormGroup {
    const partyIdentificationTypeRawValue = {
      ...this.getFormDefaults(),
      ...(partyIdentificationType ?? { id: null }),
    };

    return new FormGroup<PartyIdentificationTypeFormGroupContent>({
      id: new FormControl(
        { value: partyIdentificationTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(partyIdentificationTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(partyIdentificationTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
    });
  }

  getPartyIdentificationType(form: PartyIdentificationTypeFormGroup): IPartyIdentificationType | NewPartyIdentificationType {
    return form.getRawValue();
  }

  resetForm(form: PartyIdentificationTypeFormGroup, partyIdentificationType: PartyIdentificationTypeFormGroupInput): void {
    const partyIdentificationTypeRawValue = { ...this.getFormDefaults(), ...partyIdentificationType };
    form.reset({
      ...partyIdentificationTypeRawValue,
      id: { value: partyIdentificationTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyIdentificationTypeFormDefaults {
    return {
      id: null,
    };
  }
}
