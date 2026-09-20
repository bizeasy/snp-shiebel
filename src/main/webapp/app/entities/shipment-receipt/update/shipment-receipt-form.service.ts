import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IShipmentReceipt, NewShipmentReceipt } from '../shipment-receipt.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipmentReceipt for edit and NewShipmentReceiptFormGroupInput for create.
 */
type ShipmentReceiptFormGroupInput = IShipmentReceipt | PartialWithRequiredKeyOf<NewShipmentReceipt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IShipmentReceipt | NewShipmentReceipt> = Omit<T, 'receivedDate'> & {
  receivedDate?: string | null;
};

type ShipmentReceiptFormRawValue = FormValueOf<IShipmentReceipt>;

type NewShipmentReceiptFormRawValue = FormValueOf<NewShipmentReceipt>;

type ShipmentReceiptFormDefaults = Pick<NewShipmentReceipt, 'id' | 'receivedDate'>;

type ShipmentReceiptFormGroupContent = {
  id: FormControl<ShipmentReceiptFormRawValue['id'] | NewShipmentReceipt['id']>;
  sequenceNo: FormControl<ShipmentReceiptFormRawValue['sequenceNo']>;
  quantity: FormControl<ShipmentReceiptFormRawValue['quantity']>;
  quantityAccepted: FormControl<ShipmentReceiptFormRawValue['quantityAccepted']>;
  quantityRejected: FormControl<ShipmentReceiptFormRawValue['quantityRejected']>;
  receivedDate: FormControl<ShipmentReceiptFormRawValue['receivedDate']>;
  message: FormControl<ShipmentReceiptFormRawValue['message']>;
  shipment: FormControl<ShipmentReceiptFormRawValue['shipment']>;
  shipmentItem: FormControl<ShipmentReceiptFormRawValue['shipmentItem']>;
  order: FormControl<ShipmentReceiptFormRawValue['order']>;
  orderItem: FormControl<ShipmentReceiptFormRawValue['orderItem']>;
  inventoryItem: FormControl<ShipmentReceiptFormRawValue['inventoryItem']>;
  product: FormControl<ShipmentReceiptFormRawValue['product']>;
  receivedBy: FormControl<ShipmentReceiptFormRawValue['receivedBy']>;
};

export type ShipmentReceiptFormGroup = FormGroup<ShipmentReceiptFormGroupContent>;

@Service()
export class ShipmentReceiptFormService {
  createShipmentReceiptFormGroup(shipmentReceipt?: ShipmentReceiptFormGroupInput): ShipmentReceiptFormGroup {
    const shipmentReceiptRawValue = this.convertShipmentReceiptToShipmentReceiptRawValue({
      ...this.getFormDefaults(),
      ...(shipmentReceipt ?? { id: null }),
    });

    return new FormGroup<ShipmentReceiptFormGroupContent>({
      id: new FormControl(
        { value: shipmentReceiptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(shipmentReceiptRawValue.sequenceNo),
      quantity: new FormControl(shipmentReceiptRawValue.quantity, {
        validators: [Validators.required],
      }),
      quantityAccepted: new FormControl(shipmentReceiptRawValue.quantityAccepted),
      quantityRejected: new FormControl(shipmentReceiptRawValue.quantityRejected),
      receivedDate: new FormControl(shipmentReceiptRawValue.receivedDate),
      message: new FormControl(shipmentReceiptRawValue.message, {
        validators: [Validators.maxLength(255)],
      }),
      shipment: new FormControl(shipmentReceiptRawValue.shipment),
      shipmentItem: new FormControl(shipmentReceiptRawValue.shipmentItem),
      order: new FormControl(shipmentReceiptRawValue.order),
      orderItem: new FormControl(shipmentReceiptRawValue.orderItem),
      inventoryItem: new FormControl(shipmentReceiptRawValue.inventoryItem),
      product: new FormControl(shipmentReceiptRawValue.product),
      receivedBy: new FormControl(shipmentReceiptRawValue.receivedBy),
    });
  }

  getShipmentReceipt(form: ShipmentReceiptFormGroup): IShipmentReceipt | NewShipmentReceipt {
    return this.convertShipmentReceiptRawValueToShipmentReceipt(form.getRawValue());
  }

  resetForm(form: ShipmentReceiptFormGroup, shipmentReceipt: ShipmentReceiptFormGroupInput): void {
    const shipmentReceiptRawValue = this.convertShipmentReceiptToShipmentReceiptRawValue({ ...this.getFormDefaults(), ...shipmentReceipt });
    form.reset({
      ...shipmentReceiptRawValue,
      id: { value: shipmentReceiptRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentReceiptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      receivedDate: currentTime,
    };
  }

  private convertShipmentReceiptRawValueToShipmentReceipt(
    rawShipmentReceipt: ShipmentReceiptFormRawValue | NewShipmentReceiptFormRawValue,
  ): IShipmentReceipt | NewShipmentReceipt {
    return {
      ...rawShipmentReceipt,
      receivedDate: dayjs(rawShipmentReceipt.receivedDate, DATE_TIME_FORMAT),
    };
  }

  private convertShipmentReceiptToShipmentReceiptRawValue(
    shipmentReceipt: IShipmentReceipt | (Partial<NewShipmentReceipt> & ShipmentReceiptFormDefaults),
  ): ShipmentReceiptFormRawValue | PartialWithRequiredKeyOf<NewShipmentReceiptFormRawValue> {
    return {
      ...shipmentReceipt,
      receivedDate: shipmentReceipt.receivedDate ? shipmentReceipt.receivedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
