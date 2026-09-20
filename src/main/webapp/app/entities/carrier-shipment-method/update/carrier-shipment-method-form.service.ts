import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICarrierShipmentMethod, NewCarrierShipmentMethod } from '../carrier-shipment-method.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICarrierShipmentMethod for edit and NewCarrierShipmentMethodFormGroupInput for create.
 */
type CarrierShipmentMethodFormGroupInput = ICarrierShipmentMethod | PartialWithRequiredKeyOf<NewCarrierShipmentMethod>;

type CarrierShipmentMethodFormDefaults = Pick<NewCarrierShipmentMethod, 'id'>;

type CarrierShipmentMethodFormGroupContent = {
  id: FormControl<ICarrierShipmentMethod['id'] | NewCarrierShipmentMethod['id']>;
  sequenceNo: FormControl<ICarrierShipmentMethod['sequenceNo']>;
  carrierServiceCode: FormControl<ICarrierShipmentMethod['carrierServiceCode']>;
  shipmentMethodType: FormControl<ICarrierShipmentMethod['shipmentMethodType']>;
  party: FormControl<ICarrierShipmentMethod['party']>;
  roleType: FormControl<ICarrierShipmentMethod['roleType']>;
};

export type CarrierShipmentMethodFormGroup = FormGroup<CarrierShipmentMethodFormGroupContent>;

@Service()
export class CarrierShipmentMethodFormService {
  createCarrierShipmentMethodFormGroup(carrierShipmentMethod?: CarrierShipmentMethodFormGroupInput): CarrierShipmentMethodFormGroup {
    const carrierShipmentMethodRawValue = {
      ...this.getFormDefaults(),
      ...(carrierShipmentMethod ?? { id: null }),
    };

    return new FormGroup<CarrierShipmentMethodFormGroupContent>({
      id: new FormControl(
        { value: carrierShipmentMethodRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(carrierShipmentMethodRawValue.sequenceNo),
      carrierServiceCode: new FormControl(carrierShipmentMethodRawValue.carrierServiceCode, {
        validators: [Validators.maxLength(60)],
      }),
      shipmentMethodType: new FormControl(carrierShipmentMethodRawValue.shipmentMethodType, {
        validators: [Validators.required],
      }),
      party: new FormControl(carrierShipmentMethodRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(carrierShipmentMethodRawValue.roleType),
    });
  }

  getCarrierShipmentMethod(form: CarrierShipmentMethodFormGroup): ICarrierShipmentMethod | NewCarrierShipmentMethod {
    return form.getRawValue();
  }

  resetForm(form: CarrierShipmentMethodFormGroup, carrierShipmentMethod: CarrierShipmentMethodFormGroupInput): void {
    const carrierShipmentMethodRawValue = { ...this.getFormDefaults(), ...carrierShipmentMethod };
    form.reset({
      ...carrierShipmentMethodRawValue,
      id: { value: carrierShipmentMethodRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CarrierShipmentMethodFormDefaults {
    return {
      id: null,
    };
  }
}
