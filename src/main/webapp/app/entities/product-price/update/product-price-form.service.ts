import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductPrice, NewProductPrice } from '../product-price.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductPrice for edit and NewProductPriceFormGroupInput for create.
 */
type ProductPriceFormGroupInput = IProductPrice | PartialWithRequiredKeyOf<NewProductPrice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductPrice | NewProductPrice> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductPriceFormRawValue = FormValueOf<IProductPrice>;

type NewProductPriceFormRawValue = FormValueOf<NewProductPrice>;

type ProductPriceFormDefaults = Pick<NewProductPrice, 'id' | 'fromDate' | 'thruDate'>;

type ProductPriceFormGroupContent = {
  id: FormControl<ProductPriceFormRawValue['id'] | NewProductPrice['id']>;
  fromDate: FormControl<ProductPriceFormRawValue['fromDate']>;
  thruDate: FormControl<ProductPriceFormRawValue['thruDate']>;
  price: FormControl<ProductPriceFormRawValue['price']>;
  cgst: FormControl<ProductPriceFormRawValue['cgst']>;
  igst: FormControl<ProductPriceFormRawValue['igst']>;
  sgst: FormControl<ProductPriceFormRawValue['sgst']>;
  totalPrice: FormControl<ProductPriceFormRawValue['totalPrice']>;
  product: FormControl<ProductPriceFormRawValue['product']>;
  productPriceType: FormControl<ProductPriceFormRawValue['productPriceType']>;
  productPricePurpose: FormControl<ProductPriceFormRawValue['productPricePurpose']>;
  currencyUom: FormControl<ProductPriceFormRawValue['currencyUom']>;
};

export type ProductPriceFormGroup = FormGroup<ProductPriceFormGroupContent>;

@Service()
export class ProductPriceFormService {
  createProductPriceFormGroup(productPrice?: ProductPriceFormGroupInput): ProductPriceFormGroup {
    const productPriceRawValue = this.convertProductPriceToProductPriceRawValue({
      ...this.getFormDefaults(),
      ...(productPrice ?? { id: null }),
    });

    return new FormGroup<ProductPriceFormGroupContent>({
      id: new FormControl(
        { value: productPriceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productPriceRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productPriceRawValue.thruDate),
      price: new FormControl(productPriceRawValue.price, {
        validators: [Validators.required],
      }),
      cgst: new FormControl(productPriceRawValue.cgst),
      igst: new FormControl(productPriceRawValue.igst),
      sgst: new FormControl(productPriceRawValue.sgst),
      totalPrice: new FormControl(productPriceRawValue.totalPrice),
      product: new FormControl(productPriceRawValue.product, {
        validators: [Validators.required],
      }),
      productPriceType: new FormControl(productPriceRawValue.productPriceType),
      productPricePurpose: new FormControl(productPriceRawValue.productPricePurpose),
      currencyUom: new FormControl(productPriceRawValue.currencyUom),
    });
  }

  getProductPrice(form: ProductPriceFormGroup): IProductPrice | NewProductPrice {
    return this.convertProductPriceRawValueToProductPrice(form.getRawValue());
  }

  resetForm(form: ProductPriceFormGroup, productPrice: ProductPriceFormGroupInput): void {
    const productPriceRawValue = this.convertProductPriceToProductPriceRawValue({ ...this.getFormDefaults(), ...productPrice });
    form.reset({
      ...productPriceRawValue,
      id: { value: productPriceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductPriceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductPriceRawValueToProductPrice(
    rawProductPrice: ProductPriceFormRawValue | NewProductPriceFormRawValue,
  ): IProductPrice | NewProductPrice {
    return {
      ...rawProductPrice,
      fromDate: dayjs(rawProductPrice.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductPrice.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductPriceToProductPriceRawValue(
    productPrice: IProductPrice | (Partial<NewProductPrice> & ProductPriceFormDefaults),
  ): ProductPriceFormRawValue | PartialWithRequiredKeyOf<NewProductPriceFormRawValue> {
    return {
      ...productPrice,
      fromDate: productPrice.fromDate ? productPrice.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productPrice.thruDate ? productPrice.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
