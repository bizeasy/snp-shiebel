import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPartyClassification, NewPartyClassification } from '../party-classification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartyClassification for edit and NewPartyClassificationFormGroupInput for create.
 */
type PartyClassificationFormGroupInput = IPartyClassification | PartialWithRequiredKeyOf<NewPartyClassification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPartyClassification | NewPartyClassification> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type PartyClassificationFormRawValue = FormValueOf<IPartyClassification>;

type NewPartyClassificationFormRawValue = FormValueOf<NewPartyClassification>;

type PartyClassificationFormDefaults = Pick<NewPartyClassification, 'id' | 'fromDate' | 'thruDate'>;

type PartyClassificationFormGroupContent = {
  id: FormControl<PartyClassificationFormRawValue['id'] | NewPartyClassification['id']>;
  fromDate: FormControl<PartyClassificationFormRawValue['fromDate']>;
  thruDate: FormControl<PartyClassificationFormRawValue['thruDate']>;
  party: FormControl<PartyClassificationFormRawValue['party']>;
  classificationGroup: FormControl<PartyClassificationFormRawValue['classificationGroup']>;
};

export type PartyClassificationFormGroup = FormGroup<PartyClassificationFormGroupContent>;

@Service()
export class PartyClassificationFormService {
  createPartyClassificationFormGroup(partyClassification?: PartyClassificationFormGroupInput): PartyClassificationFormGroup {
    const partyClassificationRawValue = this.convertPartyClassificationToPartyClassificationRawValue({
      ...this.getFormDefaults(),
      ...(partyClassification ?? { id: null }),
    });

    return new FormGroup<PartyClassificationFormGroupContent>({
      id: new FormControl(
        { value: partyClassificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(partyClassificationRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(partyClassificationRawValue.thruDate),
      party: new FormControl(partyClassificationRawValue.party, {
        validators: [Validators.required],
      }),
      classificationGroup: new FormControl(partyClassificationRawValue.classificationGroup, {
        validators: [Validators.required],
      }),
    });
  }

  getPartyClassification(form: PartyClassificationFormGroup): IPartyClassification | NewPartyClassification {
    return this.convertPartyClassificationRawValueToPartyClassification(form.getRawValue());
  }

  resetForm(form: PartyClassificationFormGroup, partyClassification: PartyClassificationFormGroupInput): void {
    const partyClassificationRawValue = this.convertPartyClassificationToPartyClassificationRawValue({
      ...this.getFormDefaults(),
      ...partyClassification,
    });
    form.reset({
      ...partyClassificationRawValue,
      id: { value: partyClassificationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartyClassificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertPartyClassificationRawValueToPartyClassification(
    rawPartyClassification: PartyClassificationFormRawValue | NewPartyClassificationFormRawValue,
  ): IPartyClassification | NewPartyClassification {
    return {
      ...rawPartyClassification,
      fromDate: dayjs(rawPartyClassification.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawPartyClassification.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertPartyClassificationToPartyClassificationRawValue(
    partyClassification: IPartyClassification | (Partial<NewPartyClassification> & PartyClassificationFormDefaults),
  ): PartyClassificationFormRawValue | PartialWithRequiredKeyOf<NewPartyClassificationFormRawValue> {
    return {
      ...partyClassification,
      fromDate: partyClassification.fromDate ? partyClassification.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: partyClassification.thruDate ? partyClassification.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
