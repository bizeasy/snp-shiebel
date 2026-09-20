import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShipmentType, NewShipmentType } from '../shipment-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipmentType for edit and NewShipmentTypeFormGroupInput for create.
 */
type ShipmentTypeFormGroupInput = IShipmentType | PartialWithRequiredKeyOf<NewShipmentType>;

type ShipmentTypeFormDefaults = Pick<NewShipmentType, 'id'>;

type ShipmentTypeFormGroupContent = {
  id: FormControl<IShipmentType['id'] | NewShipmentType['id']>;
  name: FormControl<IShipmentType['name']>;
  description: FormControl<IShipmentType['description']>;
};

export type ShipmentTypeFormGroup = FormGroup<ShipmentTypeFormGroupContent>;

@Service()
export class ShipmentTypeFormService {
  createShipmentTypeFormGroup(shipmentType?: ShipmentTypeFormGroupInput): ShipmentTypeFormGroup {
    const shipmentTypeRawValue = {
      ...this.getFormDefaults(),
      ...(shipmentType ?? { id: null }),
    };

    return new FormGroup<ShipmentTypeFormGroupContent>({
      id: new FormControl(
        { value: shipmentTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(shipmentTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(shipmentTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
    });
  }

  getShipmentType(form: ShipmentTypeFormGroup): IShipmentType | NewShipmentType {
    return form.getRawValue();
  }

  resetForm(form: ShipmentTypeFormGroup, shipmentType: ShipmentTypeFormGroupInput): void {
    const shipmentTypeRawValue = { ...this.getFormDefaults(), ...shipmentType };
    form.reset({
      ...shipmentTypeRawValue,
      id: { value: shipmentTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentTypeFormDefaults {
    return {
      id: null,
    };
  }
}
