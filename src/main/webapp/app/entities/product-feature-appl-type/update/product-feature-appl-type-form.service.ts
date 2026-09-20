import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFeatureApplType, NewProductFeatureApplType } from '../product-feature-appl-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureApplType for edit and NewProductFeatureApplTypeFormGroupInput for create.
 */
type ProductFeatureApplTypeFormGroupInput = IProductFeatureApplType | PartialWithRequiredKeyOf<NewProductFeatureApplType>;

type ProductFeatureApplTypeFormDefaults = Pick<NewProductFeatureApplType, 'id'>;

type ProductFeatureApplTypeFormGroupContent = {
  id: FormControl<IProductFeatureApplType['id'] | NewProductFeatureApplType['id']>;
  name: FormControl<IProductFeatureApplType['name']>;
  description: FormControl<IProductFeatureApplType['description']>;
};

export type ProductFeatureApplTypeFormGroup = FormGroup<ProductFeatureApplTypeFormGroupContent>;

@Service()
export class ProductFeatureApplTypeFormService {
  createProductFeatureApplTypeFormGroup(productFeatureApplType?: ProductFeatureApplTypeFormGroupInput): ProductFeatureApplTypeFormGroup {
    const productFeatureApplTypeRawValue = {
      ...this.getFormDefaults(),
      ...(productFeatureApplType ?? { id: null }),
    };

    return new FormGroup<ProductFeatureApplTypeFormGroupContent>({
      id: new FormControl(
        { value: productFeatureApplTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productFeatureApplTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productFeatureApplTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductFeatureApplType(form: ProductFeatureApplTypeFormGroup): IProductFeatureApplType | NewProductFeatureApplType {
    return form.getRawValue();
  }

  resetForm(form: ProductFeatureApplTypeFormGroup, productFeatureApplType: ProductFeatureApplTypeFormGroupInput): void {
    const productFeatureApplTypeRawValue = { ...this.getFormDefaults(), ...productFeatureApplType };
    form.reset({
      ...productFeatureApplTypeRawValue,
      id: { value: productFeatureApplTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureApplTypeFormDefaults {
    return {
      id: null,
    };
  }
}
