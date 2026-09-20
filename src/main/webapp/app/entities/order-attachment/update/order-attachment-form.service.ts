import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderAttachment, NewOrderAttachment } from '../order-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderAttachment for edit and NewOrderAttachmentFormGroupInput for create.
 */
type OrderAttachmentFormGroupInput = IOrderAttachment | PartialWithRequiredKeyOf<NewOrderAttachment>;

type OrderAttachmentFormDefaults = Pick<NewOrderAttachment, 'id'>;

type OrderAttachmentFormGroupContent = {
  id: FormControl<IOrderAttachment['id'] | NewOrderAttachment['id']>;
  name: FormControl<IOrderAttachment['name']>;
  order: FormControl<IOrderAttachment['order']>;
  attachment: FormControl<IOrderAttachment['attachment']>;
};

export type OrderAttachmentFormGroup = FormGroup<OrderAttachmentFormGroupContent>;

@Service()
export class OrderAttachmentFormService {
  createOrderAttachmentFormGroup(orderAttachment?: OrderAttachmentFormGroupInput): OrderAttachmentFormGroup {
    const orderAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(orderAttachment ?? { id: null }),
    };

    return new FormGroup<OrderAttachmentFormGroupContent>({
      id: new FormControl(
        { value: orderAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(orderAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      order: new FormControl(orderAttachmentRawValue.order, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(orderAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getOrderAttachment(form: OrderAttachmentFormGroup): IOrderAttachment | NewOrderAttachment {
    return form.getRawValue();
  }

  resetForm(form: OrderAttachmentFormGroup, orderAttachment: OrderAttachmentFormGroupInput): void {
    const orderAttachmentRawValue = { ...this.getFormDefaults(), ...orderAttachment };
    form.reset({
      ...orderAttachmentRawValue,
      id: { value: orderAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
