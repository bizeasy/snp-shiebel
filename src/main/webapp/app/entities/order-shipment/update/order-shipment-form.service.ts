import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderShipment, NewOrderShipment } from '../order-shipment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderShipment for edit and NewOrderShipmentFormGroupInput for create.
 */
type OrderShipmentFormGroupInput = IOrderShipment | PartialWithRequiredKeyOf<NewOrderShipment>;

type OrderShipmentFormDefaults = Pick<NewOrderShipment, 'id'>;

type OrderShipmentFormGroupContent = {
  id: FormControl<IOrderShipment['id'] | NewOrderShipment['id']>;
  quantity: FormControl<IOrderShipment['quantity']>;
  order: FormControl<IOrderShipment['order']>;
  orderItem: FormControl<IOrderShipment['orderItem']>;
  shipment: FormControl<IOrderShipment['shipment']>;
  shipmentItem: FormControl<IOrderShipment['shipmentItem']>;
};

export type OrderShipmentFormGroup = FormGroup<OrderShipmentFormGroupContent>;

@Service()
export class OrderShipmentFormService {
  createOrderShipmentFormGroup(orderShipment?: OrderShipmentFormGroupInput): OrderShipmentFormGroup {
    const orderShipmentRawValue = {
      ...this.getFormDefaults(),
      ...(orderShipment ?? { id: null }),
    };

    return new FormGroup<OrderShipmentFormGroupContent>({
      id: new FormControl(
        { value: orderShipmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(orderShipmentRawValue.quantity, {
        validators: [Validators.required],
      }),
      order: new FormControl(orderShipmentRawValue.order, {
        validators: [Validators.required],
      }),
      orderItem: new FormControl(orderShipmentRawValue.orderItem, {
        validators: [Validators.required],
      }),
      shipment: new FormControl(orderShipmentRawValue.shipment, {
        validators: [Validators.required],
      }),
      shipmentItem: new FormControl(orderShipmentRawValue.shipmentItem),
    });
  }

  getOrderShipment(form: OrderShipmentFormGroup): IOrderShipment | NewOrderShipment {
    return form.getRawValue();
  }

  resetForm(form: OrderShipmentFormGroup, orderShipment: OrderShipmentFormGroupInput): void {
    const orderShipmentRawValue = { ...this.getFormDefaults(), ...orderShipment };
    form.reset({
      ...orderShipmentRawValue,
      id: { value: orderShipmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderShipmentFormDefaults {
    return {
      id: null,
    };
  }
}
