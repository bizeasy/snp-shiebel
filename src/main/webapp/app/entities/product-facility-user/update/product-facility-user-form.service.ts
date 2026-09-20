import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductFacilityUser, NewProductFacilityUser } from '../product-facility-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFacilityUser for edit and NewProductFacilityUserFormGroupInput for create.
 */
type ProductFacilityUserFormGroupInput = IProductFacilityUser | PartialWithRequiredKeyOf<NewProductFacilityUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductFacilityUser | NewProductFacilityUser> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductFacilityUserFormRawValue = FormValueOf<IProductFacilityUser>;

type NewProductFacilityUserFormRawValue = FormValueOf<NewProductFacilityUser>;

type ProductFacilityUserFormDefaults = Pick<NewProductFacilityUser, 'id' | 'fromDate' | 'thruDate'>;

type ProductFacilityUserFormGroupContent = {
  id: FormControl<ProductFacilityUserFormRawValue['id'] | NewProductFacilityUser['id']>;
  fromDate: FormControl<ProductFacilityUserFormRawValue['fromDate']>;
  thruDate: FormControl<ProductFacilityUserFormRawValue['thruDate']>;
  costPerDay: FormControl<ProductFacilityUserFormRawValue['costPerDay']>;
  totalCost: FormControl<ProductFacilityUserFormRawValue['totalCost']>;
  discount: FormControl<ProductFacilityUserFormRawValue['discount']>;
  productFacility: FormControl<ProductFacilityUserFormRawValue['productFacility']>;
  party: FormControl<ProductFacilityUserFormRawValue['party']>;
  status: FormControl<ProductFacilityUserFormRawValue['status']>;
};

export type ProductFacilityUserFormGroup = FormGroup<ProductFacilityUserFormGroupContent>;

@Service()
export class ProductFacilityUserFormService {
  createProductFacilityUserFormGroup(productFacilityUser?: ProductFacilityUserFormGroupInput): ProductFacilityUserFormGroup {
    const productFacilityUserRawValue = this.convertProductFacilityUserToProductFacilityUserRawValue({
      ...this.getFormDefaults(),
      ...(productFacilityUser ?? { id: null }),
    });

    return new FormGroup<ProductFacilityUserFormGroupContent>({
      id: new FormControl(
        { value: productFacilityUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productFacilityUserRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productFacilityUserRawValue.thruDate),
      costPerDay: new FormControl(productFacilityUserRawValue.costPerDay),
      totalCost: new FormControl(productFacilityUserRawValue.totalCost),
      discount: new FormControl(productFacilityUserRawValue.discount),
      productFacility: new FormControl(productFacilityUserRawValue.productFacility, {
        validators: [Validators.required],
      }),
      party: new FormControl(productFacilityUserRawValue.party, {
        validators: [Validators.required],
      }),
      status: new FormControl(productFacilityUserRawValue.status),
    });
  }

  getProductFacilityUser(form: ProductFacilityUserFormGroup): IProductFacilityUser | NewProductFacilityUser {
    return this.convertProductFacilityUserRawValueToProductFacilityUser(form.getRawValue());
  }

  resetForm(form: ProductFacilityUserFormGroup, productFacilityUser: ProductFacilityUserFormGroupInput): void {
    const productFacilityUserRawValue = this.convertProductFacilityUserToProductFacilityUserRawValue({
      ...this.getFormDefaults(),
      ...productFacilityUser,
    });
    form.reset({
      ...productFacilityUserRawValue,
      id: { value: productFacilityUserRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFacilityUserFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductFacilityUserRawValueToProductFacilityUser(
    rawProductFacilityUser: ProductFacilityUserFormRawValue | NewProductFacilityUserFormRawValue,
  ): IProductFacilityUser | NewProductFacilityUser {
    return {
      ...rawProductFacilityUser,
      fromDate: dayjs(rawProductFacilityUser.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductFacilityUser.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductFacilityUserToProductFacilityUserRawValue(
    productFacilityUser: IProductFacilityUser | (Partial<NewProductFacilityUser> & ProductFacilityUserFormDefaults),
  ): ProductFacilityUserFormRawValue | PartialWithRequiredKeyOf<NewProductFacilityUserFormRawValue> {
    return {
      ...productFacilityUser,
      fromDate: productFacilityUser.fromDate ? productFacilityUser.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productFacilityUser.thruDate ? productFacilityUser.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
