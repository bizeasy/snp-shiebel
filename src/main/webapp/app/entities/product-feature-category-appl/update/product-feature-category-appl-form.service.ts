import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductFeatureCategoryAppl, NewProductFeatureCategoryAppl } from '../product-feature-category-appl.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureCategoryAppl for edit and NewProductFeatureCategoryApplFormGroupInput for create.
 */
type ProductFeatureCategoryApplFormGroupInput = IProductFeatureCategoryAppl | PartialWithRequiredKeyOf<NewProductFeatureCategoryAppl>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductFeatureCategoryAppl | NewProductFeatureCategoryAppl> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductFeatureCategoryApplFormRawValue = FormValueOf<IProductFeatureCategoryAppl>;

type NewProductFeatureCategoryApplFormRawValue = FormValueOf<NewProductFeatureCategoryAppl>;

type ProductFeatureCategoryApplFormDefaults = Pick<NewProductFeatureCategoryAppl, 'id' | 'fromDate' | 'thruDate'>;

type ProductFeatureCategoryApplFormGroupContent = {
  id: FormControl<ProductFeatureCategoryApplFormRawValue['id'] | NewProductFeatureCategoryAppl['id']>;
  fromDate: FormControl<ProductFeatureCategoryApplFormRawValue['fromDate']>;
  thruDate: FormControl<ProductFeatureCategoryApplFormRawValue['thruDate']>;
  productCategory: FormControl<ProductFeatureCategoryApplFormRawValue['productCategory']>;
  productFeatureCategory: FormControl<ProductFeatureCategoryApplFormRawValue['productFeatureCategory']>;
};

export type ProductFeatureCategoryApplFormGroup = FormGroup<ProductFeatureCategoryApplFormGroupContent>;

@Service()
export class ProductFeatureCategoryApplFormService {
  createProductFeatureCategoryApplFormGroup(
    productFeatureCategoryAppl?: ProductFeatureCategoryApplFormGroupInput,
  ): ProductFeatureCategoryApplFormGroup {
    const productFeatureCategoryApplRawValue = this.convertProductFeatureCategoryApplToProductFeatureCategoryApplRawValue({
      ...this.getFormDefaults(),
      ...(productFeatureCategoryAppl ?? { id: null }),
    });

    return new FormGroup<ProductFeatureCategoryApplFormGroupContent>({
      id: new FormControl(
        { value: productFeatureCategoryApplRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productFeatureCategoryApplRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productFeatureCategoryApplRawValue.thruDate),
      productCategory: new FormControl(productFeatureCategoryApplRawValue.productCategory, {
        validators: [Validators.required],
      }),
      productFeatureCategory: new FormControl(productFeatureCategoryApplRawValue.productFeatureCategory, {
        validators: [Validators.required],
      }),
    });
  }

  getProductFeatureCategoryAppl(form: ProductFeatureCategoryApplFormGroup): IProductFeatureCategoryAppl | NewProductFeatureCategoryAppl {
    return this.convertProductFeatureCategoryApplRawValueToProductFeatureCategoryAppl(form.getRawValue());
  }

  resetForm(form: ProductFeatureCategoryApplFormGroup, productFeatureCategoryAppl: ProductFeatureCategoryApplFormGroupInput): void {
    const productFeatureCategoryApplRawValue = this.convertProductFeatureCategoryApplToProductFeatureCategoryApplRawValue({
      ...this.getFormDefaults(),
      ...productFeatureCategoryAppl,
    });
    form.reset({
      ...productFeatureCategoryApplRawValue,
      id: { value: productFeatureCategoryApplRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureCategoryApplFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductFeatureCategoryApplRawValueToProductFeatureCategoryAppl(
    rawProductFeatureCategoryAppl: ProductFeatureCategoryApplFormRawValue | NewProductFeatureCategoryApplFormRawValue,
  ): IProductFeatureCategoryAppl | NewProductFeatureCategoryAppl {
    return {
      ...rawProductFeatureCategoryAppl,
      fromDate: dayjs(rawProductFeatureCategoryAppl.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductFeatureCategoryAppl.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductFeatureCategoryApplToProductFeatureCategoryApplRawValue(
    productFeatureCategoryAppl:
      IProductFeatureCategoryAppl | (Partial<NewProductFeatureCategoryAppl> & ProductFeatureCategoryApplFormDefaults),
  ): ProductFeatureCategoryApplFormRawValue | PartialWithRequiredKeyOf<NewProductFeatureCategoryApplFormRawValue> {
    return {
      ...productFeatureCategoryAppl,
      fromDate: productFeatureCategoryAppl.fromDate ? productFeatureCategoryAppl.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productFeatureCategoryAppl.thruDate ? productFeatureCategoryAppl.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
