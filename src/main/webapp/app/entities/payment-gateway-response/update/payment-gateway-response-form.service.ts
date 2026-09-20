import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaymentGatewayResponse, NewPaymentGatewayResponse } from '../payment-gateway-response.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentGatewayResponse for edit and NewPaymentGatewayResponseFormGroupInput for create.
 */
type PaymentGatewayResponseFormGroupInput = IPaymentGatewayResponse | PartialWithRequiredKeyOf<NewPaymentGatewayResponse>;

type PaymentGatewayResponseFormDefaults = Pick<NewPaymentGatewayResponse, 'id'>;

type PaymentGatewayResponseFormGroupContent = {
  id: FormControl<IPaymentGatewayResponse['id'] | NewPaymentGatewayResponse['id']>;
  referenceNumber: FormControl<IPaymentGatewayResponse['referenceNumber']>;
  amount: FormControl<IPaymentGatewayResponse['amount']>;
  gatewayMessage: FormControl<IPaymentGatewayResponse['gatewayMessage']>;
  paymentGatewayConfig: FormControl<IPaymentGatewayResponse['paymentGatewayConfig']>;
  paymentMethodType: FormControl<IPaymentGatewayResponse['paymentMethodType']>;
};

export type PaymentGatewayResponseFormGroup = FormGroup<PaymentGatewayResponseFormGroupContent>;

@Service()
export class PaymentGatewayResponseFormService {
  createPaymentGatewayResponseFormGroup(paymentGatewayResponse?: PaymentGatewayResponseFormGroupInput): PaymentGatewayResponseFormGroup {
    const paymentGatewayResponseRawValue = {
      ...this.getFormDefaults(),
      ...(paymentGatewayResponse ?? { id: null }),
    };

    return new FormGroup<PaymentGatewayResponseFormGroupContent>({
      id: new FormControl(
        { value: paymentGatewayResponseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      referenceNumber: new FormControl(paymentGatewayResponseRawValue.referenceNumber, {
        validators: [Validators.maxLength(25)],
      }),
      amount: new FormControl(paymentGatewayResponseRawValue.amount),
      gatewayMessage: new FormControl(paymentGatewayResponseRawValue.gatewayMessage, {
        validators: [Validators.maxLength(2000)],
      }),
      paymentGatewayConfig: new FormControl(paymentGatewayResponseRawValue.paymentGatewayConfig),
      paymentMethodType: new FormControl(paymentGatewayResponseRawValue.paymentMethodType),
    });
  }

  getPaymentGatewayResponse(form: PaymentGatewayResponseFormGroup): IPaymentGatewayResponse | NewPaymentGatewayResponse {
    return form.getRawValue();
  }

  resetForm(form: PaymentGatewayResponseFormGroup, paymentGatewayResponse: PaymentGatewayResponseFormGroupInput): void {
    const paymentGatewayResponseRawValue = { ...this.getFormDefaults(), ...paymentGatewayResponse };
    form.reset({
      ...paymentGatewayResponseRawValue,
      id: { value: paymentGatewayResponseRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentGatewayResponseFormDefaults {
    return {
      id: null,
    };
  }
}
