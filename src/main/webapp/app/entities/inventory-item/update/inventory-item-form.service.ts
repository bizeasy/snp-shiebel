import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IInventoryItem, NewInventoryItem } from '../inventory-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryItem for edit and NewInventoryItemFormGroupInput for create.
 */
type InventoryItemFormGroupInput = IInventoryItem | PartialWithRequiredKeyOf<NewInventoryItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInventoryItem | NewInventoryItem> = Omit<
  T,
  'receivedDate' | 'manufacturedDate' | 'expiryDate' | 'retestDate' | 'activationValidThru'
> & {
  receivedDate?: string | null;
  manufacturedDate?: string | null;
  expiryDate?: string | null;
  retestDate?: string | null;
  activationValidThru?: string | null;
};

type InventoryItemFormRawValue = FormValueOf<IInventoryItem>;

type NewInventoryItemFormRawValue = FormValueOf<NewInventoryItem>;

type InventoryItemFormDefaults = Pick<
  NewInventoryItem,
  'id' | 'receivedDate' | 'manufacturedDate' | 'expiryDate' | 'retestDate' | 'activationValidThru'
>;

type InventoryItemFormGroupContent = {
  id: FormControl<InventoryItemFormRawValue['id'] | NewInventoryItem['id']>;
  receivedDate: FormControl<InventoryItemFormRawValue['receivedDate']>;
  manufacturedDate: FormControl<InventoryItemFormRawValue['manufacturedDate']>;
  expiryDate: FormControl<InventoryItemFormRawValue['expiryDate']>;
  retestDate: FormControl<InventoryItemFormRawValue['retestDate']>;
  containerId: FormControl<InventoryItemFormRawValue['containerId']>;
  batchNo: FormControl<InventoryItemFormRawValue['batchNo']>;
  mfgBatchNo: FormControl<InventoryItemFormRawValue['mfgBatchNo']>;
  lotNoStr: FormControl<InventoryItemFormRawValue['lotNoStr']>;
  binNumber: FormControl<InventoryItemFormRawValue['binNumber']>;
  comments: FormControl<InventoryItemFormRawValue['comments']>;
  quantityOnHandTotal: FormControl<InventoryItemFormRawValue['quantityOnHandTotal']>;
  availableToPromiseTotal: FormControl<InventoryItemFormRawValue['availableToPromiseTotal']>;
  accountingQuantityTotal: FormControl<InventoryItemFormRawValue['accountingQuantityTotal']>;
  oldQuantityOnHand: FormControl<InventoryItemFormRawValue['oldQuantityOnHand']>;
  oldAvailableToPromise: FormControl<InventoryItemFormRawValue['oldAvailableToPromise']>;
  serialNumber: FormControl<InventoryItemFormRawValue['serialNumber']>;
  softIdentifier: FormControl<InventoryItemFormRawValue['softIdentifier']>;
  activationNumber: FormControl<InventoryItemFormRawValue['activationNumber']>;
  activationValidThru: FormControl<InventoryItemFormRawValue['activationValidThru']>;
  unitCost: FormControl<InventoryItemFormRawValue['unitCost']>;
  inventoryItemType: FormControl<InventoryItemFormRawValue['inventoryItemType']>;
  product: FormControl<InventoryItemFormRawValue['product']>;
  supplier: FormControl<InventoryItemFormRawValue['supplier']>;
  ownerParty: FormControl<InventoryItemFormRawValue['ownerParty']>;
  status: FormControl<InventoryItemFormRawValue['status']>;
  facility: FormControl<InventoryItemFormRawValue['facility']>;
  facilityLocation: FormControl<InventoryItemFormRawValue['facilityLocation']>;
  uom: FormControl<InventoryItemFormRawValue['uom']>;
  currencyUom: FormControl<InventoryItemFormRawValue['currencyUom']>;
  lot: FormControl<InventoryItemFormRawValue['lot']>;
};

export type InventoryItemFormGroup = FormGroup<InventoryItemFormGroupContent>;

