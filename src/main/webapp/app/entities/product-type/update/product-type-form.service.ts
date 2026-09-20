import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductType, NewProductType } from '../product-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductType for edit and NewProductTypeFormGroupInput for create.
 */
type ProductTypeFormGroupInput = IProductType | PartialWithRequiredKeyOf<NewProductType>;

type ProductTypeFormDefaults = Pick<NewProductType, 'id'>;

type ProductTypeFormGroupContent = {
  id: FormControl<IProductType['id'] | NewProductType['id']>;
  name: FormControl<IProductType['name']>;
  description: FormControl<IProductType['description']>;
};

export type ProductTypeFormGroup = FormGroup<ProductTypeFormGroupContent>;

@Service()
export class ProductTypeFormService {
  createProductTypeFormGroup(productType?: ProductTypeFormGroupInput): ProductTypeFormGroup {
    const productTypeRawValue = {
      ...this.getFormDefaults(),
      ...(productType ?? { id: null }),
    };

    return new FormGroup<ProductTypeFormGroupContent>({
      id: new FormControl(
        { value: productTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductType(form: ProductTypeFormGroup): IProductType | NewProductType {
    return form.getRawValue();
  }

  resetForm(form: ProductTypeFormGroup, productType: ProductTypeFormGroupInput): void {
    const productTypeRawValue = { ...this.getFormDefaults(), ...productType };
    form.reset({
      ...productTypeRawValue,
      id: { value: productTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductTypeFormDefaults {
    return {
      id: null,
    };
  }
}
