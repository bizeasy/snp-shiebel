import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderAdjustmentType, NewOrderAdjustmentType } from '../order-adjustment-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderAdjustmentType for edit and NewOrderAdjustmentTypeFormGroupInput for create.
 */
type OrderAdjustmentTypeFormGroupInput = IOrderAdjustmentType | PartialWithRequiredKeyOf<NewOrderAdjustmentType>;

type OrderAdjustmentTypeFormDefaults = Pick<NewOrderAdjustmentType, 'id'>;

type OrderAdjustmentTypeFormGroupContent = {
  id: FormControl<IOrderAdjustmentType['id'] | NewOrderAdjustmentType['id']>;
  name: FormControl<IOrderAdjustmentType['name']>;
  description: FormControl<IOrderAdjustmentType['description']>;
  parent: FormControl<IOrderAdjustmentType['parent']>;
};

export type OrderAdjustmentTypeFormGroup = FormGroup<OrderAdjustmentTypeFormGroupContent>;

@Service()
export class OrderAdjustmentTypeFormService {
  createOrderAdjustmentTypeFormGroup(orderAdjustmentType?: OrderAdjustmentTypeFormGroupInput): OrderAdjustmentTypeFormGroup {
    const orderAdjustmentTypeRawValue = {
      ...this.getFormDefaults(),
      ...(orderAdjustmentType ?? { id: null }),
    };

    return new FormGroup<OrderAdjustmentTypeFormGroupContent>({
      id: new FormControl(
        { value: orderAdjustmentTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(orderAdjustmentTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      description: new FormControl(orderAdjustmentTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
      parent: new FormControl(orderAdjustmentTypeRawValue.parent),
    });
  }

  getOrderAdjustmentType(form: OrderAdjustmentTypeFormGroup): IOrderAdjustmentType | NewOrderAdjustmentType {
    return form.getRawValue();
  }

  resetForm(form: OrderAdjustmentTypeFormGroup, orderAdjustmentType: OrderAdjustmentTypeFormGroupInput): void {
    const orderAdjustmentTypeRawValue = { ...this.getFormDefaults(), ...orderAdjustmentType };
    form.reset({
      ...orderAdjustmentTypeRawValue,
      id: { value: orderAdjustmentTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderAdjustmentTypeFormDefaults {
    return {
      id: null,
    };
  }
}
