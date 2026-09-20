import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderItemType, NewOrderItemType } from '../order-item-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderItemType for edit and NewOrderItemTypeFormGroupInput for create.
 */
type OrderItemTypeFormGroupInput = IOrderItemType | PartialWithRequiredKeyOf<NewOrderItemType>;

type OrderItemTypeFormDefaults = Pick<NewOrderItemType, 'id'>;

type OrderItemTypeFormGroupContent = {
  id: FormControl<IOrderItemType['id'] | NewOrderItemType['id']>;
  name: FormControl<IOrderItemType['name']>;
  description: FormControl<IOrderItemType['description']>;
};

export type OrderItemTypeFormGroup = FormGroup<OrderItemTypeFormGroupContent>;

@Service()
export class OrderItemTypeFormService {
  createOrderItemTypeFormGroup(orderItemType?: OrderItemTypeFormGroupInput): OrderItemTypeFormGroup {
    const orderItemTypeRawValue = {
      ...this.getFormDefaults(),
      ...(orderItemType ?? { id: null }),
    };

    return new FormGroup<OrderItemTypeFormGroupContent>({
      id: new FormControl(
        { value: orderItemTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(orderItemTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(orderItemTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getOrderItemType(form: OrderItemTypeFormGroup): IOrderItemType | NewOrderItemType {
    return form.getRawValue();
  }

  resetForm(form: OrderItemTypeFormGroup, orderItemType: OrderItemTypeFormGroupInput): void {
    const orderItemTypeRawValue = { ...this.getFormDefaults(), ...orderItemType };
    form.reset({
      ...orderItemTypeRawValue,
      id: { value: orderItemTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderItemTypeFormDefaults {
    return {
      id: null,
    };
  }
}
