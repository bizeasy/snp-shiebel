import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductPriceType, NewProductPriceType } from '../product-price-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductPriceType for edit and NewProductPriceTypeFormGroupInput for create.
 */
type ProductPriceTypeFormGroupInput = IProductPriceType | PartialWithRequiredKeyOf<NewProductPriceType>;

type ProductPriceTypeFormDefaults = Pick<NewProductPriceType, 'id'>;

type ProductPriceTypeFormGroupContent = {
  id: FormControl<IProductPriceType['id'] | NewProductPriceType['id']>;
  name: FormControl<IProductPriceType['name']>;
  description: FormControl<IProductPriceType['description']>;
};

export type ProductPriceTypeFormGroup = FormGroup<ProductPriceTypeFormGroupContent>;

@Service()
export class ProductPriceTypeFormService {
  createProductPriceTypeFormGroup(productPriceType?: ProductPriceTypeFormGroupInput): ProductPriceTypeFormGroup {
    const productPriceTypeRawValue = {
      ...this.getFormDefaults(),
      ...(productPriceType ?? { id: null }),
    };

    return new FormGroup<ProductPriceTypeFormGroupContent>({
      id: new FormControl(
        { value: productPriceTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productPriceTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productPriceTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductPriceType(form: ProductPriceTypeFormGroup): IProductPriceType | NewProductPriceType {
    return form.getRawValue();
  }

  resetForm(form: ProductPriceTypeFormGroup, productPriceType: ProductPriceTypeFormGroupInput): void {
    const productPriceTypeRawValue = { ...this.getFormDefaults(), ...productPriceType };
    form.reset({
      ...productPriceTypeRawValue,
      id: { value: productPriceTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductPriceTypeFormDefaults {
    return {
      id: null,
    };
  }
}
