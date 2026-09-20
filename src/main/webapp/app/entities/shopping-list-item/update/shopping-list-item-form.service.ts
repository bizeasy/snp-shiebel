import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IShoppingListItem, NewShoppingListItem } from '../shopping-list-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShoppingListItem for edit and NewShoppingListItemFormGroupInput for create.
 */
type ShoppingListItemFormGroupInput = IShoppingListItem | PartialWithRequiredKeyOf<NewShoppingListItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IShoppingListItem | NewShoppingListItem> = Omit<T, 'requiredByDate'> & {
  requiredByDate?: string | null;
};

type ShoppingListItemFormRawValue = FormValueOf<IShoppingListItem>;

type NewShoppingListItemFormRawValue = FormValueOf<NewShoppingListItem>;

type ShoppingListItemFormDefaults = Pick<NewShoppingListItem, 'id' | 'requiredByDate'>;

type ShoppingListItemFormGroupContent = {
  id: FormControl<ShoppingListItemFormRawValue['id'] | NewShoppingListItem['id']>;
  sequenceNo: FormControl<ShoppingListItemFormRawValue['sequenceNo']>;
  quantity: FormControl<ShoppingListItemFormRawValue['quantity']>;
  requiredByDate: FormControl<ShoppingListItemFormRawValue['requiredByDate']>;
  selectedAmount: FormControl<ShoppingListItemFormRawValue['selectedAmount']>;
  maxAmount: FormControl<ShoppingListItemFormRawValue['maxAmount']>;
  description: FormControl<ShoppingListItemFormRawValue['description']>;
  story: FormControl<ShoppingListItemFormRawValue['story']>;
  shoppingList: FormControl<ShoppingListItemFormRawValue['shoppingList']>;
  product: FormControl<ShoppingListItemFormRawValue['product']>;
  status: FormControl<ShoppingListItemFormRawValue['status']>;
};

export type ShoppingListItemFormGroup = FormGroup<ShoppingListItemFormGroupContent>;

@Service()
export class ShoppingListItemFormService {
  createShoppingListItemFormGroup(shoppingListItem?: ShoppingListItemFormGroupInput): ShoppingListItemFormGroup {
    const shoppingListItemRawValue = this.convertShoppingListItemToShoppingListItemRawValue({
      ...this.getFormDefaults(),
      ...(shoppingListItem ?? { id: null }),
    });

    return new FormGroup<ShoppingListItemFormGroupContent>({
      id: new FormControl(
        { value: shoppingListItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(shoppingListItemRawValue.sequenceNo),
      quantity: new FormControl(shoppingListItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      requiredByDate: new FormControl(shoppingListItemRawValue.requiredByDate),
      selectedAmount: new FormControl(shoppingListItemRawValue.selectedAmount),
      maxAmount: new FormControl(shoppingListItemRawValue.maxAmount),
      description: new FormControl(shoppingListItemRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      story: new FormControl(shoppingListItemRawValue.story, {
        validators: [Validators.maxLength(2000)],
      }),
      shoppingList: new FormControl(shoppingListItemRawValue.shoppingList, {
        validators: [Validators.required],
      }),
      product: new FormControl(shoppingListItemRawValue.product),
      status: new FormControl(shoppingListItemRawValue.status),
    });
  }

  getShoppingListItem(form: ShoppingListItemFormGroup): IShoppingListItem | NewShoppingListItem {
    return this.convertShoppingListItemRawValueToShoppingListItem(form.getRawValue());
  }

  resetForm(form: ShoppingListItemFormGroup, shoppingListItem: ShoppingListItemFormGroupInput): void {
    const shoppingListItemRawValue = this.convertShoppingListItemToShoppingListItemRawValue({
      ...this.getFormDefaults(),
      ...shoppingListItem,
    });
    form.reset({
      ...shoppingListItemRawValue,
      id: { value: shoppingListItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShoppingListItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requiredByDate: currentTime,
    };
  }

  private convertShoppingListItemRawValueToShoppingListItem(
    rawShoppingListItem: ShoppingListItemFormRawValue | NewShoppingListItemFormRawValue,
  ): IShoppingListItem | NewShoppingListItem {
    return {
      ...rawShoppingListItem,
      requiredByDate: dayjs(rawShoppingListItem.requiredByDate, DATE_TIME_FORMAT),
    };
  }

  private convertShoppingListItemToShoppingListItemRawValue(
    shoppingListItem: IShoppingListItem | (Partial<NewShoppingListItem> & ShoppingListItemFormDefaults),
  ): ShoppingListItemFormRawValue | PartialWithRequiredKeyOf<NewShoppingListItemFormRawValue> {
    return {
      ...shoppingListItem,
      requiredByDate: shoppingListItem.requiredByDate ? shoppingListItem.requiredByDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
