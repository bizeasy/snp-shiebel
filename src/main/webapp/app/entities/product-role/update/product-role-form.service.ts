import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductRole, NewProductRole } from '../product-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductRole for edit and NewProductRoleFormGroupInput for create.
 */
type ProductRoleFormGroupInput = IProductRole | PartialWithRequiredKeyOf<NewProductRole>;

type ProductRoleFormDefaults = Pick<NewProductRole, 'id'>;

type ProductRoleFormGroupContent = {
  id: FormControl<IProductRole['id'] | NewProductRole['id']>;
  product: FormControl<IProductRole['product']>;
  party: FormControl<IProductRole['party']>;
  roleType: FormControl<IProductRole['roleType']>;
};

export type ProductRoleFormGroup = FormGroup<ProductRoleFormGroupContent>;

@Service()
export class ProductRoleFormService {
  createProductRoleFormGroup(productRole?: ProductRoleFormGroupInput): ProductRoleFormGroup {
    const productRoleRawValue = {
      ...this.getFormDefaults(),
      ...(productRole ?? { id: null }),
    };

    return new FormGroup<ProductRoleFormGroupContent>({
      id: new FormControl(
        { value: productRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      product: new FormControl(productRoleRawValue.product, {
        validators: [Validators.required],
      }),
      party: new FormControl(productRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(productRoleRawValue.roleType),
    });
  }

  getProductRole(form: ProductRoleFormGroup): IProductRole | NewProductRole {
    return form.getRawValue();
  }

  resetForm(form: ProductRoleFormGroup, productRole: ProductRoleFormGroupInput): void {
    const productRoleRawValue = { ...this.getFormDefaults(), ...productRole };
    form.reset({
      ...productRoleRawValue,
      id: { value: productRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductRoleFormDefaults {
    return {
      id: null,
    };
  }
}
