import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IItemIssuance, NewItemIssuance } from '../item-issuance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItemIssuance for edit and NewItemIssuanceFormGroupInput for create.
 */
type ItemIssuanceFormGroupInput = IItemIssuance | PartialWithRequiredKeyOf<NewItemIssuance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IItemIssuance | NewItemIssuance> = Omit<T, 'issuedDate'> & {
  issuedDate?: string | null;
};

type ItemIssuanceFormRawValue = FormValueOf<IItemIssuance>;

type NewItemIssuanceFormRawValue = FormValueOf<NewItemIssuance>;

type ItemIssuanceFormDefaults = Pick<NewItemIssuance, 'id' | 'issuedDate'>;

type ItemIssuanceFormGroupContent = {
  id: FormControl<ItemIssuanceFormRawValue['id'] | NewItemIssuance['id']>;
  message: FormControl<ItemIssuanceFormRawValue['message']>;
  issuedDate: FormControl<ItemIssuanceFormRawValue['issuedDate']>;
  issuedBy: FormControl<ItemIssuanceFormRawValue['issuedBy']>;
  quantity: FormControl<ItemIssuanceFormRawValue['quantity']>;
  cancelQuantity: FormControl<ItemIssuanceFormRawValue['cancelQuantity']>;
  inventoryItem: FormControl<ItemIssuanceFormRawValue['inventoryItem']>;
  order: FormControl<ItemIssuanceFormRawValue['order']>;
  orderItem: FormControl<ItemIssuanceFormRawValue['orderItem']>;
  shipment: FormControl<ItemIssuanceFormRawValue['shipment']>;
  shipmentItem: FormControl<ItemIssuanceFormRawValue['shipmentItem']>;
  issuedByParty: FormControl<ItemIssuanceFormRawValue['issuedByParty']>;
};

export type ItemIssuanceFormGroup = FormGroup<ItemIssuanceFormGroupContent>;

@Service()
export class ItemIssuanceFormService {
  createItemIssuanceFormGroup(itemIssuance?: ItemIssuanceFormGroupInput): ItemIssuanceFormGroup {
    const itemIssuanceRawValue = this.convertItemIssuanceToItemIssuanceRawValue({
      ...this.getFormDefaults(),
      ...(itemIssuance ?? { id: null }),
    });

    return new FormGroup<ItemIssuanceFormGroupContent>({
      id: new FormControl(
        { value: itemIssuanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      message: new FormControl(itemIssuanceRawValue.message, {
        validators: [Validators.maxLength(255)],
      }),
      issuedDate: new FormControl(itemIssuanceRawValue.issuedDate),
      issuedBy: new FormControl(itemIssuanceRawValue.issuedBy, {
        validators: [Validators.maxLength(60)],
      }),
      quantity: new FormControl(itemIssuanceRawValue.quantity, {
        validators: [Validators.required],
      }),
      cancelQuantity: new FormControl(itemIssuanceRawValue.cancelQuantity),
      inventoryItem: new FormControl(itemIssuanceRawValue.inventoryItem, {
        validators: [Validators.required],
      }),
      order: new FormControl(itemIssuanceRawValue.order),
      orderItem: new FormControl(itemIssuanceRawValue.orderItem),
      shipment: new FormControl(itemIssuanceRawValue.shipment),
      shipmentItem: new FormControl(itemIssuanceRawValue.shipmentItem),
      issuedByParty: new FormControl(itemIssuanceRawValue.issuedByParty),
    });
  }

  getItemIssuance(form: ItemIssuanceFormGroup): IItemIssuance | NewItemIssuance {
    return this.convertItemIssuanceRawValueToItemIssuance(form.getRawValue());
  }

  resetForm(form: ItemIssuanceFormGroup, itemIssuance: ItemIssuanceFormGroupInput): void {
    const itemIssuanceRawValue = this.convertItemIssuanceToItemIssuanceRawValue({ ...this.getFormDefaults(), ...itemIssuance });
    form.reset({
      ...itemIssuanceRawValue,
      id: { value: itemIssuanceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ItemIssuanceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issuedDate: currentTime,
    };
  }

  private convertItemIssuanceRawValueToItemIssuance(
    rawItemIssuance: ItemIssuanceFormRawValue | NewItemIssuanceFormRawValue,
  ): IItemIssuance | NewItemIssuance {
    return {
      ...rawItemIssuance,
      issuedDate: dayjs(rawItemIssuance.issuedDate, DATE_TIME_FORMAT),
    };
  }

  private convertItemIssuanceToItemIssuanceRawValue(
    itemIssuance: IItemIssuance | (Partial<NewItemIssuance> & ItemIssuanceFormDefaults),
  ): ItemIssuanceFormRawValue | PartialWithRequiredKeyOf<NewItemIssuanceFormRawValue> {
    return {
      ...itemIssuance,
      issuedDate: itemIssuance.issuedDate ? itemIssuance.issuedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
