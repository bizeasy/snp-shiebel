import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IShipmentStatus, NewShipmentStatus } from '../shipment-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipmentStatus for edit and NewShipmentStatusFormGroupInput for create.
 */
type ShipmentStatusFormGroupInput = IShipmentStatus | PartialWithRequiredKeyOf<NewShipmentStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IShipmentStatus | NewShipmentStatus> = Omit<T, 'statusDateTime'> & {
  statusDateTime?: string | null;
};

type ShipmentStatusFormRawValue = FormValueOf<IShipmentStatus>;

type NewShipmentStatusFormRawValue = FormValueOf<NewShipmentStatus>;

type ShipmentStatusFormDefaults = Pick<NewShipmentStatus, 'id' | 'statusDateTime'>;

type ShipmentStatusFormGroupContent = {
  id: FormControl<ShipmentStatusFormRawValue['id'] | NewShipmentStatus['id']>;
  statusDateTime: FormControl<ShipmentStatusFormRawValue['statusDateTime']>;
  sequenceNo: FormControl<ShipmentStatusFormRawValue['sequenceNo']>;
  shipment: FormControl<ShipmentStatusFormRawValue['shipment']>;
  status: FormControl<ShipmentStatusFormRawValue['status']>;
};

export type ShipmentStatusFormGroup = FormGroup<ShipmentStatusFormGroupContent>;

@Service()
export class ShipmentStatusFormService {
  createShipmentStatusFormGroup(shipmentStatus?: ShipmentStatusFormGroupInput): ShipmentStatusFormGroup {
    const shipmentStatusRawValue = this.convertShipmentStatusToShipmentStatusRawValue({
      ...this.getFormDefaults(),
      ...(shipmentStatus ?? { id: null }),
    });

    return new FormGroup<ShipmentStatusFormGroupContent>({
      id: new FormControl(
        { value: shipmentStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      statusDateTime: new FormControl(shipmentStatusRawValue.statusDateTime, {
        validators: [Validators.required],
      }),
      sequenceNo: new FormControl(shipmentStatusRawValue.sequenceNo),
      shipment: new FormControl(shipmentStatusRawValue.shipment, {
        validators: [Validators.required],
      }),
      status: new FormControl(shipmentStatusRawValue.status, {
        validators: [Validators.required],
      }),
    });
  }

  getShipmentStatus(form: ShipmentStatusFormGroup): IShipmentStatus | NewShipmentStatus {
    return this.convertShipmentStatusRawValueToShipmentStatus(form.getRawValue());
  }

  resetForm(form: ShipmentStatusFormGroup, shipmentStatus: ShipmentStatusFormGroupInput): void {
    const shipmentStatusRawValue = this.convertShipmentStatusToShipmentStatusRawValue({ ...this.getFormDefaults(), ...shipmentStatus });
    form.reset({
      ...shipmentStatusRawValue,
      id: { value: shipmentStatusRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentStatusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      statusDateTime: currentTime,
    };
  }

  private convertShipmentStatusRawValueToShipmentStatus(
    rawShipmentStatus: ShipmentStatusFormRawValue | NewShipmentStatusFormRawValue,
  ): IShipmentStatus | NewShipmentStatus {
    return {
      ...rawShipmentStatus,
      statusDateTime: dayjs(rawShipmentStatus.statusDateTime, DATE_TIME_FORMAT),
    };
  }

  private convertShipmentStatusToShipmentStatusRawValue(
    shipmentStatus: IShipmentStatus | (Partial<NewShipmentStatus> & ShipmentStatusFormDefaults),
  ): ShipmentStatusFormRawValue | PartialWithRequiredKeyOf<NewShipmentStatusFormRawValue> {
    return {
      ...shipmentStatus,
      statusDateTime: shipmentStatus.statusDateTime ? shipmentStatus.statusDateTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
