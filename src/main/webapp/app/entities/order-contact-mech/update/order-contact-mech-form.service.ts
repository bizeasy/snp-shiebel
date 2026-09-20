import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOrderContactMech, NewOrderContactMech } from '../order-contact-mech.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderContactMech for edit and NewOrderContactMechFormGroupInput for create.
 */
type OrderContactMechFormGroupInput = IOrderContactMech | PartialWithRequiredKeyOf<NewOrderContactMech>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrderContactMech | NewOrderContactMech> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type OrderContactMechFormRawValue = FormValueOf<IOrderContactMech>;

type NewOrderContactMechFormRawValue = FormValueOf<NewOrderContactMech>;

type OrderContactMechFormDefaults = Pick<NewOrderContactMech, 'id' | 'fromDate' | 'thruDate'>;

type OrderContactMechFormGroupContent = {
  id: FormControl<OrderContactMechFormRawValue['id'] | NewOrderContactMech['id']>;
  fromDate: FormControl<OrderContactMechFormRawValue['fromDate']>;
  thruDate: FormControl<OrderContactMechFormRawValue['thruDate']>;
  order: FormControl<OrderContactMechFormRawValue['order']>;
  contactMech: FormControl<OrderContactMechFormRawValue['contactMech']>;
  contactMechPurpose: FormControl<OrderContactMechFormRawValue['contactMechPurpose']>;
};

export type OrderContactMechFormGroup = FormGroup<OrderContactMechFormGroupContent>;

@Service()
export class OrderContactMechFormService {
  createOrderContactMechFormGroup(orderContactMech?: OrderContactMechFormGroupInput): OrderContactMechFormGroup {
    const orderContactMechRawValue = this.convertOrderContactMechToOrderContactMechRawValue({
      ...this.getFormDefaults(),
      ...(orderContactMech ?? { id: null }),
    });

    return new FormGroup<OrderContactMechFormGroupContent>({
      id: new FormControl(
        { value: orderContactMechRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(orderContactMechRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(orderContactMechRawValue.thruDate),
      order: new FormControl(orderContactMechRawValue.order, {
        validators: [Validators.required],
      }),
      contactMech: new FormControl(orderContactMechRawValue.contactMech, {
        validators: [Validators.required],
      }),
      contactMechPurpose: new FormControl(orderContactMechRawValue.contactMechPurpose),
    });
  }

  getOrderContactMech(form: OrderContactMechFormGroup): IOrderContactMech | NewOrderContactMech {
    return this.convertOrderContactMechRawValueToOrderContactMech(form.getRawValue());
  }

  resetForm(form: OrderContactMechFormGroup, orderContactMech: OrderContactMechFormGroupInput): void {
    const orderContactMechRawValue = this.convertOrderContactMechToOrderContactMechRawValue({
      ...this.getFormDefaults(),
      ...orderContactMech,
    });
    form.reset({
      ...orderContactMechRawValue,
      id: { value: orderContactMechRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrderContactMechFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
    };
  }

  private convertOrderContactMechRawValueToOrderContactMech(
    rawOrderContactMech: OrderContactMechFormRawValue | NewOrderContactMechFormRawValue,
  ): IOrderContactMech | NewOrderContactMech {
    return {
      ...rawOrderContactMech,
      fromDate: dayjs(rawOrderContactMech.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawOrderContactMech.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderContactMechToOrderContactMechRawValue(
    orderContactMech: IOrderContactMech | (Partial<NewOrderContactMech> & OrderContactMechFormDefaults),
  ): OrderContactMechFormRawValue | PartialWithRequiredKeyOf<NewOrderContactMechFormRawValue> {
    return {
      ...orderContactMech,
      fromDate: orderContactMech.fromDate ? orderContactMech.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: orderContactMech.thruDate ? orderContactMech.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
