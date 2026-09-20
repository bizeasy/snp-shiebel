import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaymentMethodType, NewPaymentMethodType } from '../payment-method-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentMethodType for edit and NewPaymentMethodTypeFormGroupInput for create.
 */
type PaymentMethodTypeFormGroupInput = IPaymentMethodType | PartialWithRequiredKeyOf<NewPaymentMethodType>;

type PaymentMethodTypeFormDefaults = Pick<NewPaymentMethodType, 'id'>;

type PaymentMethodTypeFormGroupContent = {
  id: FormControl<IPaymentMethodType['id'] | NewPaymentMethodType['id']>;
  name: FormControl<IPaymentMethodType['name']>;
  description: FormControl<IPaymentMethodType['description']>;
};

export type PaymentMethodTypeFormGroup = FormGroup<PaymentMethodTypeFormGroupContent>;

@Service()
export class PaymentMethodTypeFormService {
  createPaymentMethodTypeFormGroup(paymentMethodType?: PaymentMethodTypeFormGroupInput): PaymentMethodTypeFormGroup {
    const paymentMethodTypeRawValue = {
      ...this.getFormDefaults(),
      ...(paymentMethodType ?? { id: null }),
    };

    return new FormGroup<PaymentMethodTypeFormGroupContent>({
      id: new FormControl(
        { value: paymentMethodTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(paymentMethodTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(paymentMethodTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
    });
  }

  getPaymentMethodType(form: PaymentMethodTypeFormGroup): IPaymentMethodType | NewPaymentMethodType {
    return form.getRawValue();
  }

  resetForm(form: PaymentMethodTypeFormGroup, paymentMethodType: PaymentMethodTypeFormGroupInput): void {
    const paymentMethodTypeRawValue = { ...this.getFormDefaults(), ...paymentMethodType };
    form.reset({
      ...paymentMethodTypeRawValue,
      id: { value: paymentMethodTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentMethodTypeFormDefaults {
    return {
      id: null,
    };
  }
}