@Service()
export class InventoryItemFormService {
  createInventoryItemFormGroup(inventoryItem?: InventoryItemFormGroupInput): InventoryItemFormGroup {
    const inventoryItemRawValue = this.convertInventoryItemToInventoryItemRawValue({
      ...this.getFormDefaults(),
      ...(inventoryItem ?? { id: null }),
    });

    return new FormGroup<InventoryItemFormGroupContent>({
      id: new FormControl(
        { value: inventoryItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      receivedDate: new FormControl(inventoryItemRawValue.receivedDate),
      manufacturedDate: new FormControl(inventoryItemRawValue.manufacturedDate),
      expiryDate: new FormControl(inventoryItemRawValue.expiryDate),
      retestDate: new FormControl(inventoryItemRawValue.retestDate),
      containerId: new FormControl(inventoryItemRawValue.containerId, {
        validators: [Validators.maxLength(60)],
      }),
      batchNo: new FormControl(inventoryItemRawValue.batchNo, {
        validators: [Validators.maxLength(60)],
      }),
      mfgBatchNo: new FormControl(inventoryItemRawValue.mfgBatchNo, {
        validators: [Validators.maxLength(60)],
      }),
      lotNoStr: new FormControl(inventoryItemRawValue.lotNoStr, {
        validators: [Validators.maxLength(60)],
      }),
      binNumber: new FormControl(inventoryItemRawValue.binNumber, {
        validators: [Validators.maxLength(60)],
      }),
      comments: new FormControl(inventoryItemRawValue.comments, {
        validators: [Validators.maxLength(255)],
      }),
      quantityOnHandTotal: new FormControl(inventoryItemRawValue.quantityOnHandTotal),
      availableToPromiseTotal: new FormControl(inventoryItemRawValue.availableToPromiseTotal),
      accountingQuantityTotal: new FormControl(inventoryItemRawValue.accountingQuantityTotal),
      oldQuantityOnHand: new FormControl(inventoryItemRawValue.oldQuantityOnHand),
      oldAvailableToPromise: new FormControl(inventoryItemRawValue.oldAvailableToPromise),
      serialNumber: new FormControl(inventoryItemRawValue.serialNumber, {
        validators: [Validators.maxLength(255)],
      }),
      softIdentifier: new FormControl(inventoryItemRawValue.softIdentifier, {
        validators: [Validators.maxLength(255)],
      }),
      activationNumber: new FormControl(inventoryItemRawValue.activationNumber, {
        validators: [Validators.maxLength(255)],
      }),
      activationValidThru: new FormControl(inventoryItemRawValue.activationValidThru),
      unitCost: new FormControl(inventoryItemRawValue.unitCost),
      inventoryItemType: new FormControl(inventoryItemRawValue.inventoryItemType),
      product: new FormControl(inventoryItemRawValue.product, {
        validators: [Validators.required],
      }),
      supplier: new FormControl(inventoryItemRawValue.supplier),
      ownerParty: new FormControl(inventoryItemRawValue.ownerParty),
      status: new FormControl(inventoryItemRawValue.status),
      facility: new FormControl(inventoryItemRawValue.facility),
      facilityLocation: new FormControl(inventoryItemRawValue.facilityLocation),
      uom: new FormControl(inventoryItemRawValue.uom),
      currencyUom: new FormControl(inventoryItemRawValue.currencyUom),
      lot: new FormControl(inventoryItemRawValue.lot),
    });
  }

  getInventoryItem(form: InventoryItemFormGroup): IInventoryItem | NewInventoryItem {
    return this.convertInventoryItemRawValueToInventoryItem(form.getRawValue());
  }

  resetForm(form: InventoryItemFormGroup, inventoryItem: InventoryItemFormGroupInput): void {
    const inventoryItemRawValue = this.convertInventoryItemToInventoryItemRawValue({ ...this.getFormDefaults(), ...inventoryItem });
    form.reset({
      ...inventoryItemRawValue,
      id: { value: inventoryItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InventoryItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      receivedDate: currentTime,
      manufacturedDate: currentTime,
      expiryDate: currentTime,
      retestDate: currentTime,
      activationValidThru: currentTime,
    };
  }

  private convertInventoryItemRawValueToInventoryItem(
    rawInventoryItem: InventoryItemFormRawValue | NewInventoryItemFormRawValue,
  ): IInventoryItem | NewInventoryItem {
    return {
      ...rawInventoryItem,
      receivedDate: dayjs(rawInventoryItem.receivedDate, DATE_TIME_FORMAT),
      manufacturedDate: dayjs(rawInventoryItem.manufacturedDate, DATE_TIME_FORMAT),
      expiryDate: dayjs(rawInventoryItem.expiryDate, DATE_TIME_FORMAT),
      retestDate: dayjs(rawInventoryItem.retestDate, DATE_TIME_FORMAT),
      activationValidThru: dayjs(rawInventoryItem.activationValidThru, DATE_TIME_FORMAT),
    };
  }

  private convertInventoryItemToInventoryItemRawValue(
    inventoryItem: IInventoryItem | (Partial<NewInventoryItem> & InventoryItemFormDefaults),
  ): InventoryItemFormRawValue | PartialWithRequiredKeyOf<NewInventoryItemFormRawValue> {
    return {
      ...inventoryItem,
      receivedDate: inventoryItem.receivedDate ? inventoryItem.receivedDate.format(DATE_TIME_FORMAT) : undefined,
      manufacturedDate: inventoryItem.manufacturedDate ? inventoryItem.manufacturedDate.format(DATE_TIME_FORMAT) : undefined,
      expiryDate: inventoryItem.expiryDate ? inventoryItem.expiryDate.format(DATE_TIME_FORMAT) : undefined,
      retestDate: inventoryItem.retestDate ? inventoryItem.retestDate.format(DATE_TIME_FORMAT) : undefined,
      activationValidThru: inventoryItem.activationValidThru ? inventoryItem.activationValidThru.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
