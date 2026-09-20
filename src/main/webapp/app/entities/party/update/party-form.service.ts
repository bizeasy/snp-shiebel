import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IParty, NewParty } from '../party.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParty for edit and NewPartyFormGroupInput for create.
 */
type PartyFormGroupInput = IParty | PartialWithRequiredKeyOf<NewParty>;

type PartyFormDefaults = Pick<NewParty, 'id' | 'isOrganisation' | 'isTemporaryPassword'>;

type PartyFormGroupContent = {
  id: FormControl<IParty['id'] | NewParty['id']>;
  isOrganisation: FormControl<IParty['isOrganisation']>;
  organisationName: FormControl<IParty['organisationName']>;
  organisationShortName: FormControl<IParty['organisationShortName']>;
  salutation: FormControl<IParty['salutation']>;
  firstName: FormControl<IParty['firstName']>;
  middleName: FormControl<IParty['middleName']>;
  lastName: FormControl<IParty['lastName']>;
  gender: FormControl<IParty['gender']>;
  birthDate: FormControl<IParty['birthDate']>;
  primaryPhone: FormControl<IParty['primaryPhone']>;
  primaryEmail: FormControl<IParty['primaryEmail']>;
  isTemporaryPassword: FormControl<IParty['isTemporaryPassword']>;
  logoImageUrl: FormControl<IParty['logoImageUrl']>;
  profileImageUrl: FormControl<IParty['profileImageUrl']>;
  notes: FormControl<IParty['notes']>;
  postalAddress: FormControl<IParty['postalAddress']>;
  user: FormControl<IParty['user']>;
  partyType: FormControl<IParty['partyType']>;
  status: FormControl<IParty['status']>;
  address: FormControl<IParty['address']>;
};

export type PartyFormGroup = FormGroup<PartyFormGroupContent>;

@Service()
export class PartyFormService {
  createPartyFormGroup(party?: PartyFormGroupInput): PartyFormGroup {
    const partyRawValue = {
      ...this.getFormDefaults(),
      ...(party ?? { id: null }),
    };

    return new FormGroup<PartyFormGroupContent>({
      id: new FormControl(
        { value: partyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      isOrganisation: new FormControl(partyRawValue.isOrganisation),
      organisationName: new FormControl(partyRawValue.organisationName, {
        validators: [Validators.maxLength(200)],
      }),
      organisationShortName: new FormControl(partyRawValue.organisationShortName, {
        validators: [Validators.maxLength(100)],
      }),
      salutation: new FormControl(partyRawValue.salutation, {
        validators: [Validators.maxLength(20)],
      }),
      firstName: new FormControl(partyRawValue.firstName, {
        validators: [Validators.maxLength(60)],
      }),
      middleName: new FormControl(partyRawValue.middleName, {
        validators: [Validators.maxLength(60)],
      }),
      lastName: new FormControl(partyRawValue.lastName, {
        validators: [Validators.maxLength(60)],
      }),
      gender: new FormControl(partyRawValue.gender),
      birthDate: new FormControl(partyRawValue.birthDate),
      primaryPhone: new FormControl(partyRawValue.primaryPhone, {
        validators: [Validators.maxLength(20)],
      }),
      primaryEmail: new FormControl(partyRawValue.primaryEmail, {
        validators: [Validators.minLength(5), Validators.maxLength(75)],
      }),
      isTemporaryPassword: new FormControl(partyRawValue.isTemporaryPassword),
      logoImageUrl: new FormControl(partyRawValue.logoImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      profileImageUrl: new FormControl(partyRawValue.profileImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      notes: new FormControl(partyRawValue.notes),
      postalAddress: new FormControl(partyRawValue.postalAddress),
      user: new FormControl(partyRawValue.user),
      partyType: new FormControl(partyRawValue.partyType),
      status: new FormControl(partyRawValue.status),
      address: new FormControl(partyRawValue.address),
    });
  }

  getParty(form: PartyFormGroup): IParty | NewParty {
    return form.getRawValue();
  }

  resetForm(form: PartyFormGroup, party: PartyFormGroupInput): void {
    const partyRawValue = { ...this.getFormDefaults(), ...party };
    form.reset({
      ...partyRawValue,
      id: { value: partyRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyFormDefaults {
    return {
      id: null,
      isOrganisation: false,
      isTemporaryPassword: false,
    };
  }
}
