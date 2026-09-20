import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShoppingListType, NewShoppingListType } from '../shopping-list-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShoppingListType for edit and NewShoppingListTypeFormGroupInput for create.
 */
type ShoppingListTypeFormGroupInput = IShoppingListType | PartialWithRequiredKeyOf<NewShoppingListType>;

type ShoppingListTypeFormDefaults = Pick<NewShoppingListType, 'id'>;

type ShoppingListTypeFormGroupContent = {
  id: FormControl<IShoppingListType['id'] | NewShoppingListType['id']>;
  name: FormControl<IShoppingListType['name']>;
  description: FormControl<IShoppingListType['description']>;
};

export type ShoppingListTypeFormGroup = FormGroup<ShoppingListTypeFormGroupContent>;

@Service()
export class ShoppingListTypeFormService {
  createShoppingListTypeFormGroup(shoppingListType?: ShoppingListTypeFormGroupInput): ShoppingListTypeFormGroup {
    const shoppingListTypeRawValue = {
      ...this.getFormDefaults(),
      ...(shoppingListType ?? { id: null }),
    };

    return new FormGroup<ShoppingListTypeFormGroupContent>({
      id: new FormControl(
        { value: shoppingListTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(shoppingListTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(shoppingListTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getShoppingListType(form: ShoppingListTypeFormGroup): IShoppingListType | NewShoppingListType {
    return form.getRawValue();
  }

  resetForm(form: ShoppingListTypeFormGroup, shoppingListType: ShoppingListTypeFormGroupInput): void {
    const shoppingListTypeRawValue = { ...this.getFormDefaults(), ...shoppingListType };
    form.reset({
      ...shoppingListTypeRawValue,
      id: { value: shoppingListTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShoppingListTypeFormDefaults {
    return {
      id: null,
    };
  }
}
