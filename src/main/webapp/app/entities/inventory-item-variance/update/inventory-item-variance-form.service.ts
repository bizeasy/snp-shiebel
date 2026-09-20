import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInventoryItemVariance, NewInventoryItemVariance } from '../inventory-item-variance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryItemVariance for edit and NewInventoryItemVarianceFormGroupInput for create.
 */
type InventoryItemVarianceFormGroupInput = IInventoryItemVariance | PartialWithRequiredKeyOf<NewInventoryItemVariance>;

type InventoryItemVarianceFormDefaults = Pick<NewInventoryItemVariance, 'id'>;

type InventoryItemVarianceFormGroupContent = {
  id: FormControl<IInventoryItemVariance['id'] | NewInventoryItemVariance['id']>;
  varianceReason: FormControl<IInventoryItemVariance['varianceReason']>;
  atpVar: FormControl<IInventoryItemVariance['atpVar']>;
  qohVar: FormControl<IInventoryItemVariance['qohVar']>;
  comments: FormControl<IInventoryItemVariance['comments']>;
  inventoryItem: FormControl<IInventoryItemVariance['inventoryItem']>;
};

export type InventoryItemVarianceFormGroup = FormGroup<InventoryItemVarianceFormGroupContent>;

@Service()
export class InventoryItemVarianceFormService {
  createInventoryItemVarianceFormGroup(inventoryItemVariance?: InventoryItemVarianceFormGroupInput): InventoryItemVarianceFormGroup {
    const inventoryItemVarianceRawValue = {
      ...this.getFormDefaults(),
      ...(inventoryItemVariance ?? { id: null }),
    };

    return new FormGroup<InventoryItemVarianceFormGroupContent>({
      id: new FormControl(
        { value: inventoryItemVarianceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      varianceReason: new FormControl(inventoryItemVarianceRawValue.varianceReason, {
        validators: [Validators.maxLength(255)],
      }),
      atpVar: new FormControl(inventoryItemVarianceRawValue.atpVar),
      qohVar: new FormControl(inventoryItemVarianceRawValue.qohVar),
      comments: new FormControl(inventoryItemVarianceRawValue.comments, {
        validators: [Validators.maxLength(255)],
      }),
      inventoryItem: new FormControl(inventoryItemVarianceRawValue.inventoryItem, {
        validators: [Validators.required],
      }),
    });
  }

  getInventoryItemVariance(form: InventoryItemVarianceFormGroup): IInventoryItemVariance | NewInventoryItemVariance {
    return form.getRawValue();
  }

  resetForm(form: InventoryItemVarianceFormGroup, inventoryItemVariance: InventoryItemVarianceFormGroupInput): void {
    const inventoryItemVarianceRawValue = { ...this.getFormDefaults(), ...inventoryItemVariance };
    form.reset({
      ...inventoryItemVarianceRawValue,
      id: { value: inventoryItemVarianceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InventoryItemVarianceFormDefaults {
    return {
      id: null,
    };
  }
}
