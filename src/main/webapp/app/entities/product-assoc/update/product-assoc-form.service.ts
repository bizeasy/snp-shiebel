import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductAssoc, NewProductAssoc } from '../product-assoc.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductAssoc for edit and NewProductAssocFormGroupInput for create.
 */
type ProductAssocFormGroupInput = IProductAssoc | PartialWithRequiredKeyOf<NewProductAssoc>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductAssoc | NewProductAssoc> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type ProductAssocFormRawValue = FormValueOf<IProductAssoc>;

type NewProductAssocFormRawValue = FormValueOf<NewProductAssoc>;

type ProductAssocFormDefaults = Pick<NewProductAssoc, 'id' | 'fromDate' | 'thruDate'>;

type ProductAssocFormGroupContent = {
  id: FormControl<ProductAssocFormRawValue['id'] | NewProductAssoc['id']>;
  fromDate: FormControl<ProductAssocFormRawValue['fromDate']>;
  thruDate: FormControl<ProductAssocFormRawValue['thruDate']>;
  sequenceNo: FormControl<ProductAssocFormRawValue['sequenceNo']>;
  quantity: FormControl<ProductAssocFormRawValue['quantity']>;
  product: FormControl<ProductAssocFormRawValue['product']>;
  productTo: FormControl<ProductAssocFormRawValue['productTo']>;
  productAssocType: FormControl<ProductAssocFormRawValue['productAssocType']>;
};

export type ProductAssocFormGroup = FormGroup<ProductAssocFormGroupContent>;

@Service()
export class ProductAssocFormService {
  createProductAssocFormGroup(productAssoc?: ProductAssocFormGroupInput): ProductAssocFormGroup {
    const productAssocRawValue = this.convertProductAssocToProductAssocRawValue({
      ...this.getFormDefaults(),
      ...(productAssoc ?? { id: null }),
    });

    return new FormGroup<ProductAssocFormGroupContent>({
      id: new FormControl(
        { value: productAssocRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(productAssocRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(productAssocRawValue.thruDate),
      sequenceNo: new FormControl(productAssocRawValue.sequenceNo),
      quantity: new FormControl(productAssocRawValue.quantity),
      product: new FormControl(productAssocRawValue.product, {
        validators: [Validators.required],
      }),
      productTo: new FormControl(productAssocRawValue.productTo, {
        validators: [Validators.required],
      }),
      productAssocType: new FormControl(productAssocRawValue.productAssocType),
    });
  }

  getProductAssoc(form: ProductAssocFormGroup): IProductAssoc | NewProductAssoc {
    return this.convertProductAssocRawValueToProductAssoc(form.getRawValue());
  }

  resetForm(form: ProductAssocFormGroup, productAssoc: ProductAssocFormGroupInput): void {
    const productAssocRawValue = this.convertProductAssocToProductAssocRawValue({ ...this.getFormDefaults(), ...productAssoc });
    form.reset({
      ...productAssocRawValue,
      id: { value: productAssocRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductAssocFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertProductAssocRawValueToProductAssoc(
    rawProductAssoc: ProductAssocFormRawValue | NewProductAssocFormRawValue,
  ): IProductAssoc | NewProductAssoc {
    return {
      ...rawProductAssoc,
      fromDate: dayjs(rawProductAssoc.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawProductAssoc.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductAssocToProductAssocRawValue(
    productAssoc: IProductAssoc | (Partial<NewProductAssoc> & ProductAssocFormDefaults),
  ): ProductAssocFormRawValue | PartialWithRequiredKeyOf<NewProductAssocFormRawValue> {
    return {
      ...productAssoc,
      fromDate: productAssoc.fromDate ? productAssoc.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: productAssoc.thruDate ? productAssoc.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
