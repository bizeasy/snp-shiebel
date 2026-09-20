import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IInventoryItemDelegate, NewInventoryItemDelegate } from '../inventory-item-delegate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryItemDelegate for edit and NewInventoryItemDelegateFormGroupInput for create.
 */
type InventoryItemDelegateFormGroupInput = IInventoryItemDelegate | PartialWithRequiredKeyOf<NewInventoryItemDelegate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInventoryItemDelegate | NewInventoryItemDelegate> = Omit<T, 'effectiveDate'> & {
  effectiveDate?: string | null;
};

type InventoryItemDelegateFormRawValue = FormValueOf<IInventoryItemDelegate>;

type NewInventoryItemDelegateFormRawValue = FormValueOf<NewInventoryItemDelegate>;

type InventoryItemDelegateFormDefaults = Pick<NewInventoryItemDelegate, 'id' | 'effectiveDate'>;

type InventoryItemDelegateFormGroupContent = {
  id: FormControl<InventoryItemDelegateFormRawValue['id'] | NewInventoryItemDelegate['id']>;
  sequenceNo: FormControl<InventoryItemDelegateFormRawValue['sequenceNo']>;
  effectiveDate: FormControl<InventoryItemDelegateFormRawValue['effectiveDate']>;
  quantityOnHandDiff: FormControl<InventoryItemDelegateFormRawValue['quantityOnHandDiff']>;
  availableToPromiseDiff: FormControl<InventoryItemDelegateFormRawValue['availableToPromiseDiff']>;
  accountingQuantityDiff: FormControl<InventoryItemDelegateFormRawValue['accountingQuantityDiff']>;
  unitCost: FormControl<InventoryItemDelegateFormRawValue['unitCost']>;
  remarks: FormControl<InventoryItemDelegateFormRawValue['remarks']>;
  inventoryItem: FormControl<InventoryItemDelegateFormRawValue['inventoryItem']>;
  order: FormControl<InventoryItemDelegateFormRawValue['order']>;
  orderItem: FormControl<InventoryItemDelegateFormRawValue['orderItem']>;
  invoice: FormControl<InventoryItemDelegateFormRawValue['invoice']>;
  invoiceItem: FormControl<InventoryItemDelegateFormRawValue['invoiceItem']>;
  shipment: FormControl<InventoryItemDelegateFormRawValue['shipment']>;
  shipmentItem: FormControl<InventoryItemDelegateFormRawValue['shipmentItem']>;
  itemIssuance: FormControl<InventoryItemDelegateFormRawValue['itemIssuance']>;
  receipt: FormControl<InventoryItemDelegateFormRawValue['receipt']>;
};

export type InventoryItemDelegateFormGroup = FormGroup<InventoryItemDelegateFormGroupContent>;

@Service()
export class InventoryItemDelegateFormService {
  createInventoryItemDelegateFormGroup(inventoryItemDelegate?: InventoryItemDelegateFormGroupInput): InventoryItemDelegateFormGroup {
    const inventoryItemDelegateRawValue = this.convertInventoryItemDelegateToInventoryItemDelegateRawValue({
      ...this.getFormDefaults(),
      ...(inventoryItemDelegate ?? { id: null }),
    });

    return new FormGroup<InventoryItemDelegateFormGroupContent>({
      id: new FormControl(
        { value: inventoryItemDelegateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(inventoryItemDelegateRawValue.sequenceNo),
      effectiveDate: new FormControl(inventoryItemDelegateRawValue.effectiveDate, {
        validators: [Validators.required],
      }),
      quantityOnHandDiff: new FormControl(inventoryItemDelegateRawValue.quantityOnHandDiff),
      availableToPromiseDiff: new FormControl(inventoryItemDelegateRawValue.availableToPromiseDiff),
      accountingQuantityDiff: new FormControl(inventoryItemDelegateRawValue.accountingQuantityDiff),
      unitCost: new FormControl(inventoryItemDelegateRawValue.unitCost),
      remarks: new FormControl(inventoryItemDelegateRawValue.remarks, {
        validators: [Validators.maxLength(255)],
      }),
      inventoryItem: new FormControl(inventoryItemDelegateRawValue.inventoryItem, {
        validators: [Validators.required],
      }),
      order: new FormControl(inventoryItemDelegateRawValue.order),
      orderItem: new FormControl(inventoryItemDelegateRawValue.orderItem),
      invoice: new FormControl(inventoryItemDelegateRawValue.invoice),
      invoiceItem: new FormControl(inventoryItemDelegateRawValue.invoiceItem),
      shipment: new FormControl(inventoryItemDelegateRawValue.shipment),
      shipmentItem: new FormControl(inventoryItemDelegateRawValue.shipmentItem),
      itemIssuance: new FormControl(inventoryItemDelegateRawValue.itemIssuance),
      receipt: new FormControl(inventoryItemDelegateRawValue.receipt),
    });
  }

  getInventoryItemDelegate(form: InventoryItemDelegateFormGroup): IInventoryItemDelegate | NewInventoryItemDelegate {
    return this.convertInventoryItemDelegateRawValueToInventoryItemDelegate(form.getRawValue());
  }

  resetForm(form: InventoryItemDelegateFormGroup, inventoryItemDelegate: InventoryItemDelegateFormGroupInput): void {
    const inventoryItemDelegateRawValue = this.convertInventoryItemDelegateToInventoryItemDelegateRawValue({
      ...this.getFormDefaults(),
      ...inventoryItemDelegate,
    });
    form.reset({
      ...inventoryItemDelegateRawValue,
      id: { value: inventoryItemDelegateRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InventoryItemDelegateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      effectiveDate: currentTime,
    };
  }

  private convertInventoryItemDelegateRawValueToInventoryItemDelegate(
    rawInventoryItemDelegate: InventoryItemDelegateFormRawValue | NewInventoryItemDelegateFormRawValue,
  ): IInventoryItemDelegate | NewInventoryItemDelegate {
    return {
      ...rawInventoryItemDelegate,
      effectiveDate: dayjs(rawInventoryItemDelegate.effectiveDate, DATE_TIME_FORMAT),
    };
  }

  private convertInventoryItemDelegateToInventoryItemDelegateRawValue(
    inventoryItemDelegate: IInventoryItemDelegate | (Partial<NewInventoryItemDelegate> & InventoryItemDelegateFormDefaults),
  ): InventoryItemDelegateFormRawValue | PartialWithRequiredKeyOf<NewInventoryItemDelegateFormRawValue> {
    return {
      ...inventoryItemDelegate,
      effectiveDate: inventoryItemDelegate.effectiveDate ? inventoryItemDelegate.effectiveDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
