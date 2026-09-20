import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShipmentMethodType, NewShipmentMethodType } from '../shipment-method-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipmentMethodType for edit and NewShipmentMethodTypeFormGroupInput for create.
 */
type ShipmentMethodTypeFormGroupInput = IShipmentMethodType | PartialWithRequiredKeyOf<NewShipmentMethodType>;

type ShipmentMethodTypeFormDefaults = Pick<NewShipmentMethodType, 'id'>;

type ShipmentMethodTypeFormGroupContent = {
  id: FormControl<IShipmentMethodType['id'] | NewShipmentMethodType['id']>;
  name: FormControl<IShipmentMethodType['name']>;
  description: FormControl<IShipmentMethodType['description']>;
  sequenceNo: FormControl<IShipmentMethodType['sequenceNo']>;
};

export type ShipmentMethodTypeFormGroup = FormGroup<ShipmentMethodTypeFormGroupContent>;

@Service()
export class ShipmentMethodTypeFormService {
  createShipmentMethodTypeFormGroup(shipmentMethodType?: ShipmentMethodTypeFormGroupInput): ShipmentMethodTypeFormGroup {
    const shipmentMethodTypeRawValue = {
      ...this.getFormDefaults(),
      ...(shipmentMethodType ?? { id: null }),
    };

    return new FormGroup<ShipmentMethodTypeFormGroupContent>({
      id: new FormControl(
        { value: shipmentMethodTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(shipmentMethodTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(shipmentMethodTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      sequenceNo: new FormControl(shipmentMethodTypeRawValue.sequenceNo),
    });
  }

  getShipmentMethodType(form: ShipmentMethodTypeFormGroup): IShipmentMethodType | NewShipmentMethodType {
    return form.getRawValue();
  }

  resetForm(form: ShipmentMethodTypeFormGroup, shipmentMethodType: ShipmentMethodTypeFormGroupInput): void {
    const shipmentMethodTypeRawValue = { ...this.getFormDefaults(), ...shipmentMethodType };
    form.reset({
      ...shipmentMethodTypeRawValue,
      id: { value: shipmentMethodTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentMethodTypeFormDefaults {
    return {
      id: null,
    };
  }
}
