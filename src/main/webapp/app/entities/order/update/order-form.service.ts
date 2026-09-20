import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrder | NewOrder> = Omit<T, 'orderDate' | 'entryDate'> & {
  orderDate?: string | null;
  entryDate?: string | null;
};

type OrderFormRawValue = FormValueOf<IOrder>;

type NewOrderFormRawValue = FormValueOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id' | 'orderDate' | 'entryDate' | 'isRushOrder' | 'needsInventoryIssuance'>;

type OrderFormGroupContent = {
  id: FormControl<OrderFormRawValue['id'] | NewOrder['id']>;
  name: FormControl<OrderFormRawValue['name']>;
  orderRefNo: FormControl<OrderFormRawValue['orderRefNo']>;
  externalId: FormControl<OrderFormRawValue['externalId']>;
  orderDate: FormControl<OrderFormRawValue['orderDate']>;
  entryDate: FormControl<OrderFormRawValue['entryDate']>;
  deliveryDate: FormControl<OrderFormRawValue['deliveryDate']>;
  priority: FormControl<OrderFormRawValue['priority']>;
  isRushOrder: FormControl<OrderFormRawValue['isRushOrder']>;
  needsInventoryIssuance: FormControl<OrderFormRawValue['needsInventoryIssuance']>;
  remainingSubTotal: FormControl<OrderFormRawValue['remainingSubTotal']>;
  grandTotal: FormControl<OrderFormRawValue['grandTotal']>;
  projectName: FormControl<OrderFormRawValue['projectName']>;
  version: FormControl<OrderFormRawValue['version']>;
  versionRef: FormControl<OrderFormRawValue['versionRef']>;
  description: FormControl<OrderFormRawValue['description']>;
  remarks: FormControl<OrderFormRawValue['remarks']>;
  paymentTerms: FormControl<OrderFormRawValue['paymentTerms']>;
  deliveryTerms: FormControl<OrderFormRawValue['deliveryTerms']>;
  info: FormControl<OrderFormRawValue['info']>;
  attr1: FormControl<OrderFormRawValue['attr1']>;
  attr2: FormControl<OrderFormRawValue['attr2']>;
  attr3: FormControl<OrderFormRawValue['attr3']>;
  orderType: FormControl<OrderFormRawValue['orderType']>;
  salesChannel: FormControl<OrderFormRawValue['salesChannel']>;
  party: FormControl<OrderFormRawValue['party']>;
  supplier: FormControl<OrderFormRawValue['supplier']>;
  status: FormControl<OrderFormRawValue['status']>;
  currencyUom: FormControl<OrderFormRawValue['currencyUom']>;
  currencyConversion: FormControl<OrderFormRawValue['currencyConversion']>;
  quote: FormControl<OrderFormRawValue['quote']>;
  facility: FormControl<OrderFormRawValue['facility']>;
  productStore: FormControl<OrderFormRawValue['productStore']>;
  department: FormControl<OrderFormRawValue['department']>;
  customTimePeriod: FormControl<OrderFormRawValue['customTimePeriod']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Service()
export class OrderFormService {
  createOrderFormGroup(order?: OrderFormGroupInput): OrderFormGroup {
    const orderRawValue = this.convertOrderToOrderRawValue({
      ...this.getFormDefaults(),
      ...(order ?? { id: null }),
    });

    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(orderRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      orderRefNo: new FormControl(orderRawValue.orderRefNo, {
        validators: [Validators.maxLength(60)],
      }),
      externalId: new FormControl(orderRawValue.externalId, {
        validators: [Validators.maxLength(25)],
      }),
      orderDate: new FormControl(orderRawValue.orderDate, {
        validators: [Validators.required],
      }),
      entryDate: new FormControl(orderRawValue.entryDate),
      deliveryDate: new FormControl(orderRawValue.deliveryDate),
      priority: new FormControl(orderRawValue.priority, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      isRushOrder: new FormControl(orderRawValue.isRushOrder),
      needsInventoryIssuance: new FormControl(orderRawValue.needsInventoryIssuance),
      remainingSubTotal: new FormControl(orderRawValue.remainingSubTotal),
      grandTotal: new FormControl(orderRawValue.grandTotal),
      projectName: new FormControl(orderRawValue.projectName, {
        validators: [Validators.maxLength(255)],
      }),
      version: new FormControl(orderRawValue.version),
      versionRef: new FormControl(orderRawValue.versionRef, {
        validators: [Validators.maxLength(60)],
      }),
      description: new FormControl(orderRawValue.description),
      remarks: new FormControl(orderRawValue.remarks, {
        validators: [Validators.maxLength(2000)],
      }),
      paymentTerms: new FormControl(orderRawValue.paymentTerms),
      deliveryTerms: new FormControl(orderRawValue.deliveryTerms),
      info: new FormControl(orderRawValue.info),
      attr1: new FormControl(orderRawValue.attr1, {
        validators: [Validators.maxLength(255)],
      }),
      attr2: new FormControl(orderRawValue.attr2, {
        validators: [Validators.maxLength(255)],
      }),
      attr3: new FormControl(orderRawValue.attr3, {
        validators: [Validators.maxLength(255)],
      }),
      orderType: new FormControl(orderRawValue.orderType, {
        validators: [Validators.required],
      }),
      salesChannel: new FormControl(orderRawValue.salesChannel),
      party: new FormControl(orderRawValue.party, {
        validators: [Validators.required],
      }),
      supplier: new FormControl(orderRawValue.supplier),
      status: new FormControl(orderRawValue.status),
      currencyUom: new FormControl(orderRawValue.currencyUom),
      currencyConversion: new FormControl(orderRawValue.currencyConversion),
      quote: new FormControl(orderRawValue.quote),
      facility: new FormControl(orderRawValue.facility),
      productStore: new FormControl(orderRawValue.productStore),
      department: new FormControl(orderRawValue.department),
      customTimePeriod: new FormControl(orderRawValue.customTimePeriod),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return this.convertOrderRawValueToOrder(form.getRawValue());
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = this.convertOrderToOrderRawValue({ ...this.getFormDefaults(), ...order });
    form.reset({
      ...orderRawValue,
      id: { value: orderRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderDate: currentTime,
      entryDate: currentTime,
      isRushOrder: false,
      needsInventoryIssuance: false,
    };
  }

  private convertOrderRawValueToOrder(rawOrder: OrderFormRawValue | NewOrderFormRawValue): IOrder | NewOrder {
    return {
      ...rawOrder,
      orderDate: dayjs(rawOrder.orderDate, DATE_TIME_FORMAT),
      entryDate: dayjs(rawOrder.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderToOrderRawValue(
    order: IOrder | (Partial<NewOrder> & OrderFormDefaults),
  ): OrderFormRawValue | PartialWithRequiredKeyOf<NewOrderFormRawValue> {
    return {
      ...order,
      orderDate: order.orderDate ? order.orderDate.format(DATE_TIME_FORMAT) : undefined,
      entryDate: order.entryDate ? order.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
