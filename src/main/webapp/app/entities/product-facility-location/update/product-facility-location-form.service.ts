import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductFacilityLocation, NewProductFacilityLocation } from '../product-facility-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFacilityLocation for edit and NewProductFacilityLocationFormGroupInput for create.
 */
type ProductFacilityLocationFormGroupInput = IProductFacilityLocation | PartialWithRequiredKeyOf<NewProductFacilityLocation>;

type ProductFacilityLocationFormDefaults = Pick<NewProductFacilityLocation, 'id'>;

type ProductFacilityLocationFormGroupContent = {
  id: FormControl<IProductFacilityLocation['id'] | NewProductFacilityLocation['id']>;
  minimumStock: FormControl<IProductFacilityLocation['minimumStock']>;
  moveQuantity: FormControl<IProductFacilityLocation['moveQuantity']>;
  product: FormControl<IProductFacilityLocation['product']>;
  facility: FormControl<IProductFacilityLocation['facility']>;
  facilityLocation: FormControl<IProductFacilityLocation['facilityLocation']>;
};

export type ProductFacilityLocationFormGroup = FormGroup<ProductFacilityLocationFormGroupContent>;

@Service()
export class ProductFacilityLocationFormService {
  createProductFacilityLocationFormGroup(
    productFacilityLocation?: ProductFacilityLocationFormGroupInput,
  ): ProductFacilityLocationFormGroup {
    const productFacilityLocationRawValue = {
      ...this.getFormDefaults(),
      ...(productFacilityLocation ?? { id: null }),
    };

    return new FormGroup<ProductFacilityLocationFormGroupContent>({
      id: new FormControl(
        { value: productFacilityLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      minimumStock: new FormControl(productFacilityLocationRawValue.minimumStock),
      moveQuantity: new FormControl(productFacilityLocationRawValue.moveQuantity),
      product: new FormControl(productFacilityLocationRawValue.product, {
        validators: [Validators.required],
      }),
      facility: new FormControl(productFacilityLocationRawValue.facility, {
        validators: [Validators.required],
      }),
      facilityLocation: new FormControl(productFacilityLocationRawValue.facilityLocation, {
        validators: [Validators.required],
      }),
    });
  }

  getProductFacilityLocation(form: ProductFacilityLocationFormGroup): IProductFacilityLocation | NewProductFacilityLocation {
    return form.getRawValue();
  }

  resetForm(form: ProductFacilityLocationFormGroup, productFacilityLocation: ProductFacilityLocationFormGroupInput): void {
    const productFacilityLocationRawValue = { ...this.getFormDefaults(), ...productFacilityLocation };
    form.reset({
      ...productFacilityLocationRawValue,
      id: { value: productFacilityLocationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFacilityLocationFormDefaults {
    return {
      id: null,
    };
  }
}
