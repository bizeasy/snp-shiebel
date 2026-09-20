import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPaymentMethod, NewPaymentMethod } from '../payment-method.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentMethod for edit and NewPaymentMethodFormGroupInput for create.
 */
type PaymentMethodFormGroupInput = IPaymentMethod | PartialWithRequiredKeyOf<NewPaymentMethod>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPaymentMethod | NewPaymentMethod> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type PaymentMethodFormRawValue = FormValueOf<IPaymentMethod>;

type NewPaymentMethodFormRawValue = FormValueOf<NewPaymentMethod>;

type PaymentMethodFormDefaults = Pick<NewPaymentMethod, 'id' | 'fromDate' | 'thruDate'>;

type PaymentMethodFormGroupContent = {
  id: FormControl<PaymentMethodFormRawValue['id'] | NewPaymentMethod['id']>;
  name: FormControl<PaymentMethodFormRawValue['name']>;
  description: FormControl<PaymentMethodFormRawValue['description']>;
  fromDate: FormControl<PaymentMethodFormRawValue['fromDate']>;
  thruDate: FormControl<PaymentMethodFormRawValue['thruDate']>;
  paymentMethodType: FormControl<PaymentMethodFormRawValue['paymentMethodType']>;
  party: FormControl<PaymentMethodFormRawValue['party']>;
};

export type PaymentMethodFormGroup = FormGroup<PaymentMethodFormGroupContent>;

@Service()
export class PaymentMethodFormService {
  createPaymentMethodFormGroup(paymentMethod?: PaymentMethodFormGroupInput): PaymentMethodFormGroup {
    const paymentMethodRawValue = this.convertPaymentMethodToPaymentMethodRawValue({
      ...this.getFormDefaults(),
      ...(paymentMethod ?? { id: null }),
    });

    return new FormGroup<PaymentMethodFormGroupContent>({
      id: new FormControl(
        { value: paymentMethodRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(paymentMethodRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(paymentMethodRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
      fromDate: new FormControl(paymentMethodRawValue.fromDate),
      thruDate: new FormControl(paymentMethodRawValue.thruDate),
      paymentMethodType: new FormControl(paymentMethodRawValue.paymentMethodType, {
        validators: [Validators.required],
      }),
      party: new FormControl(paymentMethodRawValue.party),
    });
  }

  getPaymentMethod(form: PaymentMethodFormGroup): IPaymentMethod | NewPaymentMethod {
    return this.convertPaymentMethodRawValueToPaymentMethod(form.getRawValue());
  }

  resetForm(form: PaymentMethodFormGroup, paymentMethod: PaymentMethodFormGroupInput): void {
    const paymentMethodRawValue = this.convertPaymentMethodToPaymentMethodRawValue({ ...this.getFormDefaults(), ...paymentMethod });
    form.reset({
      ...paymentMethodRawValue,
      id: { value: paymentMethodRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentMethodFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertPaymentMethodRawValueToPaymentMethod(
    rawPaymentMethod: PaymentMethodFormRawValue | NewPaymentMethodFormRawValue,
  ): IPaymentMethod | NewPaymentMethod {
    return {
      ...rawPaymentMethod,
      fromDate: dayjs(rawPaymentMethod.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawPaymentMethod.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentMethodToPaymentMethodRawValue(
    paymentMethod: IPaymentMethod | (Partial<NewPaymentMethod> & PaymentMethodFormDefaults),
  ): PaymentMethodFormRawValue | PartialWithRequiredKeyOf<NewPaymentMethodFormRawValue> {
    return {
      ...paymentMethod,
      fromDate: paymentMethod.fromDate ? paymentMethod.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: paymentMethod.thruDate ? paymentMethod.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
