import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOrderStatus, NewOrderStatus } from '../order-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderStatus for edit and NewOrderStatusFormGroupInput for create.
 */
type OrderStatusFormGroupInput = IOrderStatus | PartialWithRequiredKeyOf<NewOrderStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrderStatus | NewOrderStatus> = Omit<T, 'statusDateTime'> & {
  statusDateTime?: string | null;
};

type OrderStatusFormRawValue = FormValueOf<IOrderStatus>;

type NewOrderStatusFormRawValue = FormValueOf<NewOrderStatus>;

type OrderStatusFormDefaults = Pick<NewOrderStatus, 'id' | 'statusDateTime'>;

type OrderStatusFormGroupContent = {
  id: FormControl<OrderStatusFormRawValue['id'] | NewOrderStatus['id']>;
  statusDateTime: FormControl<OrderStatusFormRawValue['statusDateTime']>;
  sequenceNo: FormControl<OrderStatusFormRawValue['sequenceNo']>;
  order: FormControl<OrderStatusFormRawValue['order']>;
  status: FormControl<OrderStatusFormRawValue['status']>;
};

export type OrderStatusFormGroup = FormGroup<OrderStatusFormGroupContent>;

@Service()
export class OrderStatusFormService {
  createOrderStatusFormGroup(orderStatus?: OrderStatusFormGroupInput): OrderStatusFormGroup {
    const orderStatusRawValue = this.convertOrderStatusToOrderStatusRawValue({
      ...this.getFormDefaults(),
      ...(orderStatus ?? { id: null }),
    });

    return new FormGroup<OrderStatusFormGroupContent>({
      id: new FormControl(
        { value: orderStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      statusDateTime: new FormControl(orderStatusRawValue.statusDateTime, {
        validators: [Validators.required],
      }),
      sequenceNo: new FormControl(orderStatusRawValue.sequenceNo),
      order: new FormControl(orderStatusRawValue.order, {
        validators: [Validators.required],
      }),
      status: new FormControl(orderStatusRawValue.status, {
        validators: [Validators.required],
      }),
    });
  }

  getOrderStatus(form: OrderStatusFormGroup): IOrderStatus | NewOrderStatus {
    return this.convertOrderStatusRawValueToOrderStatus(form.getRawValue());
  }

  resetForm(form: OrderStatusFormGroup, orderStatus: OrderStatusFormGroupInput): void {
    const orderStatusRawValue = this.convertOrderStatusToOrderStatusRawValue({ ...this.getFormDefaults(), ...orderStatus });
    form.reset({
      ...orderStatusRawValue,
      id: { value: orderStatusRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderStatusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      statusDateTime: currentTime,
    };
  }

  private convertOrderStatusRawValueToOrderStatus(
    rawOrderStatus: OrderStatusFormRawValue | NewOrderStatusFormRawValue,
  ): IOrderStatus | NewOrderStatus {
    return {
      ...rawOrderStatus,
      statusDateTime: dayjs(rawOrderStatus.statusDateTime, DATE_TIME_FORMAT),
    };
  }

  private convertOrderStatusToOrderStatusRawValue(
    orderStatus: IOrderStatus | (Partial<NewOrderStatus> & OrderStatusFormDefaults),
  ): OrderStatusFormRawValue | PartialWithRequiredKeyOf<NewOrderStatusFormRawValue> {
    return {
      ...orderStatus,
      statusDateTime: orderStatus.statusDateTime ? orderStatus.statusDateTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
