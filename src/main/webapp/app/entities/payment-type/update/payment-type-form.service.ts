import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaymentType, NewPaymentType } from '../payment-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentType for edit and NewPaymentTypeFormGroupInput for create.
 */
type PaymentTypeFormGroupInput = IPaymentType | PartialWithRequiredKeyOf<NewPaymentType>;

type PaymentTypeFormDefaults = Pick<NewPaymentType, 'id'>;

type PaymentTypeFormGroupContent = {
  id: FormControl<IPaymentType['id'] | NewPaymentType['id']>;
  name: FormControl<IPaymentType['name']>;
  description: FormControl<IPaymentType['description']>;
};

export type PaymentTypeFormGroup = FormGroup<PaymentTypeFormGroupContent>;

@Service()
export class PaymentTypeFormService {
  createPaymentTypeFormGroup(paymentType?: PaymentTypeFormGroupInput): PaymentTypeFormGroup {
    const paymentTypeRawValue = {
      ...this.getFormDefaults(),
      ...(paymentType ?? { id: null }),
    };

    return new FormGroup<PaymentTypeFormGroupContent>({
      id: new FormControl(
        { value: paymentTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(paymentTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(paymentTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
    });
  }

  getPaymentType(form: PaymentTypeFormGroup): IPaymentType | NewPaymentType {
    return form.getRawValue();
  }

  resetForm(form: PaymentTypeFormGroup, paymentType: PaymentTypeFormGroupInput): void {
    const paymentTypeRawValue = { ...this.getFormDefaults(), ...paymentType };
    form.reset({
      ...paymentTypeRawValue,
      id: { value: paymentTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentTypeFormDefaults {
    return {
      id: null,
    };
  }
}
