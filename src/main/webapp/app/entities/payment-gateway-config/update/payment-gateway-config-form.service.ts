import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaymentGatewayConfig, NewPaymentGatewayConfig } from '../payment-gateway-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentGatewayConfig for edit and NewPaymentGatewayConfigFormGroupInput for create.
 */
type PaymentGatewayConfigFormGroupInput = IPaymentGatewayConfig | PartialWithRequiredKeyOf<NewPaymentGatewayConfig>;

type PaymentGatewayConfigFormDefaults = Pick<NewPaymentGatewayConfig, 'id'>;

type PaymentGatewayConfigFormGroupContent = {
  id: FormControl<IPaymentGatewayConfig['id'] | NewPaymentGatewayConfig['id']>;
  name: FormControl<IPaymentGatewayConfig['name']>;
  authUrl: FormControl<IPaymentGatewayConfig['authUrl']>;
  releaseUrl: FormControl<IPaymentGatewayConfig['releaseUrl']>;
  refundUrl: FormControl<IPaymentGatewayConfig['refundUrl']>;
};

export type PaymentGatewayConfigFormGroup = FormGroup<PaymentGatewayConfigFormGroupContent>;

@Service()
export class PaymentGatewayConfigFormService {
  createPaymentGatewayConfigFormGroup(paymentGatewayConfig?: PaymentGatewayConfigFormGroupInput): PaymentGatewayConfigFormGroup {
    const paymentGatewayConfigRawValue = {
      ...this.getFormDefaults(),
      ...(paymentGatewayConfig ?? { id: null }),
    };

    return new FormGroup<PaymentGatewayConfigFormGroupContent>({
      id: new FormControl(
        { value: paymentGatewayConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(paymentGatewayConfigRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      authUrl: new FormControl(paymentGatewayConfigRawValue.authUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      releaseUrl: new FormControl(paymentGatewayConfigRawValue.releaseUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      refundUrl: new FormControl(paymentGatewayConfigRawValue.refundUrl, {
        validators: [Validators.maxLength(2000)],
      }),
    });
  }

  getPaymentGatewayConfig(form: PaymentGatewayConfigFormGroup): IPaymentGatewayConfig | NewPaymentGatewayConfig {
    return form.getRawValue();
  }

  resetForm(form: PaymentGatewayConfigFormGroup, paymentGatewayConfig: PaymentGatewayConfigFormGroupInput): void {
    const paymentGatewayConfigRawValue = { ...this.getFormDefaults(), ...paymentGatewayConfig };
    form.reset({
      ...paymentGatewayConfigRawValue,
      id: { value: paymentGatewayConfigRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentGatewayConfigFormDefaults {
    return {
      id: null,
    };
  }
}
