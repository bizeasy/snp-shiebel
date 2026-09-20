import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReturnItem, NewReturnItem } from '../return-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReturnItem for edit and NewReturnItemFormGroupInput for create.
 */
type ReturnItemFormGroupInput = IReturnItem | PartialWithRequiredKeyOf<NewReturnItem>;

type ReturnItemFormDefaults = Pick<NewReturnItem, 'id'>;

type ReturnItemFormGroupContent = {
  id: FormControl<IReturnItem['id'] | NewReturnItem['id']>;
  sequenceNo: FormControl<IReturnItem['sequenceNo']>;
  description: FormControl<IReturnItem['description']>;
  returnQuantity: FormControl<IReturnItem['returnQuantity']>;
  receivedQuantity: FormControl<IReturnItem['receivedQuantity']>;
  returnPrice: FormControl<IReturnItem['returnPrice']>;
  returnItemPurpose: FormControl<IReturnItem['returnItemPurpose']>;
  returnHeader: FormControl<IReturnItem['returnHeader']>;
  returnType: FormControl<IReturnItem['returnType']>;
  product: FormControl<IReturnItem['product']>;
  orderItem: FormControl<IReturnItem['orderItem']>;
  status: FormControl<IReturnItem['status']>;
};

export type ReturnItemFormGroup = FormGroup<ReturnItemFormGroupContent>;

@Service()
export class ReturnItemFormService {
  createReturnItemFormGroup(returnItem?: ReturnItemFormGroupInput): ReturnItemFormGroup {
    const returnItemRawValue = {
      ...this.getFormDefaults(),
      ...(returnItem ?? { id: null }),
    };

    return new FormGroup<ReturnItemFormGroupContent>({
      id: new FormControl(
        { value: returnItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(returnItemRawValue.sequenceNo),
      description: new FormControl(returnItemRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      returnQuantity: new FormControl(returnItemRawValue.returnQuantity, {
        validators: [Validators.required],
      }),
      receivedQuantity: new FormControl(returnItemRawValue.receivedQuantity),
      returnPrice: new FormControl(returnItemRawValue.returnPrice),
      returnItemPurpose: new FormControl(returnItemRawValue.returnItemPurpose, {
        validators: [Validators.maxLength(60)],
      }),
      returnHeader: new FormControl(returnItemRawValue.returnHeader, {
        validators: [Validators.required],
      }),
      returnType: new FormControl(returnItemRawValue.returnType),
      product: new FormControl(returnItemRawValue.product),
      orderItem: new FormControl(returnItemRawValue.orderItem),
      status: new FormControl(returnItemRawValue.status),
    });
  }

  getReturnItem(form: ReturnItemFormGroup): IReturnItem | NewReturnItem {
    return form.getRawValue();
  }

  resetForm(form: ReturnItemFormGroup, returnItem: ReturnItemFormGroupInput): void {
    const returnItemRawValue = { ...this.getFormDefaults(), ...returnItem };
    form.reset({
      ...returnItemRawValue,
      id: { value: returnItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ReturnItemFormDefaults {
    return {
      id: null,
    };
  }
}
