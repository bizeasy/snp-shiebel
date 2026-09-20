import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaymentApplication, NewPaymentApplication } from '../payment-application.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentApplication for edit and NewPaymentApplicationFormGroupInput for create.
 */
type PaymentApplicationFormGroupInput = IPaymentApplication | PartialWithRequiredKeyOf<NewPaymentApplication>;

type PaymentApplicationFormDefaults = Pick<NewPaymentApplication, 'id'>;

type PaymentApplicationFormGroupContent = {
  id: FormControl<IPaymentApplication['id'] | NewPaymentApplication['id']>;
  amountApplied: FormControl<IPaymentApplication['amountApplied']>;
  payment: FormControl<IPaymentApplication['payment']>;
  toPayment: FormControl<IPaymentApplication['toPayment']>;
  invoice: FormControl<IPaymentApplication['invoice']>;
  invoiceItem: FormControl<IPaymentApplication['invoiceItem']>;
  order: FormControl<IPaymentApplication['order']>;
  orderItem: FormControl<IPaymentApplication['orderItem']>;
};

export type PaymentApplicationFormGroup = FormGroup<PaymentApplicationFormGroupContent>;

@Service()
export class PaymentApplicationFormService {
  createPaymentApplicationFormGroup(paymentApplication?: PaymentApplicationFormGroupInput): PaymentApplicationFormGroup {
    const paymentApplicationRawValue = {
      ...this.getFormDefaults(),
      ...(paymentApplication ?? { id: null }),
    };

    return new FormGroup<PaymentApplicationFormGroupContent>({
      id: new FormControl(
        { value: paymentApplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      amountApplied: new FormControl(paymentApplicationRawValue.amountApplied, {
        validators: [Validators.required],
      }),
      payment: new FormControl(paymentApplicationRawValue.payment, {
        validators: [Validators.required],
      }),
      toPayment: new FormControl(paymentApplicationRawValue.toPayment),
      invoice: new FormControl(paymentApplicationRawValue.invoice),
      invoiceItem: new FormControl(paymentApplicationRawValue.invoiceItem),
      order: new FormControl(paymentApplicationRawValue.order),
      orderItem: new FormControl(paymentApplicationRawValue.orderItem),
    });
  }

  getPaymentApplication(form: PaymentApplicationFormGroup): IPaymentApplication | NewPaymentApplication {
    return form.getRawValue();
  }

  resetForm(form: PaymentApplicationFormGroup, paymentApplication: PaymentApplicationFormGroupInput): void {
    const paymentApplicationRawValue = { ...this.getFormDefaults(), ...paymentApplication };
    form.reset({
      ...paymentApplicationRawValue,
      id: { value: paymentApplicationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentApplicationFormDefaults {
    return {
      id: null,
    };
  }
}
