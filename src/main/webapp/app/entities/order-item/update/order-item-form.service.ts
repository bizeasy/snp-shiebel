import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOrderItem, NewOrderItem } from '../order-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderItem for edit and NewOrderItemFormGroupInput for create.
 */
type OrderItemFormGroupInput = IOrderItem | PartialWithRequiredKeyOf<NewOrderItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrderItem | NewOrderItem> = Omit<
  T,
  'estimatedShipDate' | 'estimatedDeliveryDate' | 'shipBeforeDate' | 'shipAfterDate' | 'entryDate'
> & {
  estimatedShipDate?: string | null;
  estimatedDeliveryDate?: string | null;
  shipBeforeDate?: string | null;
  shipAfterDate?: string | null;
  entryDate?: string | null;
};

type OrderItemFormRawValue = FormValueOf<IOrderItem>;

type NewOrderItemFormRawValue = FormValueOf<NewOrderItem>;

type OrderItemFormDefaults = Pick<
  NewOrderItem,
  'id' | 'isModifiedPrice' | 'estimatedShipDate' | 'estimatedDeliveryDate' | 'shipBeforeDate' | 'shipAfterDate' | 'entryDate'
>;

type OrderItemFormGroupContent = {
  id: FormControl<OrderItemFormRawValue['id'] | NewOrderItem['id']>;
  sequenceNo: FormControl<OrderItemFormRawValue['sequenceNo']>;
  quantity: FormControl<OrderItemFormRawValue['quantity']>;
  cancelQuantity: FormControl<OrderItemFormRawValue['cancelQuantity']>;
  selectedAmount: FormControl<OrderItemFormRawValue['selectedAmount']>;
  unitPrice: FormControl<OrderItemFormRawValue['unitPrice']>;
  unitListPrice: FormControl<OrderItemFormRawValue['unitListPrice']>;
  isModifiedPrice: FormControl<OrderItemFormRawValue['isModifiedPrice']>;
  estimatedShipDate: FormControl<OrderItemFormRawValue['estimatedShipDate']>;
  estimatedDeliveryDate: FormControl<OrderItemFormRawValue['estimatedDeliveryDate']>;
  shipBeforeDate: FormControl<OrderItemFormRawValue['shipBeforeDate']>;
  shipAfterDate: FormControl<OrderItemFormRawValue['shipAfterDate']>;
  entryDate: FormControl<OrderItemFormRawValue['entryDate']>;
  attr1: FormControl<OrderItemFormRawValue['attr1']>;
  attr2: FormControl<OrderItemFormRawValue['attr2']>;
  attr3: FormControl<OrderItemFormRawValue['attr3']>;
  order: FormControl<OrderItemFormRawValue['order']>;
  orderItemType: FormControl<OrderItemFormRawValue['orderItemType']>;
  product: FormControl<OrderItemFormRawValue['product']>;
  uom: FormControl<OrderItemFormRawValue['uom']>;
  supplierProduct: FormControl<OrderItemFormRawValue['supplierProduct']>;
  supplier: FormControl<OrderItemFormRawValue['supplier']>;
  productFeature: FormControl<OrderItemFormRawValue['productFeature']>;
  productFacility: FormControl<OrderItemFormRawValue['productFacility']>;
  fromInventoryItem: FormControl<OrderItemFormRawValue['fromInventoryItem']>;
  shoppingListItem: FormControl<OrderItemFormRawValue['shoppingListItem']>;
  quoteItem: FormControl<OrderItemFormRawValue['quoteItem']>;
  status: FormControl<OrderItemFormRawValue['status']>;
  taxAuthRate: FormControl<OrderItemFormRawValue['taxAuthRate']>;
};

export type OrderItemFormGroup = FormGroup<OrderItemFormGroupContent>;

