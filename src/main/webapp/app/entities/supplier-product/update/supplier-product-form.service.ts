import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ISupplierProduct, NewSupplierProduct } from '../supplier-product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISupplierProduct for edit and NewSupplierProductFormGroupInput for create.
 */
type SupplierProductFormGroupInput = ISupplierProduct | PartialWithRequiredKeyOf<NewSupplierProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISupplierProduct | NewSupplierProduct> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type SupplierProductFormRawValue = FormValueOf<ISupplierProduct>;

type NewSupplierProductFormRawValue = FormValueOf<NewSupplierProduct>;

type SupplierProductFormDefaults = Pick<NewSupplierProduct, 'id' | 'fromDate' | 'thruDate' | 'canDropShip'>;

type SupplierProductFormGroupContent = {
  id: FormControl<SupplierProductFormRawValue['id'] | NewSupplierProduct['id']>;
  fromDate: FormControl<SupplierProductFormRawValue['fromDate']>;
  thruDate: FormControl<SupplierProductFormRawValue['thruDate']>;
  supplierProductId: FormControl<SupplierProductFormRawValue['supplierProductId']>;
  minOrderQty: FormControl<SupplierProductFormRawValue['minOrderQty']>;
  lastPrice: FormControl<SupplierProductFormRawValue['lastPrice']>;
  shippingPrice: FormControl<SupplierProductFormRawValue['shippingPrice']>;
  cgst: FormControl<SupplierProductFormRawValue['cgst']>;
  igst: FormControl<SupplierProductFormRawValue['igst']>;
  sgst: FormControl<SupplierProductFormRawValue['sgst']>;
  totalPrice: FormControl<SupplierProductFormRawValue['totalPrice']>;
  comments: FormControl<SupplierProductFormRawValue['comments']>;
  leadTimeDays: FormControl<SupplierProductFormRawValue['leadTimeDays']>;
  unitsIncluded: FormControl<SupplierProductFormRawValue['unitsIncluded']>;
  canDropShip: FormControl<SupplierProductFormRawValue['canDropShip']>;
  product: FormControl<SupplierProductFormRawValue['product']>;
  party: FormControl<SupplierProductFormRawValue['party']>;
  quantityUom: FormControl<SupplierProductFormRawValue['quantityUom']>;
  currencyUom: FormControl<SupplierProductFormRawValue['currencyUom']>;
};

export type SupplierProductFormGroup = FormGroup<SupplierProductFormGroupContent>;

@Service()
export class SupplierProductFormService {
  createSupplierProductFormGroup(supplierProduct?: SupplierProductFormGroupInput): SupplierProductFormGroup {
    const supplierProductRawValue = this.convertSupplierProductToSupplierProductRawValue({
      ...this.getFormDefaults(),
      ...(supplierProduct ?? { id: null }),
    });

    return new FormGroup<SupplierProductFormGroupContent>({
      id: new FormControl(
        { value: supplierProductRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(supplierProductRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(supplierProductRawValue.thruDate),
      supplierProductId: new FormControl(supplierProductRawValue.supplierProductId, {
        validators: [Validators.maxLength(60)],
      }),
      minOrderQty: new FormControl(supplierProductRawValue.minOrderQty),
      lastPrice: new FormControl(supplierProductRawValue.lastPrice),
      shippingPrice: new FormControl(supplierProductRawValue.shippingPrice),
      cgst: new FormControl(supplierProductRawValue.cgst),
      igst: new FormControl(supplierProductRawValue.igst),
      sgst: new FormControl(supplierProductRawValue.sgst),
      totalPrice: new FormControl(supplierProductRawValue.totalPrice),
      comments: new FormControl(supplierProductRawValue.comments, {
        validators: [Validators.maxLength(255)],
      }),
      leadTimeDays: new FormControl(supplierProductRawValue.leadTimeDays),
      unitsIncluded: new FormControl(supplierProductRawValue.unitsIncluded),
      canDropShip: new FormControl(supplierProductRawValue.canDropShip),
      product: new FormControl(supplierProductRawValue.product, {
        validators: [Validators.required],
      }),
      party: new FormControl(supplierProductRawValue.party, {
        validators: [Validators.required],
      }),
      quantityUom: new FormControl(supplierProductRawValue.quantityUom),
      currencyUom: new FormControl(supplierProductRawValue.currencyUom),
    });
  }

  getSupplierProduct(form: SupplierProductFormGroup): ISupplierProduct | NewSupplierProduct {
    return this.convertSupplierProductRawValueToSupplierProduct(form.getRawValue());
  }

  resetForm(form: SupplierProductFormGroup, supplierProduct: SupplierProductFormGroupInput): void {
    const supplierProductRawValue = this.convertSupplierProductToSupplierProductRawValue({ ...this.getFormDefaults(), ...supplierProduct });
    form.reset({
      ...supplierProductRawValue,
      id: { value: supplierProductRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SupplierProductFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
      canDropShip: false,
    };
  }

  private convertSupplierProductRawValueToSupplierProduct(
    rawSupplierProduct: SupplierProductFormRawValue | NewSupplierProductFormRawValue,
  ): ISupplierProduct | NewSupplierProduct {
    return {
      ...rawSupplierProduct,
      fromDate: dayjs(rawSupplierProduct.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawSupplierProduct.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertSupplierProductToSupplierProductRawValue(
    supplierProduct: ISupplierProduct | (Partial<NewSupplierProduct> & SupplierProductFormDefaults),
  ): SupplierProductFormRawValue | PartialWithRequiredKeyOf<NewSupplierProductFormRawValue> {
    return {
      ...supplierProduct,
      fromDate: supplierProduct.fromDate ? supplierProduct.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: supplierProduct.thruDate ? supplierProduct.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
