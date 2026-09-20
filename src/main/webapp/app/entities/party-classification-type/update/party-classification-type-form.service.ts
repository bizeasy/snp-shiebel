import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartyClassificationType, NewPartyClassificationType } from '../party-classification-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyClassificationType for edit and NewPartyClassificationTypeFormGroupInput for create.
 */
type PartyClassificationTypeFormGroupInput = IPartyClassificationType | PartialWithRequiredKeyOf<NewPartyClassificationType>;

type PartyClassificationTypeFormDefaults = Pick<NewPartyClassificationType, 'id'>;

type PartyClassificationTypeFormGroupContent = {
  id: FormControl<IPartyClassificationType['id'] | NewPartyClassificationType['id']>;
  name: FormControl<IPartyClassificationType['name']>;
  description: FormControl<IPartyClassificationType['description']>;
};

export type PartyClassificationTypeFormGroup = FormGroup<PartyClassificationTypeFormGroupContent>;

@Service()
export class PartyClassificationTypeFormService {
  createPartyClassificationTypeFormGroup(
    partyClassificationType?: PartyClassificationTypeFormGroupInput,
  ): PartyClassificationTypeFormGroup {
    const partyClassificationTypeRawValue = {
      ...this.getFormDefaults(),
      ...(partyClassificationType ?? { id: null }),
    };

    return new FormGroup<PartyClassificationTypeFormGroupContent>({
      id: new FormControl(
        { value: partyClassificationTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(partyClassificationTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(partyClassificationTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
    });
  }

  getPartyClassificationType(form: PartyClassificationTypeFormGroup): IPartyClassificationType | NewPartyClassificationType {
    return form.getRawValue();
  }

  resetForm(form: PartyClassificationTypeFormGroup, partyClassificationType: PartyClassificationTypeFormGroupInput): void {
    const partyClassificationTypeRawValue = { ...this.getFormDefaults(), ...partyClassificationType };
    form.reset({
      ...partyClassificationTypeRawValue,
      id: { value: partyClassificationTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyClassificationTypeFormDefaults {
    return {
      id: null,
    };
  }
}
