import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFeatureCategory, NewProductFeatureCategory } from '../product-feature-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureCategory for edit and NewProductFeatureCategoryFormGroupInput for create.
 */
type ProductFeatureCategoryFormGroupInput = IProductFeatureCategory | PartialWithRequiredKeyOf<NewProductFeatureCategory>;

type ProductFeatureCategoryFormDefaults = Pick<NewProductFeatureCategory, 'id'>;

type ProductFeatureCategoryFormGroupContent = {
  id: FormControl<IProductFeatureCategory['id'] | NewProductFeatureCategory['id']>;
  name: FormControl<IProductFeatureCategory['name']>;
  description: FormControl<IProductFeatureCategory['description']>;
};

export type ProductFeatureCategoryFormGroup = FormGroup<ProductFeatureCategoryFormGroupContent>;

@Service()
export class ProductFeatureCategoryFormService {
  createProductFeatureCategoryFormGroup(productFeatureCategory?: ProductFeatureCategoryFormGroupInput): ProductFeatureCategoryFormGroup {
    const productFeatureCategoryRawValue = {
      ...this.getFormDefaults(),
      ...(productFeatureCategory ?? { id: null }),
    };

    return new FormGroup<ProductFeatureCategoryFormGroupContent>({
      id: new FormControl(
        { value: productFeatureCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productFeatureCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productFeatureCategoryRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductFeatureCategory(form: ProductFeatureCategoryFormGroup): IProductFeatureCategory | NewProductFeatureCategory {
    return form.getRawValue();
  }

  resetForm(form: ProductFeatureCategoryFormGroup, productFeatureCategory: ProductFeatureCategoryFormGroupInput): void {
    const productFeatureCategoryRawValue = { ...this.getFormDefaults(), ...productFeatureCategory };
    form.reset({
      ...productFeatureCategoryRawValue,
      id: { value: productFeatureCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
