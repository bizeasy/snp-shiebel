import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFeature, NewProductFeature } from '../product-feature.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeature for edit and NewProductFeatureFormGroupInput for create.
 */
type ProductFeatureFormGroupInput = IProductFeature | PartialWithRequiredKeyOf<NewProductFeature>;

type ProductFeatureFormDefaults = Pick<NewProductFeature, 'id'>;

type ProductFeatureFormGroupContent = {
  id: FormControl<IProductFeature['id'] | NewProductFeature['id']>;
  name: FormControl<IProductFeature['name']>;
  description: FormControl<IProductFeature['description']>;
  amount: FormControl<IProductFeature['amount']>;
  sequenceNo: FormControl<IProductFeature['sequenceNo']>;
  abbrev: FormControl<IProductFeature['abbrev']>;
  productFeatureType: FormControl<IProductFeature['productFeatureType']>;
  productFeatureCategory: FormControl<IProductFeature['productFeatureCategory']>;
};

export type ProductFeatureFormGroup = FormGroup<ProductFeatureFormGroupContent>;

@Service()
export class ProductFeatureFormService {
  createProductFeatureFormGroup(productFeature?: ProductFeatureFormGroupInput): ProductFeatureFormGroup {
    const productFeatureRawValue = {
      ...this.getFormDefaults(),
      ...(productFeature ?? { id: null }),
    };

    return new FormGroup<ProductFeatureFormGroupContent>({
      id: new FormControl(
        { value: productFeatureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productFeatureRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productFeatureRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      amount: new FormControl(productFeatureRawValue.amount),
      sequenceNo: new FormControl(productFeatureRawValue.sequenceNo),
      abbrev: new FormControl(productFeatureRawValue.abbrev, {
        validators: [Validators.maxLength(20)],
      }),
      productFeatureType: new FormControl(productFeatureRawValue.productFeatureType),
      productFeatureCategory: new FormControl(productFeatureRawValue.productFeatureCategory),
    });
  }

  getProductFeature(form: ProductFeatureFormGroup): IProductFeature | NewProductFeature {
    return form.getRawValue();
  }

  resetForm(form: ProductFeatureFormGroup, productFeature: ProductFeatureFormGroupInput): void {
    const productFeatureRawValue = { ...this.getFormDefaults(), ...productFeature };
    form.reset({
      ...productFeatureRawValue,
      id: { value: productFeatureRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureFormDefaults {
    return {
      id: null,
    };
  }
}
