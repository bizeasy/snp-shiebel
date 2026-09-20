import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderItemAttachment, NewOrderItemAttachment } from '../order-item-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderItemAttachment for edit and NewOrderItemAttachmentFormGroupInput for create.
 */
type OrderItemAttachmentFormGroupInput = IOrderItemAttachment | PartialWithRequiredKeyOf<NewOrderItemAttachment>;

type OrderItemAttachmentFormDefaults = Pick<NewOrderItemAttachment, 'id'>;

type OrderItemAttachmentFormGroupContent = {
  id: FormControl<IOrderItemAttachment['id'] | NewOrderItemAttachment['id']>;
  name: FormControl<IOrderItemAttachment['name']>;
  orderItem: FormControl<IOrderItemAttachment['orderItem']>;
  attachment: FormControl<IOrderItemAttachment['attachment']>;
};

export type OrderItemAttachmentFormGroup = FormGroup<OrderItemAttachmentFormGroupContent>;

@Service()
export class OrderItemAttachmentFormService {
  createOrderItemAttachmentFormGroup(orderItemAttachment?: OrderItemAttachmentFormGroupInput): OrderItemAttachmentFormGroup {
    const orderItemAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(orderItemAttachment ?? { id: null }),
    };

    return new FormGroup<OrderItemAttachmentFormGroupContent>({
      id: new FormControl(
        { value: orderItemAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(orderItemAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      orderItem: new FormControl(orderItemAttachmentRawValue.orderItem, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(orderItemAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getOrderItemAttachment(form: OrderItemAttachmentFormGroup): IOrderItemAttachment | NewOrderItemAttachment {
    return form.getRawValue();
  }

  resetForm(form: OrderItemAttachmentFormGroup, orderItemAttachment: OrderItemAttachmentFormGroupInput): void {
    const orderItemAttachmentRawValue = { ...this.getFormDefaults(), ...orderItemAttachment };
    form.reset({
      ...orderItemAttachmentRawValue,
      id: { value: orderItemAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderItemAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
