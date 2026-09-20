import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductCategoryType, NewProductCategoryType } from '../product-category-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductCategoryType for edit and NewProductCategoryTypeFormGroupInput for create.
 */
type ProductCategoryTypeFormGroupInput = IProductCategoryType | PartialWithRequiredKeyOf<NewProductCategoryType>;

type ProductCategoryTypeFormDefaults = Pick<NewProductCategoryType, 'id'>;

type ProductCategoryTypeFormGroupContent = {
  id: FormControl<IProductCategoryType['id'] | NewProductCategoryType['id']>;
  name: FormControl<IProductCategoryType['name']>;
  description: FormControl<IProductCategoryType['description']>;
};

export type ProductCategoryTypeFormGroup = FormGroup<ProductCategoryTypeFormGroupContent>;

@Service()
export class ProductCategoryTypeFormService {
  createProductCategoryTypeFormGroup(productCategoryType?: ProductCategoryTypeFormGroupInput): ProductCategoryTypeFormGroup {
    const productCategoryTypeRawValue = {
      ...this.getFormDefaults(),
      ...(productCategoryType ?? { id: null }),
    };

    return new FormGroup<ProductCategoryTypeFormGroupContent>({
      id: new FormControl(
        { value: productCategoryTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productCategoryTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productCategoryTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductCategoryType(form: ProductCategoryTypeFormGroup): IProductCategoryType | NewProductCategoryType {
    return form.getRawValue();
  }

  resetForm(form: ProductCategoryTypeFormGroup, productCategoryType: ProductCategoryTypeFormGroupInput): void {
    const productCategoryTypeRawValue = { ...this.getFormDefaults(), ...productCategoryType };
    form.reset({
      ...productCategoryTypeRawValue,
      id: { value: productCategoryTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductCategoryTypeFormDefaults {
    return {
      id: null,
    };
  }
}
