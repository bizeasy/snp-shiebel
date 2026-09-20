import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductAssocType, NewProductAssocType } from '../product-assoc-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductAssocType for edit and NewProductAssocTypeFormGroupInput for create.
 */
type ProductAssocTypeFormGroupInput = IProductAssocType | PartialWithRequiredKeyOf<NewProductAssocType>;

type ProductAssocTypeFormDefaults = Pick<NewProductAssocType, 'id'>;

type ProductAssocTypeFormGroupContent = {
  id: FormControl<IProductAssocType['id'] | NewProductAssocType['id']>;
  name: FormControl<IProductAssocType['name']>;
  description: FormControl<IProductAssocType['description']>;
};

export type ProductAssocTypeFormGroup = FormGroup<ProductAssocTypeFormGroupContent>;

@Service()
export class ProductAssocTypeFormService {
  createProductAssocTypeFormGroup(productAssocType?: ProductAssocTypeFormGroupInput): ProductAssocTypeFormGroup {
    const productAssocTypeRawValue = {
      ...this.getFormDefaults(),
      ...(productAssocType ?? { id: null }),
    };

    return new FormGroup<ProductAssocTypeFormGroupContent>({
      id: new FormControl(
        { value: productAssocTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productAssocTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productAssocTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getProductAssocType(form: ProductAssocTypeFormGroup): IProductAssocType | NewProductAssocType {
    return form.getRawValue();
  }

  resetForm(form: ProductAssocTypeFormGroup, productAssocType: ProductAssocTypeFormGroupInput): void {
    const productAssocTypeRawValue = { ...this.getFormDefaults(), ...productAssocType };
    form.reset({
      ...productAssocTypeRawValue,
      id: { value: productAssocTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductAssocTypeFormDefaults {
    return {
      id: null,
    };
  }
}
