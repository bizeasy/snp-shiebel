import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPartyContactMech, NewPartyContactMech } from '../party-contact-mech.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyContactMech for edit and NewPartyContactMechFormGroupInput for create.
 */
type PartyContactMechFormGroupInput = IPartyContactMech | PartialWithRequiredKeyOf<NewPartyContactMech>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPartyContactMech | NewPartyContactMech> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type PartyContactMechFormRawValue = FormValueOf<IPartyContactMech>;

type NewPartyContactMechFormRawValue = FormValueOf<NewPartyContactMech>;

type PartyContactMechFormDefaults = Pick<NewPartyContactMech, 'id' | 'fromDate' | 'thruDate'>;

type PartyContactMechFormGroupContent = {
  id: FormControl<PartyContactMechFormRawValue['id'] | NewPartyContactMech['id']>;
  fromDate: FormControl<PartyContactMechFormRawValue['fromDate']>;
  thruDate: FormControl<PartyContactMechFormRawValue['thruDate']>;
  party: FormControl<PartyContactMechFormRawValue['party']>;
  contactMech: FormControl<PartyContactMechFormRawValue['contactMech']>;
  contactMechPurpose: FormControl<PartyContactMechFormRawValue['contactMechPurpose']>;
};

export type PartyContactMechFormGroup = FormGroup<PartyContactMechFormGroupContent>;

@Service()
export class PartyContactMechFormService {
  createPartyContactMechFormGroup(partyContactMech?: PartyContactMechFormGroupInput): PartyContactMechFormGroup {
    const partyContactMechRawValue = this.convertPartyContactMechToPartyContactMechRawValue({
      ...this.getFormDefaults(),
      ...(partyContactMech ?? { id: null }),
    });

    return new FormGroup<PartyContactMechFormGroupContent>({
      id: new FormControl(
        { value: partyContactMechRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(partyContactMechRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(partyContactMechRawValue.thruDate),
      party: new FormControl(partyContactMechRawValue.party, {
        validators: [Validators.required],
      }),
      contactMech: new FormControl(partyContactMechRawValue.contactMech, {
        validators: [Validators.required],
      }),
      contactMechPurpose: new FormControl(partyContactMechRawValue.contactMechPurpose),
    });
  }

  getPartyContactMech(form: PartyContactMechFormGroup): IPartyContactMech | NewPartyContactMech {
    return this.convertPartyContactMechRawValueToPartyContactMech(form.getRawValue());
  }

  resetForm(form: PartyContactMechFormGroup, partyContactMech: PartyContactMechFormGroupInput): void {
    const partyContactMechRawValue = this.convertPartyContactMechToPartyContactMechRawValue({
      ...this.getFormDefaults(),
      ...partyContactMech,
    });
    form.reset({
      ...partyContactMechRawValue,
      id: { value: partyContactMechRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyContactMechFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertPartyContactMechRawValueToPartyContactMech(
    rawPartyContactMech: PartyContactMechFormRawValue | NewPartyContactMechFormRawValue,
  ): IPartyContactMech | NewPartyContactMech {
    return {
      ...rawPartyContactMech,
      fromDate: dayjs(rawPartyContactMech.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawPartyContactMech.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertPartyContactMechToPartyContactMechRawValue(
    partyContactMech: IPartyContactMech | (Partial<NewPartyContactMech> & PartyContactMechFormDefaults),
  ): PartyContactMechFormRawValue | PartialWithRequiredKeyOf<NewPartyContactMechFormRawValue> {
    return {
      ...partyContactMech,
      fromDate: partyContactMech.fromDate ? partyContactMech.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: partyContactMech.thruDate ? partyContactMech.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
