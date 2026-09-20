import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductStore, NewProductStore } from '../product-store.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductStore for edit and NewProductStoreFormGroupInput for create.
 */
type ProductStoreFormGroupInput = IProductStore | PartialWithRequiredKeyOf<NewProductStore>;

type ProductStoreFormDefaults = Pick<NewProductStore, 'id'>;

type ProductStoreFormGroupContent = {
  id: FormControl<IProductStore['id'] | NewProductStore['id']>;
  name: FormControl<IProductStore['name']>;
  title: FormControl<IProductStore['title']>;
  postalAddress: FormControl<IProductStore['postalAddress']>;
  owner: FormControl<IProductStore['owner']>;
  admin: FormControl<IProductStore['admin']>;
};

export type ProductStoreFormGroup = FormGroup<ProductStoreFormGroupContent>;

@Service()
export class ProductStoreFormService {
  createProductStoreFormGroup(productStore?: ProductStoreFormGroupInput): ProductStoreFormGroup {
    const productStoreRawValue = {
      ...this.getFormDefaults(),
      ...(productStore ?? { id: null }),
    };

    return new FormGroup<ProductStoreFormGroupContent>({
      id: new FormControl(
        { value: productStoreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productStoreRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      title: new FormControl(productStoreRawValue.title, {
        validators: [Validators.maxLength(100)],
      }),
      postalAddress: new FormControl(productStoreRawValue.postalAddress),
      owner: new FormControl(productStoreRawValue.owner),
      admin: new FormControl(productStoreRawValue.admin),
    });
  }

  getProductStore(form: ProductStoreFormGroup): IProductStore | NewProductStore {
    return form.getRawValue();
  }

  resetForm(form: ProductStoreFormGroup, productStore: ProductStoreFormGroupInput): void {
    const productStoreRawValue = { ...this.getFormDefaults(), ...productStore };
    form.reset({
      ...productStoreRawValue,
      id: { value: productStoreRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductStoreFormDefaults {
    return {
      id: null,
    };
  }
}
