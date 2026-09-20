import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFeatureGroup, NewProductFeatureGroup } from '../product-feature-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureGroup for edit and NewProductFeatureGroupFormGroupInput for create.
 */
type ProductFeatureGroupFormGroupInput = IProductFeatureGroup | PartialWithRequiredKeyOf<NewProductFeatureGroup>;

type ProductFeatureGroupFormDefaults = Pick<NewProductFeatureGroup, 'id'>;

type ProductFeatureGroupFormGroupContent = {
  id: FormControl<IProductFeatureGroup['id'] | NewProductFeatureGroup['id']>;
  name: FormControl<IProductFeatureGroup['name']>;
  description: FormControl<IProductFeatureGroup['description']>;
};

export type ProductFeatureGroupFormGroup = FormGroup<ProductFeatureGroupFormGroupContent>;

@Service()
export class ProductFeatureGroupFormService {
  createProductFeatureGroupFormGroup(productFeatureGroup?: ProductFeatureGroupFormGroupInput): ProductFeatureGroupFormGroup {
    const productFeatureGroupRawValue = {
      ...this.getFormDefaults(),
      ...(productFeatureGroup ?? { id: null }),
    };

    return new FormGroup<ProductFeatureGroupFormGroupContent>({
      id: new FormControl(
        { value: productFeatureGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productFeatureGroupRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productFeatureGroupRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductFeatureGroup(form: ProductFeatureGroupFormGroup): IProductFeatureGroup | NewProductFeatureGroup {
    return form.getRawValue();
  }

  resetForm(form: ProductFeatureGroupFormGroup, productFeatureGroup: ProductFeatureGroupFormGroupInput): void {
    const productFeatureGroupRawValue = { ...this.getFormDefaults(), ...productFeatureGroup };
    form.reset({
      ...productFeatureGroupRawValue,
      id: { value: productFeatureGroupRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureGroupFormDefaults {
    return {
      id: null,
    };
  }
}
