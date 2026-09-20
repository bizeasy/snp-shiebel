import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IUomConversion, NewUomConversion } from '../uom-conversion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUomConversion for edit and NewUomConversionFormGroupInput for create.
 */
type UomConversionFormGroupInput = IUomConversion | PartialWithRequiredKeyOf<NewUomConversion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUomConversion | NewUomConversion> = Omit<T, 'fromDate' | 'toDate'> & {
  fromDate?: string | null;
  toDate?: string | null;
};

type UomConversionFormRawValue = FormValueOf<IUomConversion>;

type NewUomConversionFormRawValue = FormValueOf<NewUomConversion>;

type UomConversionFormDefaults = Pick<NewUomConversion, 'id' | 'fromDate' | 'toDate'>;

type UomConversionFormGroupContent = {
  id: FormControl<UomConversionFormRawValue['id'] | NewUomConversion['id']>;
  name: FormControl<UomConversionFormRawValue['name']>;
  conversionFactor: FormControl<UomConversionFormRawValue['conversionFactor']>;
  fromDate: FormControl<UomConversionFormRawValue['fromDate']>;
  toDate: FormControl<UomConversionFormRawValue['toDate']>;
  fromUom: FormControl<UomConversionFormRawValue['fromUom']>;
  toUom: FormControl<UomConversionFormRawValue['toUom']>;
};

export type UomConversionFormGroup = FormGroup<UomConversionFormGroupContent>;

@Service()
export class UomConversionFormService {
  createUomConversionFormGroup(uomConversion?: UomConversionFormGroupInput): UomConversionFormGroup {
    const uomConversionRawValue = this.convertUomConversionToUomConversionRawValue({
      ...this.getFormDefaults(),
      ...(uomConversion ?? { id: null }),
    });

    return new FormGroup<UomConversionFormGroupContent>({
      id: new FormControl(
        { value: uomConversionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(uomConversionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      conversionFactor: new FormControl(uomConversionRawValue.conversionFactor, {
        validators: [Validators.required],
      }),
      fromDate: new FormControl(uomConversionRawValue.fromDate),
      toDate: new FormControl(uomConversionRawValue.toDate),
      fromUom: new FormControl(uomConversionRawValue.fromUom, {
        validators: [Validators.required],
      }),
      toUom: new FormControl(uomConversionRawValue.toUom, {
        validators: [Validators.required],
      }),
    });
  }

  getUomConversion(form: UomConversionFormGroup): IUomConversion | NewUomConversion {
    return this.convertUomConversionRawValueToUomConversion(form.getRawValue());
  }

  resetForm(form: UomConversionFormGroup, uomConversion: UomConversionFormGroupInput): void {
    const uomConversionRawValue = this.convertUomConversionToUomConversionRawValue({ ...this.getFormDefaults(), ...uomConversion });
    form.reset({
      ...uomConversionRawValue,
      id: { value: uomConversionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UomConversionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      toDate: currentTime,
    };
  }

  private convertUomConversionRawValueToUomConversion(
    rawUomConversion: UomConversionFormRawValue | NewUomConversionFormRawValue,
  ): IUomConversion | NewUomConversion {
    return {
      ...rawUomConversion,
      fromDate: dayjs(rawUomConversion.fromDate, DATE_TIME_FORMAT),
      toDate: dayjs(rawUomConversion.toDate, DATE_TIME_FORMAT),
    };
  }

  private convertUomConversionToUomConversionRawValue(
    uomConversion: IUomConversion | (Partial<NewUomConversion> & UomConversionFormDefaults),
  ): UomConversionFormRawValue | PartialWithRequiredKeyOf<NewUomConversionFormRawValue> {
    return {
      ...uomConversion,
      fromDate: uomConversion.fromDate ? uomConversion.fromDate.format(DATE_TIME_FORMAT) : undefined,
      toDate: uomConversion.toDate ? uomConversion.toDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
