import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IInventoryTransfer, NewInventoryTransfer } from '../inventory-transfer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryTransfer for edit and NewInventoryTransferFormGroupInput for create.
 */
type InventoryTransferFormGroupInput = IInventoryTransfer | PartialWithRequiredKeyOf<NewInventoryTransfer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInventoryTransfer | NewInventoryTransfer> = Omit<T, 'sentDate' | 'receivedDate'> & {
  sentDate?: string | null;
  receivedDate?: string | null;
};

type InventoryTransferFormRawValue = FormValueOf<IInventoryTransfer>;

type NewInventoryTransferFormRawValue = FormValueOf<NewInventoryTransfer>;

type InventoryTransferFormDefaults = Pick<NewInventoryTransfer, 'id' | 'sentDate' | 'receivedDate'>;

type InventoryTransferFormGroupContent = {
  id: FormControl<InventoryTransferFormRawValue['id'] | NewInventoryTransfer['id']>;
  sentDate: FormControl<InventoryTransferFormRawValue['sentDate']>;
  receivedDate: FormControl<InventoryTransferFormRawValue['receivedDate']>;
  comments: FormControl<InventoryTransferFormRawValue['comments']>;
  inventoryItem: FormControl<InventoryTransferFormRawValue['inventoryItem']>;
  status: FormControl<InventoryTransferFormRawValue['status']>;
  facility: FormControl<InventoryTransferFormRawValue['facility']>;
  facilityLocation: FormControl<InventoryTransferFormRawValue['facilityLocation']>;
  facilityTo: FormControl<InventoryTransferFormRawValue['facilityTo']>;
  facilityLocationTo: FormControl<InventoryTransferFormRawValue['facilityLocationTo']>;
  issuance: FormControl<InventoryTransferFormRawValue['issuance']>;
};

export type InventoryTransferFormGroup = FormGroup<InventoryTransferFormGroupContent>;

@Service()
export class InventoryTransferFormService {
  createInventoryTransferFormGroup(inventoryTransfer?: InventoryTransferFormGroupInput): InventoryTransferFormGroup {
    const inventoryTransferRawValue = this.convertInventoryTransferToInventoryTransferRawValue({
      ...this.getFormDefaults(),
      ...(inventoryTransfer ?? { id: null }),
    });

    return new FormGroup<InventoryTransferFormGroupContent>({
      id: new FormControl(
        { value: inventoryTransferRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sentDate: new FormControl(inventoryTransferRawValue.sentDate),
      receivedDate: new FormControl(inventoryTransferRawValue.receivedDate),
      comments: new FormControl(inventoryTransferRawValue.comments, {
        validators: [Validators.maxLength(255)],
      }),
      inventoryItem: new FormControl(inventoryTransferRawValue.inventoryItem, {
        validators: [Validators.required],
      }),
      status: new FormControl(inventoryTransferRawValue.status),
      facility: new FormControl(inventoryTransferRawValue.facility),
      facilityLocation: new FormControl(inventoryTransferRawValue.facilityLocation),
      facilityTo: new FormControl(inventoryTransferRawValue.facilityTo),
      facilityLocationTo: new FormControl(inventoryTransferRawValue.facilityLocationTo),
      issuance: new FormControl(inventoryTransferRawValue.issuance),
    });
  }

  getInventoryTransfer(form: InventoryTransferFormGroup): IInventoryTransfer | NewInventoryTransfer {
    return this.convertInventoryTransferRawValueToInventoryTransfer(form.getRawValue());
  }

  resetForm(form: InventoryTransferFormGroup, inventoryTransfer: InventoryTransferFormGroupInput): void {
    const inventoryTransferRawValue = this.convertInventoryTransferToInventoryTransferRawValue({
      ...this.getFormDefaults(),
      ...inventoryTransfer,
    });
    form.reset({
      ...inventoryTransferRawValue,
      id: { value: inventoryTransferRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InventoryTransferFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      sentDate: currentTime,
      receivedDate: currentTime,
    };
  }

  private convertInventoryTransferRawValueToInventoryTransfer(
    rawInventoryTransfer: InventoryTransferFormRawValue | NewInventoryTransferFormRawValue,
  ): IInventoryTransfer | NewInventoryTransfer {
    return {
      ...rawInventoryTransfer,
      sentDate: dayjs(rawInventoryTransfer.sentDate, DATE_TIME_FORMAT),
      receivedDate: dayjs(rawInventoryTransfer.receivedDate, DATE_TIME_FORMAT),
    };
  }

  private convertInventoryTransferToInventoryTransferRawValue(
    inventoryTransfer: IInventoryTransfer | (Partial<NewInventoryTransfer> & InventoryTransferFormDefaults),
  ): InventoryTransferFormRawValue | PartialWithRequiredKeyOf<NewInventoryTransferFormRawValue> {
    return {
      ...inventoryTransfer,
      sentDate: inventoryTransfer.sentDate ? inventoryTransfer.sentDate.format(DATE_TIME_FORMAT) : undefined,
      receivedDate: inventoryTransfer.receivedDate ? inventoryTransfer.receivedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
