import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductCategoryMember, NewProductCategoryMember } from '../product-category-member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductCategoryMember for edit and NewProductCategoryMemberFormGroupInput for create.
 */
type ProductCategoryMemberFormGroupInput = IProductCategoryMember | PartialWithRequiredKeyOf<NewProductCategoryMember>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductCategoryMember | NewProductCategoryMember> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductCategoryMemberFormRawValue = FormValueOf<IProductCategoryMember>;

type NewProductCategoryMemberFormRawValue = FormValueOf<NewProductCategoryMember>;

type ProductCategoryMemberFormDefaults = Pick<NewProductCategoryMember, 'id' | 'fromDate' | 'thruDate'>;

type ProductCategoryMemberFormGroupContent = {
  id: FormControl<ProductCategoryMemberFormRawValue['id'] | NewProductCategoryMember['id']>;
  fromDate: FormControl<ProductCategoryMemberFormRawValue['fromDate']>;
  thruDate: FormControl<ProductCategoryMemberFormRawValue['thruDate']>;
  sequenceNo: FormControl<ProductCategoryMemberFormRawValue['sequenceNo']>;
  product: FormControl<ProductCategoryMemberFormRawValue['product']>;
  productCategory: FormControl<ProductCategoryMemberFormRawValue['productCategory']>;
};

export type ProductCategoryMemberFormGroup = FormGroup<ProductCategoryMemberFormGroupContent>;

@Service()
export class ProductCategoryMemberFormService {
  createProductCategoryMemberFormGroup(productCategoryMember?: ProductCategoryMemberFormGroupInput): ProductCategoryMemberFormGroup {
    const productCategoryMemberRawValue = this.convertProductCategoryMemberToProductCategoryMemberRawValue({
      ...this.getFormDefaults(),
      ...(productCategoryMember ?? { id: null }),
    });

    return new FormGroup<ProductCategoryMemberFormGroupContent>({
      id: new FormControl(
        { value: productCategoryMemberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productCategoryMemberRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productCategoryMemberRawValue.thruDate),
      sequenceNo: new FormControl(productCategoryMemberRawValue.sequenceNo),
      product: new FormControl(productCategoryMemberRawValue.product, {
        validators: [Validators.required],
      }),
      productCategory: new FormControl(productCategoryMemberRawValue.productCategory, {
        validators: [Validators.required],
      }),
    });
  }

  getProductCategoryMember(form: ProductCategoryMemberFormGroup): IProductCategoryMember | NewProductCategoryMember {
    return this.convertProductCategoryMemberRawValueToProductCategoryMember(form.getRawValue());
  }

  resetForm(form: ProductCategoryMemberFormGroup, productCategoryMember: ProductCategoryMemberFormGroupInput): void {
    const productCategoryMemberRawValue = this.convertProductCategoryMemberToProductCategoryMemberRawValue({
      ...this.getFormDefaults(),
      ...productCategoryMember,
    });
    form.reset({
      ...productCategoryMemberRawValue,
      id: { value: productCategoryMemberRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductCategoryMemberFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductCategoryMemberRawValueToProductCategoryMember(
    rawProductCategoryMember: ProductCategoryMemberFormRawValue | NewProductCategoryMemberFormRawValue,
  ): IProductCategoryMember | NewProductCategoryMember {
    return {
      ...rawProductCategoryMember,
      fromDate: dayjs(rawProductCategoryMember.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductCategoryMember.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductCategoryMemberToProductCategoryMemberRawValue(
    productCategoryMember: IProductCategoryMember | (Partial<NewProductCategoryMember> & ProductCategoryMemberFormDefaults),
  ): ProductCategoryMemberFormRawValue | PartialWithRequiredKeyOf<NewProductCategoryMemberFormRawValue> {
    return {
      ...productCategoryMember,
      fromDate: productCategoryMember.fromDate ? productCategoryMember.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productCategoryMember.thruDate ? productCategoryMember.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
