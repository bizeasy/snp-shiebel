import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPickList, NewPickList } from '../pick-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPickList for edit and NewPickListFormGroupInput for create.
 */
type PickListFormGroupInput = IPickList | PartialWithRequiredKeyOf<NewPickList>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPickList | NewPickList> = Omit<T, 'pickListDate'> & {
  pickListDate?: string | null;
};

type PickListFormRawValue = FormValueOf<IPickList>;

type NewPickListFormRawValue = FormValueOf<NewPickList>;

type PickListFormDefaults = Pick<NewPickList, 'id' | 'pickListDate'>;

type PickListFormGroupContent = {
  id: FormControl<PickListFormRawValue['id'] | NewPickList['id']>;
  description: FormControl<PickListFormRawValue['description']>;
  pickListDate: FormControl<PickListFormRawValue['pickListDate']>;
  facility: FormControl<PickListFormRawValue['facility']>;
  status: FormControl<PickListFormRawValue['status']>;
  party: FormControl<PickListFormRawValue['party']>;
};

export type PickListFormGroup = FormGroup<PickListFormGroupContent>;

@Service()
export class PickListFormService {
  createPickListFormGroup(pickList?: PickListFormGroupInput): PickListFormGroup {
    const pickListRawValue = this.convertPickListToPickListRawValue({
      ...this.getFormDefaults(),
      ...(pickList ?? { id: null }),
    });

    return new FormGroup<PickListFormGroupContent>({
      id: new FormControl(
        { value: pickListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(pickListRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      pickListDate: new FormControl(pickListRawValue.pickListDate, {
        validators: [Validators.required],
      }),
      facility: new FormControl(pickListRawValue.facility),
      status: new FormControl(pickListRawValue.status),
      party: new FormControl(pickListRawValue.party),
    });
  }

  getPickList(form: PickListFormGroup): IPickList | NewPickList {
    return this.convertPickListRawValueToPickList(form.getRawValue());
  }

  resetForm(form: PickListFormGroup, pickList: PickListFormGroupInput): void {
    const pickListRawValue = this.convertPickListToPickListRawValue({ ...this.getFormDefaults(), ...pickList });
    form.reset({
      ...pickListRawValue,
      id: { value: pickListRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PickListFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      pickListDate: currentTime,
    };
  }

  private convertPickListRawValueToPickList(rawPickList: PickListFormRawValue | NewPickListFormRawValue): IPickList | NewPickList {
    return {
      ...rawPickList,
      pickListDate: dayjs(rawPickList.pickListDate, DATE_TIME_FORMAT),
    };
  }

  private convertPickListToPickListRawValue(
    pickList: IPickList | (Partial<NewPickList> & PickListFormDefaults),
  ): PickListFormRawValue | PartialWithRequiredKeyOf<NewPickListFormRawValue> {
    return {
      ...pickList,
      pickListDate: pickList.pickListDate ? pickList.pickListDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
