import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductPricePurpose, NewProductPricePurpose } from '../product-price-purpose.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductPricePurpose for edit and NewProductPricePurposeFormGroupInput for create.
 */
type ProductPricePurposeFormGroupInput = IProductPricePurpose | PartialWithRequiredKeyOf<NewProductPricePurpose>;

type ProductPricePurposeFormDefaults = Pick<NewProductPricePurpose, 'id'>;

type ProductPricePurposeFormGroupContent = {
  id: FormControl<IProductPricePurpose['id'] | NewProductPricePurpose['id']>;
  name: FormControl<IProductPricePurpose['name']>;
  description: FormControl<IProductPricePurpose['description']>;
};

export type ProductPricePurposeFormGroup = FormGroup<ProductPricePurposeFormGroupContent>;

@Service()
export class ProductPricePurposeFormService {
  createProductPricePurposeFormGroup(productPricePurpose?: ProductPricePurposeFormGroupInput): ProductPricePurposeFormGroup {
    const productPricePurposeRawValue = {
      ...this.getFormDefaults(),
      ...(productPricePurpose ?? { id: null }),
    };

    return new FormGroup<ProductPricePurposeFormGroupContent>({
      id: new FormControl(
        { value: productPricePurposeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productPricePurposeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productPricePurposeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductPricePurpose(form: ProductPricePurposeFormGroup): IProductPricePurpose | NewProductPricePurpose {
    return form.getRawValue();
  }

  resetForm(form: ProductPricePurposeFormGroup, productPricePurpose: ProductPricePurposeFormGroupInput): void {
    const productPricePurposeRawValue = { ...this.getFormDefaults(), ...productPricePurpose };
    form.reset({
      ...productPricePurposeRawValue,
      id: { value: productPricePurposeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductPricePurposeFormDefaults {
    return {
      id: null,
    };
  }
}