@Service()
export class OrderItemFormService {
  createOrderItemFormGroup(orderItem?: OrderItemFormGroupInput): OrderItemFormGroup {
    const orderItemRawValue = this.convertOrderItemToOrderItemRawValue({
      ...this.getFormDefaults(),
      ...(orderItem ?? { id: null }),
    });

    return new FormGroup<OrderItemFormGroupContent>({
      id: new FormControl(
        { value: orderItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(orderItemRawValue.sequenceNo),
      quantity: new FormControl(orderItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      cancelQuantity: new FormControl(orderItemRawValue.cancelQuantity),
      selectedAmount: new FormControl(orderItemRawValue.selectedAmount),
      unitPrice: new FormControl(orderItemRawValue.unitPrice),
      unitListPrice: new FormControl(orderItemRawValue.unitListPrice),
      isModifiedPrice: new FormControl(orderItemRawValue.isModifiedPrice),
      estimatedShipDate: new FormControl(orderItemRawValue.estimatedShipDate),
      estimatedDeliveryDate: new FormControl(orderItemRawValue.estimatedDeliveryDate),
      shipBeforeDate: new FormControl(orderItemRawValue.shipBeforeDate),
      shipAfterDate: new FormControl(orderItemRawValue.shipAfterDate),
      entryDate: new FormControl(orderItemRawValue.entryDate),
      attr1: new FormControl(orderItemRawValue.attr1, {
        validators: [Validators.maxLength(255)],
      }),
      attr2: new FormControl(orderItemRawValue.attr2, {
        validators: [Validators.maxLength(255)],
      }),
      attr3: new FormControl(orderItemRawValue.attr3, {
        validators: [Validators.maxLength(255)],
      }),
      order: new FormControl(orderItemRawValue.order, {
        validators: [Validators.required],
      }),
      orderItemType: new FormControl(orderItemRawValue.orderItemType),
      product: new FormControl(orderItemRawValue.product),
      uom: new FormControl(orderItemRawValue.uom),
      supplierProduct: new FormControl(orderItemRawValue.supplierProduct),
      supplier: new FormControl(orderItemRawValue.supplier),
      productFeature: new FormControl(orderItemRawValue.productFeature),
      productFacility: new FormControl(orderItemRawValue.productFacility),
      fromInventoryItem: new FormControl(orderItemRawValue.fromInventoryItem),
      shoppingListItem: new FormControl(orderItemRawValue.shoppingListItem),
      quoteItem: new FormControl(orderItemRawValue.quoteItem),
      status: new FormControl(orderItemRawValue.status),
      taxAuthRate: new FormControl(orderItemRawValue.taxAuthRate),
    });
  }

  getOrderItem(form: OrderItemFormGroup): IOrderItem | NewOrderItem {
    return this.convertOrderItemRawValueToOrderItem(form.getRawValue());
  }

  resetForm(form: OrderItemFormGroup, orderItem: OrderItemFormGroupInput): void {
    const orderItemRawValue = this.convertOrderItemToOrderItemRawValue({ ...this.getFormDefaults(), ...orderItem });
    form.reset({
      ...orderItemRawValue,
      id: { value: orderItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isModifiedPrice: false,
      estimatedShipDate: currentTime,
      estimatedDeliveryDate: currentTime,
      shipBeforeDate: currentTime,
      shipAfterDate: currentTime,
      entryDate: currentTime,
    };
  }

  private convertOrderItemRawValueToOrderItem(rawOrderItem: OrderItemFormRawValue | NewOrderItemFormRawValue): IOrderItem | NewOrderItem {
    return {
      ...rawOrderItem,
      estimatedShipDate: dayjs(rawOrderItem.estimatedShipDate, DATE_TIME_FORMAT),
      estimatedDeliveryDate: dayjs(rawOrderItem.estimatedDeliveryDate, DATE_TIME_FORMAT),
      shipBeforeDate: dayjs(rawOrderItem.shipBeforeDate, DATE_TIME_FORMAT),
      shipAfterDate: dayjs(rawOrderItem.shipAfterDate, DATE_TIME_FORMAT),
      entryDate: dayjs(rawOrderItem.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderItemToOrderItemRawValue(
    orderItem: IOrderItem | (Partial<NewOrderItem> & OrderItemFormDefaults),
  ): OrderItemFormRawValue | PartialWithRequiredKeyOf<NewOrderItemFormRawValue> {
    return {
      ...orderItem,
      estimatedShipDate: orderItem.estimatedShipDate ? orderItem.estimatedShipDate.format(DATE_TIME_FORMAT) : undefined,
      estimatedDeliveryDate: orderItem.estimatedDeliveryDate ? orderItem.estimatedDeliveryDate.format(DATE_TIME_FORMAT) : undefined,
      shipBeforeDate: orderItem.shipBeforeDate ? orderItem.shipBeforeDate.format(DATE_TIME_FORMAT) : undefined,
      shipAfterDate: orderItem.shipAfterDate ? orderItem.shipAfterDate.format(DATE_TIME_FORMAT) : undefined,
      entryDate: orderItem.entryDate ? orderItem.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
