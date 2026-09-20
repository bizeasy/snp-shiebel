import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderRole, NewOrderRole } from '../order-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderRole for edit and NewOrderRoleFormGroupInput for create.
 */
type OrderRoleFormGroupInput = IOrderRole | PartialWithRequiredKeyOf<NewOrderRole>;

type OrderRoleFormDefaults = Pick<NewOrderRole, 'id'>;

type OrderRoleFormGroupContent = {
  id: FormControl<IOrderRole['id'] | NewOrderRole['id']>;
  order: FormControl<IOrderRole['order']>;
  orderItem: FormControl<IOrderRole['orderItem']>;
  party: FormControl<IOrderRole['party']>;
  roleType: FormControl<IOrderRole['roleType']>;
};

export type OrderRoleFormGroup = FormGroup<OrderRoleFormGroupContent>;

@Service()
export class OrderRoleFormService {
  createOrderRoleFormGroup(orderRole?: OrderRoleFormGroupInput): OrderRoleFormGroup {
    const orderRoleRawValue = {
      ...this.getFormDefaults(),
      ...(orderRole ?? { id: null }),
    };

    return new FormGroup<OrderRoleFormGroupContent>({
      id: new FormControl(
        { value: orderRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      order: new FormControl(orderRoleRawValue.order),
      orderItem: new FormControl(orderRoleRawValue.orderItem),
      party: new FormControl(orderRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(orderRoleRawValue.roleType),
    });
  }

  getOrderRole(form: OrderRoleFormGroup): IOrderRole | NewOrderRole {
    return form.getRawValue();
  }

  resetForm(form: OrderRoleFormGroup, orderRole: OrderRoleFormGroupInput): void {
    const orderRoleRawValue = { ...this.getFormDefaults(), ...orderRole };
    form.reset({
      ...orderRoleRawValue,
      id: { value: orderRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderRoleFormDefaults {
    return {
      id: null,
    };
  }
}
