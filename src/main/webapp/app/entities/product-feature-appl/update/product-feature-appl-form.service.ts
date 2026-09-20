import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductFeatureAppl, NewProductFeatureAppl } from '../product-feature-appl.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureAppl for edit and NewProductFeatureApplFormGroupInput for create.
 */
type ProductFeatureApplFormGroupInput = IProductFeatureAppl | PartialWithRequiredKeyOf<NewProductFeatureAppl>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductFeatureAppl | NewProductFeatureAppl> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductFeatureApplFormRawValue = FormValueOf<IProductFeatureAppl>;

type NewProductFeatureApplFormRawValue = FormValueOf<NewProductFeatureAppl>;

type ProductFeatureApplFormDefaults = Pick<NewProductFeatureAppl, 'id' | 'fromDate' | 'thruDate'>;

type ProductFeatureApplFormGroupContent = {
  id: FormControl<ProductFeatureApplFormRawValue['id'] | NewProductFeatureAppl['id']>;
  fromDate: FormControl<ProductFeatureApplFormRawValue['fromDate']>;
  thruDate: FormControl<ProductFeatureApplFormRawValue['thruDate']>;
  sequenceNo: FormControl<ProductFeatureApplFormRawValue['sequenceNo']>;
  amount: FormControl<ProductFeatureApplFormRawValue['amount']>;
  product: FormControl<ProductFeatureApplFormRawValue['product']>;
  productFeature: FormControl<ProductFeatureApplFormRawValue['productFeature']>;
  featureApplType: FormControl<ProductFeatureApplFormRawValue['featureApplType']>;
};

export type ProductFeatureApplFormGroup = FormGroup<ProductFeatureApplFormGroupContent>;

@Service()
export class ProductFeatureApplFormService {
  createProductFeatureApplFormGroup(productFeatureAppl?: ProductFeatureApplFormGroupInput): ProductFeatureApplFormGroup {
    const productFeatureApplRawValue = this.convertProductFeatureApplToProductFeatureApplRawValue({
      ...this.getFormDefaults(),
      ...(productFeatureAppl ?? { id: null }),
    });

    return new FormGroup<ProductFeatureApplFormGroupContent>({
      id: new FormControl(
        { value: productFeatureApplRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productFeatureApplRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productFeatureApplRawValue.thruDate),
      sequenceNo: new FormControl(productFeatureApplRawValue.sequenceNo),
      amount: new FormControl(productFeatureApplRawValue.amount),
      product: new FormControl(productFeatureApplRawValue.product, {
        validators: [Validators.required],
      }),
      productFeature: new FormControl(productFeatureApplRawValue.productFeature, {
        validators: [Validators.required],
      }),
      featureApplType: new FormControl(productFeatureApplRawValue.featureApplType),
    });
  }

  getProductFeatureAppl(form: ProductFeatureApplFormGroup): IProductFeatureAppl | NewProductFeatureAppl {
    return this.convertProductFeatureApplRawValueToProductFeatureAppl(form.getRawValue());
  }

  resetForm(form: ProductFeatureApplFormGroup, productFeatureAppl: ProductFeatureApplFormGroupInput): void {
    const productFeatureApplRawValue = this.convertProductFeatureApplToProductFeatureApplRawValue({
      ...this.getFormDefaults(),
      ...productFeatureAppl,
    });
    form.reset({
      ...productFeatureApplRawValue,
      id: { value: productFeatureApplRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureApplFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductFeatureApplRawValueToProductFeatureAppl(
    rawProductFeatureAppl: ProductFeatureApplFormRawValue | NewProductFeatureApplFormRawValue,
  ): IProductFeatureAppl | NewProductFeatureAppl {
    return {
      ...rawProductFeatureAppl,
      fromDate: dayjs(rawProductFeatureAppl.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductFeatureAppl.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductFeatureApplToProductFeatureApplRawValue(
    productFeatureAppl: IProductFeatureAppl | (Partial<NewProductFeatureAppl> & ProductFeatureApplFormDefaults),
  ): ProductFeatureApplFormRawValue | PartialWithRequiredKeyOf<NewProductFeatureApplFormRawValue> {
    return {
      ...productFeatureAppl,
      fromDate: productFeatureAppl.fromDate ? productFeatureAppl.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productFeatureAppl.thruDate ? productFeatureAppl.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
