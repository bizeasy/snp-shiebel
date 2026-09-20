import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInventoryItemType, NewInventoryItemType } from '../inventory-item-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryItemType for edit and NewInventoryItemTypeFormGroupInput for create.
 */
type InventoryItemTypeFormGroupInput = IInventoryItemType | PartialWithRequiredKeyOf<NewInventoryItemType>;

type InventoryItemTypeFormDefaults = Pick<NewInventoryItemType, 'id'>;

type InventoryItemTypeFormGroupContent = {
  id: FormControl<IInventoryItemType['id'] | NewInventoryItemType['id']>;
  name: FormControl<IInventoryItemType['name']>;
  description: FormControl<IInventoryItemType['description']>;
};

export type InventoryItemTypeFormGroup = FormGroup<InventoryItemTypeFormGroupContent>;

@Service()
export class InventoryItemTypeFormService {
  createInventoryItemTypeFormGroup(inventoryItemType?: InventoryItemTypeFormGroupInput): InventoryItemTypeFormGroup {
    const inventoryItemTypeRawValue = {
      ...this.getFormDefaults(),
      ...(inventoryItemType ?? { id: null }),
    };

    return new FormGroup<InventoryItemTypeFormGroupContent>({
      id: new FormControl(
        { value: inventoryItemTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(inventoryItemTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(inventoryItemTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getInventoryItemType(form: InventoryItemTypeFormGroup): IInventoryItemType | NewInventoryItemType {
    return form.getRawValue();
  }

  resetForm(form: InventoryItemTypeFormGroup, inventoryItemType: InventoryItemTypeFormGroupInput): void {
    const inventoryItemTypeRawValue = { ...this.getFormDefaults(), ...inventoryItemType };
    form.reset({
      ...inventoryItemTypeRawValue,
      id: { value: inventoryItemTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InventoryItemTypeFormDefaults {
    return {
      id: null,
    };
  }
}
