import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartyIdentification, NewPartyIdentification } from '../party-identification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyIdentification for edit and NewPartyIdentificationFormGroupInput for create.
 */
type PartyIdentificationFormGroupInput = IPartyIdentification | PartialWithRequiredKeyOf<NewPartyIdentification>;

type PartyIdentificationFormDefaults = Pick<NewPartyIdentification, 'id'>;

type PartyIdentificationFormGroupContent = {
  id: FormControl<IPartyIdentification['id'] | NewPartyIdentification['id']>;
  idValue: FormControl<IPartyIdentification['idValue']>;
  party: FormControl<IPartyIdentification['party']>;
  type: FormControl<IPartyIdentification['type']>;
};

export type PartyIdentificationFormGroup = FormGroup<PartyIdentificationFormGroupContent>;

@Service()
export class PartyIdentificationFormService {
  createPartyIdentificationFormGroup(partyIdentification?: PartyIdentificationFormGroupInput): PartyIdentificationFormGroup {
    const partyIdentificationRawValue = {
      ...this.getFormDefaults(),
      ...(partyIdentification ?? { id: null }),
    };

    return new FormGroup<PartyIdentificationFormGroupContent>({
      id: new FormControl(
        { value: partyIdentificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idValue: new FormControl(partyIdentificationRawValue.idValue, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      party: new FormControl(partyIdentificationRawValue.party, {
        validators: [Validators.required],
      }),
      type: new FormControl(partyIdentificationRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getPartyIdentification(form: PartyIdentificationFormGroup): IPartyIdentification | NewPartyIdentification {
    return form.getRawValue();
  }

  resetForm(form: PartyIdentificationFormGroup, partyIdentification: PartyIdentificationFormGroupInput): void {
    const partyIdentificationRawValue = { ...this.getFormDefaults(), ...partyIdentification };
    form.reset({
      ...partyIdentificationRawValue,
      id: { value: partyIdentificationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyIdentificationFormDefaults {
    return {
      id: null,
    };
  }
}
