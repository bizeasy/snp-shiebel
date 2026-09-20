import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductFeatureGroupAppl, NewProductFeatureGroupAppl } from '../product-feature-group-appl.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductFeatureGroupAppl for edit and NewProductFeatureGroupApplFormGroupInput for create.
 */
type ProductFeatureGroupApplFormGroupInput = IProductFeatureGroupAppl | PartialWithRequiredKeyOf<NewProductFeatureGroupAppl>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductFeatureGroupAppl | NewProductFeatureGroupAppl> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductFeatureGroupApplFormRawValue = FormValueOf<IProductFeatureGroupAppl>;

type NewProductFeatureGroupApplFormRawValue = FormValueOf<NewProductFeatureGroupAppl>;

type ProductFeatureGroupApplFormDefaults = Pick<NewProductFeatureGroupAppl, 'id' | 'fromDate' | 'thruDate'>;

type ProductFeatureGroupApplFormGroupContent = {
  id: FormControl<ProductFeatureGroupApplFormRawValue['id'] | NewProductFeatureGroupAppl['id']>;
  fromDate: FormControl<ProductFeatureGroupApplFormRawValue['fromDate']>;
  thruDate: FormControl<ProductFeatureGroupApplFormRawValue['thruDate']>;
  sequenceNo: FormControl<ProductFeatureGroupApplFormRawValue['sequenceNo']>;
  productFeatureGroup: FormControl<ProductFeatureGroupApplFormRawValue['productFeatureGroup']>;
  productFeature: FormControl<ProductFeatureGroupApplFormRawValue['productFeature']>;
};

export type ProductFeatureGroupApplFormGroup = FormGroup<ProductFeatureGroupApplFormGroupContent>;

@Service()
export class ProductFeatureGroupApplFormService {
  createProductFeatureGroupApplFormGroup(
    productFeatureGroupAppl?: ProductFeatureGroupApplFormGroupInput,
  ): ProductFeatureGroupApplFormGroup {
    const productFeatureGroupApplRawValue = this.convertProductFeatureGroupApplToProductFeatureGroupApplRawValue({
      ...this.getFormDefaults(),
      ...(productFeatureGroupAppl ?? { id: null }),
    });

    return new FormGroup<ProductFeatureGroupApplFormGroupContent>({
      id: new FormControl(
        { value: productFeatureGroupApplRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productFeatureGroupApplRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productFeatureGroupApplRawValue.thruDate),
      sequenceNo: new FormControl(productFeatureGroupApplRawValue.sequenceNo),
      productFeatureGroup: new FormControl(productFeatureGroupApplRawValue.productFeatureGroup, {
        validators: [Validators.required],
      }),
      productFeature: new FormControl(productFeatureGroupApplRawValue.productFeature, {
        validators: [Validators.required],
      }),
    });
  }

  getProductFeatureGroupAppl(form: ProductFeatureGroupApplFormGroup): IProductFeatureGroupAppl | NewProductFeatureGroupAppl {
    return this.convertProductFeatureGroupApplRawValueToProductFeatureGroupAppl(form.getRawValue());
  }

  resetForm(form: ProductFeatureGroupApplFormGroup, productFeatureGroupAppl: ProductFeatureGroupApplFormGroupInput): void {
    const productFeatureGroupApplRawValue = this.convertProductFeatureGroupApplToProductFeatureGroupApplRawValue({
      ...this.getFormDefaults(),
      ...productFeatureGroupAppl,
    });
    form.reset({
      ...productFeatureGroupApplRawValue,
      id: { value: productFeatureGroupApplRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFeatureGroupApplFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductFeatureGroupApplRawValueToProductFeatureGroupAppl(
    rawProductFeatureGroupAppl: ProductFeatureGroupApplFormRawValue | NewProductFeatureGroupApplFormRawValue,
  ): IProductFeatureGroupAppl | NewProductFeatureGroupAppl {
    return {
      ...rawProductFeatureGroupAppl,
      fromDate: dayjs(rawProductFeatureGroupAppl.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductFeatureGroupAppl.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductFeatureGroupApplToProductFeatureGroupApplRawValue(
    productFeatureGroupAppl: IProductFeatureGroupAppl | (Partial<NewProductFeatureGroupAppl> & ProductFeatureGroupApplFormDefaults),
  ): ProductFeatureGroupApplFormRawValue | PartialWithRequiredKeyOf<NewProductFeatureGroupApplFormRawValue> {
    return {
      ...productFeatureGroupAppl,
      fromDate: productFeatureGroupAppl.fromDate ? productFeatureGroupAppl.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productFeatureGroupAppl.thruDate ? productFeatureGroupAppl.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
