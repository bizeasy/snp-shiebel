import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayment | NewPayment> = Omit<T, 'effectiveDate' | 'paymentDate'> & {
  effectiveDate?: string | null;
  paymentDate?: string | null;
};

type PaymentFormRawValue = FormValueOf<IPayment>;

type NewPaymentFormRawValue = FormValueOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id' | 'effectiveDate' | 'paymentDate'>;

type PaymentFormGroupContent = {
  id: FormControl<PaymentFormRawValue['id'] | NewPayment['id']>;
  paymentRefNumber: FormControl<PaymentFormRawValue['paymentRefNumber']>;
  effectiveDate: FormControl<PaymentFormRawValue['effectiveDate']>;
  paymentDate: FormControl<PaymentFormRawValue['paymentDate']>;
  amount: FormControl<PaymentFormRawValue['amount']>;
  actualAmount: FormControl<PaymentFormRawValue['actualAmount']>;
  paymentStatus: FormControl<PaymentFormRawValue['paymentStatus']>;
  mihpayId: FormControl<PaymentFormRawValue['mihpayId']>;
  txnId: FormControl<PaymentFormRawValue['txnId']>;
  email: FormControl<PaymentFormRawValue['email']>;
  phone: FormControl<PaymentFormRawValue['phone']>;
  productInfo: FormControl<PaymentFormRawValue['productInfo']>;
  paymentType: FormControl<PaymentFormRawValue['paymentType']>;
  paymentMethodType: FormControl<PaymentFormRawValue['paymentMethodType']>;
  paymentMethod: FormControl<PaymentFormRawValue['paymentMethod']>;
  paymentGatewayResponse: FormControl<PaymentFormRawValue['paymentGatewayResponse']>;
  status: FormControl<PaymentFormRawValue['status']>;
  fromParty: FormControl<PaymentFormRawValue['fromParty']>;
  toParty: FormControl<PaymentFormRawValue['toParty']>;
  roleType: FormControl<PaymentFormRawValue['roleType']>;
  currencyUom: FormControl<PaymentFormRawValue['currencyUom']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Service()
export class PaymentFormService {
  createPaymentFormGroup(payment?: PaymentFormGroupInput): PaymentFormGroup {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({
      ...this.getFormDefaults(),
      ...(payment ?? { id: null }),
    });

    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      paymentRefNumber: new FormControl(paymentRawValue.paymentRefNumber, {
        validators: [Validators.maxLength(60)],
      }),
      effectiveDate: new FormControl(paymentRawValue.effectiveDate),
      paymentDate: new FormControl(paymentRawValue.paymentDate),
      amount: new FormControl(paymentRawValue.amount, {
        validators: [Validators.required],
      }),
      actualAmount: new FormControl(paymentRawValue.actualAmount),
      paymentStatus: new FormControl(paymentRawValue.paymentStatus, {
        validators: [Validators.maxLength(60)],
      }),
      mihpayId: new FormControl(paymentRawValue.mihpayId, {
        validators: [Validators.maxLength(60)],
      }),
      txnId: new FormControl(paymentRawValue.txnId, {
        validators: [Validators.maxLength(60)],
      }),
      email: new FormControl(paymentRawValue.email, {
        validators: [Validators.minLength(5), Validators.maxLength(75)],
      }),
      phone: new FormControl(paymentRawValue.phone, {
        validators: [Validators.maxLength(20)],
      }),
      productInfo: new FormControl(paymentRawValue.productInfo, {
        validators: [Validators.maxLength(255)],
      }),
      paymentType: new FormControl(paymentRawValue.paymentType),
      paymentMethodType: new FormControl(paymentRawValue.paymentMethodType),
      paymentMethod: new FormControl(paymentRawValue.paymentMethod),
      paymentGatewayResponse: new FormControl(paymentRawValue.paymentGatewayResponse),
      status: new FormControl(paymentRawValue.status),
      fromParty: new FormControl(paymentRawValue.fromParty),
      toParty: new FormControl(paymentRawValue.toParty),
      roleType: new FormControl(paymentRawValue.roleType),
      currencyUom: new FormControl(paymentRawValue.currencyUom),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return this.convertPaymentRawValueToPayment(form.getRawValue());
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({ ...this.getFormDefaults(), ...payment });
    form.reset({
      ...paymentRawValue,
      id: { value: paymentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      effectiveDate: currentTime,
      paymentDate: currentTime,
    };
  }

  private convertPaymentRawValueToPayment(rawPayment: PaymentFormRawValue | NewPaymentFormRawValue): IPayment | NewPayment {
    return {
      ...rawPayment,
      effectiveDate: dayjs(rawPayment.effectiveDate, DATE_TIME_FORMAT),
      paymentDate: dayjs(rawPayment.paymentDate, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentToPaymentRawValue(
    payment: IPayment | (Partial<NewPayment> & PaymentFormDefaults),
  ): PaymentFormRawValue | PartialWithRequiredKeyOf<NewPaymentFormRawValue> {
    return {
      ...payment,
      effectiveDate: payment.effectiveDate ? payment.effectiveDate.format(DATE_TIME_FORMAT) : undefined,
      paymentDate: payment.paymentDate ? payment.paymentDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
