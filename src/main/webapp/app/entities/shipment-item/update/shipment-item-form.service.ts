import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShipmentItem, NewShipmentItem } from '../shipment-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipmentItem for edit and NewShipmentItemFormGroupInput for create.
 */
type ShipmentItemFormGroupInput = IShipmentItem | PartialWithRequiredKeyOf<NewShipmentItem>;

type ShipmentItemFormDefaults = Pick<NewShipmentItem, 'id'>;

type ShipmentItemFormGroupContent = {
  id: FormControl<IShipmentItem['id'] | NewShipmentItem['id']>;
  sequenceNo: FormControl<IShipmentItem['sequenceNo']>;
  quantity: FormControl<IShipmentItem['quantity']>;
  message: FormControl<IShipmentItem['message']>;
  shipment: FormControl<IShipmentItem['shipment']>;
  product: FormControl<IShipmentItem['product']>;
};

export type ShipmentItemFormGroup = FormGroup<ShipmentItemFormGroupContent>;

@Service()
export class ShipmentItemFormService {
  createShipmentItemFormGroup(shipmentItem?: ShipmentItemFormGroupInput): ShipmentItemFormGroup {
    const shipmentItemRawValue = {
      ...this.getFormDefaults(),
      ...(shipmentItem ?? { id: null }),
    };

    return new FormGroup<ShipmentItemFormGroupContent>({
      id: new FormControl(
        { value: shipmentItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(shipmentItemRawValue.sequenceNo),
      quantity: new FormControl(shipmentItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      message: new FormControl(shipmentItemRawValue.message, {
        validators: [Validators.maxLength(255)],
      }),
      shipment: new FormControl(shipmentItemRawValue.shipment, {
        validators: [Validators.required],
      }),
      product: new FormControl(shipmentItemRawValue.product),
    });
  }

  getShipmentItem(form: ShipmentItemFormGroup): IShipmentItem | NewShipmentItem {
    return form.getRawValue();
  }

  resetForm(form: ShipmentItemFormGroup, shipmentItem: ShipmentItemFormGroupInput): void {
    const shipmentItemRawValue = { ...this.getFormDefaults(), ...shipmentItem };
    form.reset({
      ...shipmentItemRawValue,
      id: { value: shipmentItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentItemFormDefaults {
    return {
      id: null,
    };
  }
}
