import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFeatureType, NewProductFeatureType } from '../product-feature-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureType for edit and NewProductFeatureTypeFormGroupInput for create.
 */
type ProductFeatureTypeFormGroupInput = IProductFeatureType | PartialWithRequiredKeyOf<NewProductFeatureType>;

type ProductFeatureTypeFormDefaults = Pick<NewProductFeatureType, 'id'>;

type ProductFeatureTypeFormGroupContent = {
  id: FormControl<IProductFeatureType['id'] | NewProductFeatureType['id']>;
  name: FormControl<IProductFeatureType['name']>;
  description: FormControl<IProductFeatureType['description']>;
};

export type ProductFeatureTypeFormGroup = FormGroup<ProductFeatureTypeFormGroupContent>;

@Service()
export class ProductFeatureTypeFormService {
  createProductFeatureTypeFormGroup(productFeatureType?: ProductFeatureTypeFormGroupInput): ProductFeatureTypeFormGroup {
    const productFeatureTypeRawValue = {
      ...this.getFormDefaults(),
      ...(productFeatureType ?? { id: null }),
    };

    return new FormGroup<ProductFeatureTypeFormGroupContent>({
      id: new FormControl(
        { value: productFeatureTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productFeatureTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productFeatureTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductFeatureType(form: ProductFeatureTypeFormGroup): IProductFeatureType | NewProductFeatureType {
    return form.getRawValue();
  }

  resetForm(form: ProductFeatureTypeFormGroup, productFeatureType: ProductFeatureTypeFormGroupInput): void {
    const productFeatureTypeRawValue = { ...this.getFormDefaults(), ...productFeatureType };
    form.reset({
      ...productFeatureTypeRawValue,
      id: { value: productFeatureTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureTypeFormDefaults {
    return {
      id: null,
    };
  }
}
