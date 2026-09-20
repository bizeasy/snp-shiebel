import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderItemBilling, NewOrderItemBilling } from '../order-item-billing.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderItemBilling for edit and NewOrderItemBillingFormGroupInput for create.
 */
type OrderItemBillingFormGroupInput = IOrderItemBilling | PartialWithRequiredKeyOf<NewOrderItemBilling>;

type OrderItemBillingFormDefaults = Pick<NewOrderItemBilling, 'id'>;

type OrderItemBillingFormGroupContent = {
  id: FormControl<IOrderItemBilling['id'] | NewOrderItemBilling['id']>;
  quantity: FormControl<IOrderItemBilling['quantity']>;
  amount: FormControl<IOrderItemBilling['amount']>;
  orderItem: FormControl<IOrderItemBilling['orderItem']>;
  invoiceItem: FormControl<IOrderItemBilling['invoiceItem']>;
  shipmentReceipt: FormControl<IOrderItemBilling['shipmentReceipt']>;
  itemIssuance: FormControl<IOrderItemBilling['itemIssuance']>;
};

export type OrderItemBillingFormGroup = FormGroup<OrderItemBillingFormGroupContent>;

@Service()
export class OrderItemBillingFormService {
  createOrderItemBillingFormGroup(orderItemBilling?: OrderItemBillingFormGroupInput): OrderItemBillingFormGroup {
    const orderItemBillingRawValue = {
      ...this.getFormDefaults(),
      ...(orderItemBilling ?? { id: null }),
    };

    return new FormGroup<OrderItemBillingFormGroupContent>({
      id: new FormControl(
        { value: orderItemBillingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(orderItemBillingRawValue.quantity, {
        validators: [Validators.required],
      }),
      amount: new FormControl(orderItemBillingRawValue.amount),
      orderItem: new FormControl(orderItemBillingRawValue.orderItem, {
        validators: [Validators.required],
      }),
      invoiceItem: new FormControl(orderItemBillingRawValue.invoiceItem, {
        validators: [Validators.required],
      }),
      shipmentReceipt: new FormControl(orderItemBillingRawValue.shipmentReceipt),
      itemIssuance: new FormControl(orderItemBillingRawValue.itemIssuance),
    });
  }

  getOrderItemBilling(form: OrderItemBillingFormGroup): IOrderItemBilling | NewOrderItemBilling {
    return form.getRawValue();
  }

  resetForm(form: OrderItemBillingFormGroup, orderItemBilling: OrderItemBillingFormGroupInput): void {
    const orderItemBillingRawValue = { ...this.getFormDefaults(), ...orderItemBilling };
    form.reset({
      ...orderItemBillingRawValue,
      id: { value: orderItemBillingRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderItemBillingFormDefaults {
    return {
      id: null,
    };
  }
}
