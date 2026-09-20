import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductAttachment, NewProductAttachment } from '../product-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductAttachment for edit and NewProductAttachmentFormGroupInput for create.
 */
type ProductAttachmentFormGroupInput = IProductAttachment | PartialWithRequiredKeyOf<NewProductAttachment>;

type ProductAttachmentFormDefaults = Pick<NewProductAttachment, 'id'>;

type ProductAttachmentFormGroupContent = {
  id: FormControl<IProductAttachment['id'] | NewProductAttachment['id']>;
  name: FormControl<IProductAttachment['name']>;
  product: FormControl<IProductAttachment['product']>;
  attachment: FormControl<IProductAttachment['attachment']>;
};

export type ProductAttachmentFormGroup = FormGroup<ProductAttachmentFormGroupContent>;

@Service()
export class ProductAttachmentFormService {
  createProductAttachmentFormGroup(productAttachment?: ProductAttachmentFormGroupInput): ProductAttachmentFormGroup {
    const productAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(productAttachment ?? { id: null }),
    };

    return new FormGroup<ProductAttachmentFormGroupContent>({
      id: new FormControl(
        { value: productAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      product: new FormControl(productAttachmentRawValue.product, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(productAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getProductAttachment(form: ProductAttachmentFormGroup): IProductAttachment | NewProductAttachment {
    return form.getRawValue();
  }

  resetForm(form: ProductAttachmentFormGroup, productAttachment: ProductAttachmentFormGroupInput): void {
    const productAttachmentRawValue = { ...this.getFormDefaults(), ...productAttachment };
    form.reset({
      ...productAttachmentRawValue,
      id: { value: productAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
