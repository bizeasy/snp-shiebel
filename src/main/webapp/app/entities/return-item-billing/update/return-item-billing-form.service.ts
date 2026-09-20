import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReturnItemBilling, NewReturnItemBilling } from '../return-item-billing.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReturnItemBilling for edit and NewReturnItemBillingFormGroupInput for create.
 */
type ReturnItemBillingFormGroupInput = IReturnItemBilling | PartialWithRequiredKeyOf<NewReturnItemBilling>;

type ReturnItemBillingFormDefaults = Pick<NewReturnItemBilling, 'id'>;

type ReturnItemBillingFormGroupContent = {
  id: FormControl<IReturnItemBilling['id'] | NewReturnItemBilling['id']>;
  quantity: FormControl<IReturnItemBilling['quantity']>;
  amount: FormControl<IReturnItemBilling['amount']>;
  returnItem: FormControl<IReturnItemBilling['returnItem']>;
  orderItem: FormControl<IReturnItemBilling['orderItem']>;
  invoiceItem: FormControl<IReturnItemBilling['invoiceItem']>;
  shipmentReceipt: FormControl<IReturnItemBilling['shipmentReceipt']>;
};

export type ReturnItemBillingFormGroup = FormGroup<ReturnItemBillingFormGroupContent>;

@Service()
export class ReturnItemBillingFormService {
  createReturnItemBillingFormGroup(returnItemBilling?: ReturnItemBillingFormGroupInput): ReturnItemBillingFormGroup {
    const returnItemBillingRawValue = {
      ...this.getFormDefaults(),
      ...(returnItemBilling ?? { id: null }),
    };

    return new FormGroup<ReturnItemBillingFormGroupContent>({
      id: new FormControl(
        { value: returnItemBillingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(returnItemBillingRawValue.quantity, {
        validators: [Validators.required],
      }),
      amount: new FormControl(returnItemBillingRawValue.amount),
      returnItem: new FormControl(returnItemBillingRawValue.returnItem, {
        validators: [Validators.required],
      }),
      orderItem: new FormControl(returnItemBillingRawValue.orderItem),
      invoiceItem: new FormControl(returnItemBillingRawValue.invoiceItem),
      shipmentReceipt: new FormControl(returnItemBillingRawValue.shipmentReceipt),
    });
  }

  getReturnItemBilling(form: ReturnItemBillingFormGroup): IReturnItemBilling | NewReturnItemBilling {
    return form.getRawValue();
  }

  resetForm(form: ReturnItemBillingFormGroup, returnItemBilling: ReturnItemBillingFormGroupInput): void {
    const returnItemBillingRawValue = { ...this.getFormDefaults(), ...returnItemBilling };
    form.reset({
      ...returnItemBillingRawValue,
      id: { value: returnItemBillingRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ReturnItemBillingFormDefaults {
    return {
      id: null,
    };
  }
}
