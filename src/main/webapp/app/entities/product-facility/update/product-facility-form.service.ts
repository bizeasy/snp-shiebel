import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFacility, NewProductFacility } from '../product-facility.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFacility for edit and NewProductFacilityFormGroupInput for create.
 */
type ProductFacilityFormGroupInput = IProductFacility | PartialWithRequiredKeyOf<NewProductFacility>;

type ProductFacilityFormDefaults = Pick<NewProductFacility, 'id'>;

type ProductFacilityFormGroupContent = {
  id: FormControl<IProductFacility['id'] | NewProductFacility['id']>;
  minimumStock: FormControl<IProductFacility['minimumStock']>;
  reorderQty: FormControl<IProductFacility['reorderQty']>;
  daysToShip: FormControl<IProductFacility['daysToShip']>;
  lastInventoryCount: FormControl<IProductFacility['lastInventoryCount']>;
  product: FormControl<IProductFacility['product']>;
  facility: FormControl<IProductFacility['facility']>;
};

export type ProductFacilityFormGroup = FormGroup<ProductFacilityFormGroupContent>;

@Service()
export class ProductFacilityFormService {
  createProductFacilityFormGroup(productFacility?: ProductFacilityFormGroupInput): ProductFacilityFormGroup {
    const productFacilityRawValue = {
      ...this.getFormDefaults(),
      ...(productFacility ?? { id: null }),
    };

    return new FormGroup<ProductFacilityFormGroupContent>({
      id: new FormControl(
        { value: productFacilityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      minimumStock: new FormControl(productFacilityRawValue.minimumStock),
      reorderQty: new FormControl(productFacilityRawValue.reorderQty),
      daysToShip: new FormControl(productFacilityRawValue.daysToShip),
      lastInventoryCount: new FormControl(productFacilityRawValue.lastInventoryCount),
      product: new FormControl(productFacilityRawValue.product, {
        validators: [Validators.required],
      }),
      facility: new FormControl(productFacilityRawValue.facility, {
        validators: [Validators.required],
      }),
    });
  }

  getProductFacility(form: ProductFacilityFormGroup): IProductFacility | NewProductFacility {
    return form.getRawValue();
  }

  resetForm(form: ProductFacilityFormGroup, productFacility: ProductFacilityFormGroupInput): void {
    const productFacilityRawValue = { ...this.getFormDefaults(), ...productFacility };
    form.reset({
      ...productFacilityRawValue,
      id: { value: productFacilityRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFacilityFormDefaults {
    return {
      id: null,
    };
  }
}
