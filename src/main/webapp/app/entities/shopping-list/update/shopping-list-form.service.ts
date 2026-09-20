import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShoppingList, NewShoppingList } from '../shopping-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShoppingList for edit and NewShoppingListFormGroupInput for create.
 */
type ShoppingListFormGroupInput = IShoppingList | PartialWithRequiredKeyOf<NewShoppingList>;

type ShoppingListFormDefaults = Pick<NewShoppingList, 'id' | 'isActive'>;

type ShoppingListFormGroupContent = {
  id: FormControl<IShoppingList['id'] | NewShoppingList['id']>;
  name: FormControl<IShoppingList['name']>;
  isActive: FormControl<IShoppingList['isActive']>;
  shoppingListType: FormControl<IShoppingList['shoppingListType']>;
  party: FormControl<IShoppingList['party']>;
  status: FormControl<IShoppingList['status']>;
};

export type ShoppingListFormGroup = FormGroup<ShoppingListFormGroupContent>;

@Service()
export class ShoppingListFormService {
  createShoppingListFormGroup(shoppingList?: ShoppingListFormGroupInput): ShoppingListFormGroup {
    const shoppingListRawValue = {
      ...this.getFormDefaults(),
      ...(shoppingList ?? { id: null }),
    };

    return new FormGroup<ShoppingListFormGroupContent>({
      id: new FormControl(
        { value: shoppingListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(shoppingListRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      isActive: new FormControl(shoppingListRawValue.isActive),
      shoppingListType: new FormControl(shoppingListRawValue.shoppingListType),
      party: new FormControl(shoppingListRawValue.party, {
        validators: [Validators.required],
      }),
      status: new FormControl(shoppingListRawValue.status),
    });
  }

  getShoppingList(form: ShoppingListFormGroup): IShoppingList | NewShoppingList {
    return form.getRawValue();
  }

  resetForm(form: ShoppingListFormGroup, shoppingList: ShoppingListFormGroupInput): void {
    const shoppingListRawValue = { ...this.getFormDefaults(), ...shoppingList };
    form.reset({
      ...shoppingListRawValue,
      id: { value: shoppingListRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShoppingListFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
