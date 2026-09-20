import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderType, NewOrderType } from '../order-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderType for edit and NewOrderTypeFormGroupInput for create.
 */
type OrderTypeFormGroupInput = IOrderType | PartialWithRequiredKeyOf<NewOrderType>;

type OrderTypeFormDefaults = Pick<NewOrderType, 'id'>;

type OrderTypeFormGroupContent = {
  id: FormControl<IOrderType['id'] | NewOrderType['id']>;
  name: FormControl<IOrderType['name']>;
  description: FormControl<IOrderType['description']>;
};

export type OrderTypeFormGroup = FormGroup<OrderTypeFormGroupContent>;

@Service()
export class OrderTypeFormService {
  createOrderTypeFormGroup(orderType?: OrderTypeFormGroupInput): OrderTypeFormGroup {
    const orderTypeRawValue = {
      ...this.getFormDefaults(),
      ...(orderType ?? { id: null }),
    };

    return new FormGroup<OrderTypeFormGroupContent>({
      id: new FormControl(
        { value: orderTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(orderTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(orderTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getOrderType(form: OrderTypeFormGroup): IOrderType | NewOrderType {
    return form.getRawValue();
  }

  resetForm(form: OrderTypeFormGroup, orderType: OrderTypeFormGroupInput): void {
    const orderTypeRawValue = { ...this.getFormDefaults(), ...orderType };
    form.reset({
      ...orderTypeRawValue,
      id: { value: orderTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderTypeFormDefaults {
    return {
      id: null,
    };
  }
}
