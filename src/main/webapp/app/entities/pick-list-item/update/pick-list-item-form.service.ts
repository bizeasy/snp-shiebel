import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPickListItem, NewPickListItem } from '../pick-list-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPickListItem for edit and NewPickListItemFormGroupInput for create.
 */
type PickListItemFormGroupInput = IPickListItem | PartialWithRequiredKeyOf<NewPickListItem>;

type PickListItemFormDefaults = Pick<NewPickListItem, 'id'>;

type PickListItemFormGroupContent = {
  id: FormControl<IPickListItem['id'] | NewPickListItem['id']>;
  quantity: FormControl<IPickListItem['quantity']>;
  pickList: FormControl<IPickListItem['pickList']>;
  order: FormControl<IPickListItem['order']>;
  orderItem: FormControl<IPickListItem['orderItem']>;
  inventoryItem: FormControl<IPickListItem['inventoryItem']>;
  product: FormControl<IPickListItem['product']>;
  facilityLocation: FormControl<IPickListItem['facilityLocation']>;
  status: FormControl<IPickListItem['status']>;
};

export type PickListItemFormGroup = FormGroup<PickListItemFormGroupContent>;

@Service()
export class PickListItemFormService {
  createPickListItemFormGroup(pickListItem?: PickListItemFormGroupInput): PickListItemFormGroup {
    const pickListItemRawValue = {
      ...this.getFormDefaults(),
      ...(pickListItem ?? { id: null }),
    };

    return new FormGroup<PickListItemFormGroupContent>({
      id: new FormControl(
        { value: pickListItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(pickListItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      pickList: new FormControl(pickListItemRawValue.pickList, {
        validators: [Validators.required],
      }),
      order: new FormControl(pickListItemRawValue.order),
      orderItem: new FormControl(pickListItemRawValue.orderItem),
      inventoryItem: new FormControl(pickListItemRawValue.inventoryItem),
      product: new FormControl(pickListItemRawValue.product),
      facilityLocation: new FormControl(pickListItemRawValue.facilityLocation),
      status: new FormControl(pickListItemRawValue.status),
    });
  }

  getPickListItem(form: PickListItemFormGroup): IPickListItem | NewPickListItem {
    return form.getRawValue();
  }

  resetForm(form: PickListItemFormGroup, pickListItem: PickListItemFormGroupInput): void {
    const pickListItemRawValue = { ...this.getFormDefaults(), ...pickListItem };
    form.reset({
      ...pickListItemRawValue,
      id: { value: pickListItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PickListItemFormDefaults {
    return {
      id: null,
    };
  }
}
