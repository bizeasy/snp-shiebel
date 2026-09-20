import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFacilityCarrierShipment, NewFacilityCarrierShipment } from '../facility-carrier-shipment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityCarrierShipment for edit and NewFacilityCarrierShipmentFormGroupInput for create.
 */
type FacilityCarrierShipmentFormGroupInput = IFacilityCarrierShipment | PartialWithRequiredKeyOf<NewFacilityCarrierShipment>;

type FacilityCarrierShipmentFormDefaults = Pick<NewFacilityCarrierShipment, 'id'>;

type FacilityCarrierShipmentFormGroupContent = {
  id: FormControl<IFacilityCarrierShipment['id'] | NewFacilityCarrierShipment['id']>;
  facility: FormControl<IFacilityCarrierShipment['facility']>;
  party: FormControl<IFacilityCarrierShipment['party']>;
  roleType: FormControl<IFacilityCarrierShipment['roleType']>;
  shipmentMethodType: FormControl<IFacilityCarrierShipment['shipmentMethodType']>;
};

export type FacilityCarrierShipmentFormGroup = FormGroup<FacilityCarrierShipmentFormGroupContent>;

@Service()
export class FacilityCarrierShipmentFormService {
  createFacilityCarrierShipmentFormGroup(
    facilityCarrierShipment?: FacilityCarrierShipmentFormGroupInput,
  ): FacilityCarrierShipmentFormGroup {
    const facilityCarrierShipmentRawValue = {
      ...this.getFormDefaults(),
      ...(facilityCarrierShipment ?? { id: null }),
    };

    return new FormGroup<FacilityCarrierShipmentFormGroupContent>({
      id: new FormControl(
        { value: facilityCarrierShipmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      facility: new FormControl(facilityCarrierShipmentRawValue.facility, {
        validators: [Validators.required],
      }),
      party: new FormControl(facilityCarrierShipmentRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(facilityCarrierShipmentRawValue.roleType),
      shipmentMethodType: new FormControl(facilityCarrierShipmentRawValue.shipmentMethodType),
    });
  }

  getFacilityCarrierShipment(form: FacilityCarrierShipmentFormGroup): IFacilityCarrierShipment | NewFacilityCarrierShipment {
    return form.getRawValue();
  }

  resetForm(form: FacilityCarrierShipmentFormGroup, facilityCarrierShipment: FacilityCarrierShipmentFormGroupInput): void {
    const facilityCarrierShipmentRawValue = { ...this.getFormDefaults(), ...facilityCarrierShipment };
    form.reset({
      ...facilityCarrierShipmentRawValue,
      id: { value: facilityCarrierShipmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityCarrierShipmentFormDefaults {
    return {
      id: null,
    };
  }
}
