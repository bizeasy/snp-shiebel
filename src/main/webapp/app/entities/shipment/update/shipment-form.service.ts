import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IShipment, NewShipment } from '../shipment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipment for edit and NewShipmentFormGroupInput for create.
 */
type ShipmentFormGroupInput = IShipment | PartialWithRequiredKeyOf<NewShipment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IShipment | NewShipment> = Omit<T, 'estimatedShipDate' | 'estimatedReadyDate' | 'estimatedArrivalDate'> & {
  estimatedShipDate?: string | null;
  estimatedReadyDate?: string | null;
  estimatedArrivalDate?: string | null;
};

type ShipmentFormRawValue = FormValueOf<IShipment>;

type NewShipmentFormRawValue = FormValueOf<NewShipment>;

type ShipmentFormDefaults = Pick<NewShipment, 'id' | 'estimatedShipDate' | 'estimatedReadyDate' | 'estimatedArrivalDate'>;

type ShipmentFormGroupContent = {
  id: FormControl<ShipmentFormRawValue['id'] | NewShipment['id']>;
  estimatedShipDate: FormControl<ShipmentFormRawValue['estimatedShipDate']>;
  estimatedReadyDate: FormControl<ShipmentFormRawValue['estimatedReadyDate']>;
  estimatedArrivalDate: FormControl<ShipmentFormRawValue['estimatedArrivalDate']>;
  estimatedShipCost: FormControl<ShipmentFormRawValue['estimatedShipCost']>;
  shipmentType: FormControl<ShipmentFormRawValue['shipmentType']>;
  shipmentMethodType: FormControl<ShipmentFormRawValue['shipmentMethodType']>;
  status: FormControl<ShipmentFormRawValue['status']>;
  order: FormControl<ShipmentFormRawValue['order']>;
  orderItem: FormControl<ShipmentFormRawValue['orderItem']>;
  originFacility: FormControl<ShipmentFormRawValue['originFacility']>;
  destinationFacility: FormControl<ShipmentFormRawValue['destinationFacility']>;
  fromParty: FormControl<ShipmentFormRawValue['fromParty']>;
  toParty: FormControl<ShipmentFormRawValue['toParty']>;
  carrierParty: FormControl<ShipmentFormRawValue['carrierParty']>;
  contactMech: FormControl<ShipmentFormRawValue['contactMech']>;
};

export type ShipmentFormGroup = FormGroup<ShipmentFormGroupContent>;

@Service()
export class ShipmentFormService {
  createShipmentFormGroup(shipment?: ShipmentFormGroupInput): ShipmentFormGroup {
    const shipmentRawValue = this.convertShipmentToShipmentRawValue({
      ...this.getFormDefaults(),
      ...(shipment ?? { id: null }),
    });

    return new FormGroup<ShipmentFormGroupContent>({
      id: new FormControl(
        { value: shipmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      estimatedShipDate: new FormControl(shipmentRawValue.estimatedShipDate),
      estimatedReadyDate: new FormControl(shipmentRawValue.estimatedReadyDate),
      estimatedArrivalDate: new FormControl(shipmentRawValue.estimatedArrivalDate),
      estimatedShipCost: new FormControl(shipmentRawValue.estimatedShipCost),
      shipmentType: new FormControl(shipmentRawValue.shipmentType),
      shipmentMethodType: new FormControl(shipmentRawValue.shipmentMethodType),
      status: new FormControl(shipmentRawValue.status),
      order: new FormControl(shipmentRawValue.order),
      orderItem: new FormControl(shipmentRawValue.orderItem),
      originFacility: new FormControl(shipmentRawValue.originFacility),
      destinationFacility: new FormControl(shipmentRawValue.destinationFacility),
      fromParty: new FormControl(shipmentRawValue.fromParty),
      toParty: new FormControl(shipmentRawValue.toParty),
      carrierParty: new FormControl(shipmentRawValue.carrierParty),
      contactMech: new FormControl(shipmentRawValue.contactMech),
    });
  }

  getShipment(form: ShipmentFormGroup): IShipment | NewShipment {
    return this.convertShipmentRawValueToShipment(form.getRawValue());
  }

  resetForm(form: ShipmentFormGroup, shipment: ShipmentFormGroupInput): void {
    const shipmentRawValue = this.convertShipmentToShipmentRawValue({ ...this.getFormDefaults(), ...shipment });
    form.reset({
      ...shipmentRawValue,
      id: { value: shipmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      estimatedShipDate: currentTime,
      estimatedReadyDate: currentTime,
      estimatedArrivalDate: currentTime,
    };
  }

  private convertShipmentRawValueToShipment(rawShipment: ShipmentFormRawValue | NewShipmentFormRawValue): IShipment | NewShipment {
    return {
      ...rawShipment,
      estimatedShipDate: dayjs(rawShipment.estimatedShipDate, DATE_TIME_FORMAT),
      estimatedReadyDate: dayjs(rawShipment.estimatedReadyDate, DATE_TIME_FORMAT),
      estimatedArrivalDate: dayjs(rawShipment.estimatedArrivalDate, DATE_TIME_FORMAT),
    };
  }

  private convertShipmentToShipmentRawValue(
    shipment: IShipment | (Partial<NewShipment> & ShipmentFormDefaults),
  ): ShipmentFormRawValue | PartialWithRequiredKeyOf<NewShipmentFormRawValue> {
    return {
      ...shipment,
      estimatedShipDate: shipment.estimatedShipDate ? shipment.estimatedShipDate.format(DATE_TIME_FORMAT) : undefined,
      estimatedReadyDate: shipment.estimatedReadyDate ? shipment.estimatedReadyDate.format(DATE_TIME_FORMAT) : undefined,
      estimatedArrivalDate: shipment.estimatedArrivalDate ? shipment.estimatedArrivalDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
