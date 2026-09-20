import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOrderAdjustment, NewOrderAdjustment } from '../order-adjustment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderAdjustment for edit and NewOrderAdjustmentFormGroupInput for create.
 */
type OrderAdjustmentFormGroupInput = IOrderAdjustment | PartialWithRequiredKeyOf<NewOrderAdjustment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrderAdjustment | NewOrderAdjustment> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

type OrderAdjustmentFormRawValue = FormValueOf<IOrderAdjustment>;

type NewOrderAdjustmentFormRawValue = FormValueOf<NewOrderAdjustment>;

type OrderAdjustmentFormDefaults = Pick<NewOrderAdjustment, 'id' | 'includeInTax' | 'includeInShipping' | 'includedInPrice' | 'entryDate'>;

type OrderAdjustmentFormGroupContent = {
  id: FormControl<OrderAdjustmentFormRawValue['id'] | NewOrderAdjustment['id']>;
  description: FormControl<OrderAdjustmentFormRawValue['description']>;
  comments: FormControl<OrderAdjustmentFormRawValue['comments']>;
  amount: FormControl<OrderAdjustmentFormRawValue['amount']>;
  sourcePercentage: FormControl<OrderAdjustmentFormRawValue['sourcePercentage']>;
  sourceRefId: FormControl<OrderAdjustmentFormRawValue['sourceRefId']>;
  exemptAmount: FormControl<OrderAdjustmentFormRawValue['exemptAmount']>;
  includeInTax: FormControl<OrderAdjustmentFormRawValue['includeInTax']>;
  includeInShipping: FormControl<OrderAdjustmentFormRawValue['includeInShipping']>;
  includedInPrice: FormControl<OrderAdjustmentFormRawValue['includedInPrice']>;
  entryDate: FormControl<OrderAdjustmentFormRawValue['entryDate']>;
  order: FormControl<OrderAdjustmentFormRawValue['order']>;
  orderItem: FormControl<OrderAdjustmentFormRawValue['orderItem']>;
  adjustmentType: FormControl<OrderAdjustmentFormRawValue['adjustmentType']>;
  product: FormControl<OrderAdjustmentFormRawValue['product']>;
  productFeature: FormControl<OrderAdjustmentFormRawValue['productFeature']>;
  taxAuthority: FormControl<OrderAdjustmentFormRawValue['taxAuthority']>;
};

export type OrderAdjustmentFormGroup = FormGroup<OrderAdjustmentFormGroupContent>;

@Service()
export class OrderAdjustmentFormService {
  createOrderAdjustmentFormGroup(orderAdjustment?: OrderAdjustmentFormGroupInput): OrderAdjustmentFormGroup {
    const orderAdjustmentRawValue = this.convertOrderAdjustmentToOrderAdjustmentRawValue({
      ...this.getFormDefaults(),
      ...(orderAdjustment ?? { id: null }),
    });

    return new FormGroup<OrderAdjustmentFormGroupContent>({
      id: new FormControl(
        { value: orderAdjustmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(orderAdjustmentRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      comments: new FormControl(orderAdjustmentRawValue.comments, {
        validators: [Validators.maxLength(2000)],
      }),
      amount: new FormControl(orderAdjustmentRawValue.amount),
      sourcePercentage: new FormControl(orderAdjustmentRawValue.sourcePercentage),
      sourceRefId: new FormControl(orderAdjustmentRawValue.sourceRefId, {
        validators: [Validators.maxLength(60)],
      }),
      exemptAmount: new FormControl(orderAdjustmentRawValue.exemptAmount),
      includeInTax: new FormControl(orderAdjustmentRawValue.includeInTax),
      includeInShipping: new FormControl(orderAdjustmentRawValue.includeInShipping),
      includedInPrice: new FormControl(orderAdjustmentRawValue.includedInPrice),
      entryDate: new FormControl(orderAdjustmentRawValue.entryDate),
      order: new FormControl(orderAdjustmentRawValue.order),
      orderItem: new FormControl(orderAdjustmentRawValue.orderItem),
      adjustmentType: new FormControl(orderAdjustmentRawValue.adjustmentType),
      product: new FormControl(orderAdjustmentRawValue.product),
      productFeature: new FormControl(orderAdjustmentRawValue.productFeature),
      taxAuthority: new FormControl(orderAdjustmentRawValue.taxAuthority),
    });
  }

  getOrderAdjustment(form: OrderAdjustmentFormGroup): IOrderAdjustment | NewOrderAdjustment {
    return this.convertOrderAdjustmentRawValueToOrderAdjustment(form.getRawValue());
  }

  resetForm(form: OrderAdjustmentFormGroup, orderAdjustment: OrderAdjustmentFormGroupInput): void {
    const orderAdjustmentRawValue = this.convertOrderAdjustmentToOrderAdjustmentRawValue({ ...this.getFormDefaults(), ...orderAdjustment });
    form.reset({
      ...orderAdjustmentRawValue,
      id: { value: orderAdjustmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderAdjustmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      includeInTax: false,
      includeInShipping: false,
      includedInPrice: false,
      entryDate: currentTime,
    };
  }

  private convertOrderAdjustmentRawValueToOrderAdjustment(
    rawOrderAdjustment: OrderAdjustmentFormRawValue | NewOrderAdjustmentFormRawValue,
  ): IOrderAdjustment | NewOrderAdjustment {
    return {
      ...rawOrderAdjustment,
      entryDate: dayjs(rawOrderAdjustment.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderAdjustmentToOrderAdjustmentRawValue(
    orderAdjustment: IOrderAdjustment | (Partial<NewOrderAdjustment> & OrderAdjustmentFormDefaults),
  ): OrderAdjustmentFormRawValue | PartialWithRequiredKeyOf<NewOrderAdjustmentFormRawValue> {
    return {
      ...orderAdjustment,
      entryDate: orderAdjustment.entryDate ? orderAdjustment.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
