import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShipmentAttachment, NewShipmentAttachment } from '../shipment-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShipmentAttachment for edit and NewShipmentAttachmentFormGroupInput for create.
 */
type ShipmentAttachmentFormGroupInput = IShipmentAttachment | PartialWithRequiredKeyOf<NewShipmentAttachment>;

type ShipmentAttachmentFormDefaults = Pick<NewShipmentAttachment, 'id'>;

type ShipmentAttachmentFormGroupContent = {
  id: FormControl<IShipmentAttachment['id'] | NewShipmentAttachment['id']>;
  name: FormControl<IShipmentAttachment['name']>;
  shipment: FormControl<IShipmentAttachment['shipment']>;
  attachment: FormControl<IShipmentAttachment['attachment']>;
};

export type ShipmentAttachmentFormGroup = FormGroup<ShipmentAttachmentFormGroupContent>;

@Service()
export class ShipmentAttachmentFormService {
  createShipmentAttachmentFormGroup(shipmentAttachment?: ShipmentAttachmentFormGroupInput): ShipmentAttachmentFormGroup {
    const shipmentAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(shipmentAttachment ?? { id: null }),
    };

    return new FormGroup<ShipmentAttachmentFormGroupContent>({
      id: new FormControl(
        { value: shipmentAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(shipmentAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      shipment: new FormControl(shipmentAttachmentRawValue.shipment, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(shipmentAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getShipmentAttachment(form: ShipmentAttachmentFormGroup): IShipmentAttachment | NewShipmentAttachment {
    return form.getRawValue();
  }

  resetForm(form: ShipmentAttachmentFormGroup, shipmentAttachment: ShipmentAttachmentFormGroupInput): void {
    const shipmentAttachmentRawValue = { ...this.getFormDefaults(), ...shipmentAttachment };
    form.reset({
      ...shipmentAttachmentRawValue,
      id: { value: shipmentAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ShipmentAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
