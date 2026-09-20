import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPartyRelationship, NewPartyRelationship } from '../party-relationship.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyRelationship for edit and NewPartyRelationshipFormGroupInput for create.
 */
type PartyRelationshipFormGroupInput = IPartyRelationship | PartialWithRequiredKeyOf<NewPartyRelationship>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPartyRelationship | NewPartyRelationship> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type PartyRelationshipFormRawValue = FormValueOf<IPartyRelationship>;

type NewPartyRelationshipFormRawValue = FormValueOf<NewPartyRelationship>;

type PartyRelationshipFormDefaults = Pick<NewPartyRelationship, 'id' | 'fromDate' | 'thruDate'>;

type PartyRelationshipFormGroupContent = {
  id: FormControl<PartyRelationshipFormRawValue['id'] | NewPartyRelationship['id']>;
  fromDate: FormControl<PartyRelationshipFormRawValue['fromDate']>;
  thruDate: FormControl<PartyRelationshipFormRawValue['thruDate']>;
  relationshipName: FormControl<PartyRelationshipFormRawValue['relationshipName']>;
  partyFrom: FormControl<PartyRelationshipFormRawValue['partyFrom']>;
  partyTo: FormControl<PartyRelationshipFormRawValue['partyTo']>;
  roleTypeFrom: FormControl<PartyRelationshipFormRawValue['roleTypeFrom']>;
  roleTypeTo: FormControl<PartyRelationshipFormRawValue['roleTypeTo']>;
  relationshipType: FormControl<PartyRelationshipFormRawValue['relationshipType']>;
};

export type PartyRelationshipFormGroup = FormGroup<PartyRelationshipFormGroupContent>;

@Service()
export class PartyRelationshipFormService {
  createPartyRelationshipFormGroup(partyRelationship?: PartyRelationshipFormGroupInput): PartyRelationshipFormGroup {
    const partyRelationshipRawValue = this.convertPartyRelationshipToPartyRelationshipRawValue({
      ...this.getFormDefaults(),
      ...(partyRelationship ?? { id: null }),
    });

    return new FormGroup<PartyRelationshipFormGroupContent>({
      id: new FormControl(
        { value: partyRelationshipRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(partyRelationshipRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(partyRelationshipRawValue.thruDate),
      relationshipName: new FormControl(partyRelationshipRawValue.relationshipName, {
        validators: [Validators.maxLength(100)],
      }),
      partyFrom: new FormControl(partyRelationshipRawValue.partyFrom, {
        validators: [Validators.required],
      }),
      partyTo: new FormControl(partyRelationshipRawValue.partyTo, {
        validators: [Validators.required],
      }),
      roleTypeFrom: new FormControl(partyRelationshipRawValue.roleTypeFrom),
      roleTypeTo: new FormControl(partyRelationshipRawValue.roleTypeTo),
      relationshipType: new FormControl(partyRelationshipRawValue.relationshipType),
    });
  }

  getPartyRelationship(form: PartyRelationshipFormGroup): IPartyRelationship | NewPartyRelationship {
    return this.convertPartyRelationshipRawValueToPartyRelationship(form.getRawValue());
  }

  resetForm(form: PartyRelationshipFormGroup, partyRelationship: PartyRelationshipFormGroupInput): void {
    const partyRelationshipRawValue = this.convertPartyRelationshipToPartyRelationshipRawValue({
      ...this.getFormDefaults(),
      ...partyRelationship,
    });
    form.reset({
      ...partyRelationshipRawValue,
      id: { value: partyRelationshipRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyRelationshipFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertPartyRelationshipRawValueToPartyRelationship(
    rawPartyRelationship: PartyRelationshipFormRawValue | NewPartyRelationshipFormRawValue,
  ): IPartyRelationship | NewPartyRelationship {
    return {
      ...rawPartyRelationship,
      fromDate: dayjs(rawPartyRelationship.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawPartyRelationship.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertPartyRelationshipToPartyRelationshipRawValue(
    partyRelationship: IPartyRelationship | (Partial<NewPartyRelationship> & PartyRelationshipFormDefaults),
  ): PartyRelationshipFormRawValue | PartialWithRequiredKeyOf<NewPartyRelationshipFormRawValue> {
    return {
      ...partyRelationship,
      fromDate: partyRelationship.fromDate ? partyRelationship.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: partyRelationship.thruDate ? partyRelationship.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
