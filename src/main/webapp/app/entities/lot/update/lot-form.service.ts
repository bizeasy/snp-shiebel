import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ILot, NewLot } from '../lot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILot for edit and NewLotFormGroupInput for create.
 */
type LotFormGroupInput = ILot | PartialWithRequiredKeyOf<NewLot>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILot | NewLot> = Omit<T, 'creationDate' | 'expirationDate' | 'retestDate'> & {
  creationDate?: string | null;
  expirationDate?: string | null;
  retestDate?: string | null;
};

type LotFormRawValue = FormValueOf<ILot>;

type NewLotFormRawValue = FormValueOf<NewLot>;

type LotFormDefaults = Pick<NewLot, 'id' | 'creationDate' | 'expirationDate' | 'retestDate'>;

type LotFormGroupContent = {
  id: FormControl<LotFormRawValue['id'] | NewLot['id']>;
  creationDate: FormControl<LotFormRawValue['creationDate']>;
  quantity: FormControl<LotFormRawValue['quantity']>;
  expirationDate: FormControl<LotFormRawValue['expirationDate']>;
  retestDate: FormControl<LotFormRawValue['retestDate']>;
};

export type LotFormGroup = FormGroup<LotFormGroupContent>;

@Service()
export class LotFormService {
  createLotFormGroup(lot?: LotFormGroupInput): LotFormGroup {
    const lotRawValue = this.convertLotToLotRawValue({
      ...this.getFormDefaults(),
      ...(lot ?? { id: null }),
    });

    return new FormGroup<LotFormGroupContent>({
      id: new FormControl(
        { value: lotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      creationDate: new FormControl(lotRawValue.creationDate),
      quantity: new FormControl(lotRawValue.quantity),
      expirationDate: new FormControl(lotRawValue.expirationDate),
      retestDate: new FormControl(lotRawValue.retestDate),
    });
  }

  getLot(form: LotFormGroup): ILot | NewLot {
    return this.convertLotRawValueToLot(form.getRawValue());
  }

  resetForm(form: LotFormGroup, lot: LotFormGroupInput): void {
    const lotRawValue = this.convertLotToLotRawValue({ ...this.getFormDefaults(), ...lot });
    form.reset({
      ...lotRawValue,
      id: { value: lotRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): LotFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      expirationDate: currentTime,
      retestDate: currentTime,
    };
  }

  private convertLotRawValueToLot(rawLot: LotFormRawValue | NewLotFormRawValue): ILot | NewLot {
    return {
      ...rawLot,
      creationDate: dayjs(rawLot.creationDate, DATE_TIME_FORMAT),
      expirationDate: dayjs(rawLot.expirationDate, DATE_TIME_FORMAT),
      retestDate: dayjs(rawLot.retestDate, DATE_TIME_FORMAT),
    };
  }

  private convertLotToLotRawValue(
    lot: ILot | (Partial<NewLot> & LotFormDefaults),
  ): LotFormRawValue | PartialWithRequiredKeyOf<NewLotFormRawValue> {
    return {
      ...lot,
      creationDate: lot.creationDate ? lot.creationDate.format(DATE_TIME_FORMAT) : undefined,
      expirationDate: lot.expirationDate ? lot.expirationDate.format(DATE_TIME_FORMAT) : undefined,
      retestDate: lot.retestDate ? lot.retestDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
