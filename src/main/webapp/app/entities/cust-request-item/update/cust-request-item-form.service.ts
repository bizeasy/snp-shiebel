import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICustRequestItem, NewCustRequestItem } from '../cust-request-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequestItem for edit and NewCustRequestItemFormGroupInput for create.
 */
type CustRequestItemFormGroupInput = ICustRequestItem | PartialWithRequiredKeyOf<NewCustRequestItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustRequestItem | NewCustRequestItem> = Omit<T, 'requiredByDate'> & {
  requiredByDate?: string | null;
};

type CustRequestItemFormRawValue = FormValueOf<ICustRequestItem>;

type NewCustRequestItemFormRawValue = FormValueOf<NewCustRequestItem>;

type CustRequestItemFormDefaults = Pick<NewCustRequestItem, 'id' | 'requiredByDate'>;

type CustRequestItemFormGroupContent = {
  id: FormControl<CustRequestItemFormRawValue['id'] | NewCustRequestItem['id']>;
  sequenceNo: FormControl<CustRequestItemFormRawValue['sequenceNo']>;
  priority: FormControl<CustRequestItemFormRawValue['priority']>;
  requiredByDate: FormControl<CustRequestItemFormRawValue['requiredByDate']>;
  quantity: FormControl<CustRequestItemFormRawValue['quantity']>;
  selectedAmount: FormControl<CustRequestItemFormRawValue['selectedAmount']>;
  maxAmount: FormControl<CustRequestItemFormRawValue['maxAmount']>;
  description: FormControl<CustRequestItemFormRawValue['description']>;
  story: FormControl<CustRequestItemFormRawValue['story']>;
  custRequest: FormControl<CustRequestItemFormRawValue['custRequest']>;
  status: FormControl<CustRequestItemFormRawValue['status']>;
  product: FormControl<CustRequestItemFormRawValue['product']>;
  uom: FormControl<CustRequestItemFormRawValue['uom']>;
  shoppingListItem: FormControl<CustRequestItemFormRawValue['shoppingListItem']>;
};

export type CustRequestItemFormGroup = FormGroup<CustRequestItemFormGroupContent>;

@Service()
export class CustRequestItemFormService {
  createCustRequestItemFormGroup(custRequestItem?: CustRequestItemFormGroupInput): CustRequestItemFormGroup {
    const custRequestItemRawValue = this.convertCustRequestItemToCustRequestItemRawValue({
      ...this.getFormDefaults(),
      ...(custRequestItem ?? { id: null }),
    });

    return new FormGroup<CustRequestItemFormGroupContent>({
      id: new FormControl(
        { value: custRequestItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(custRequestItemRawValue.sequenceNo),
      priority: new FormControl(custRequestItemRawValue.priority, {
        validators: [Validators.min(1), Validators.max(20)],
      }),
      requiredByDate: new FormControl(custRequestItemRawValue.requiredByDate),
      quantity: new FormControl(custRequestItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      selectedAmount: new FormControl(custRequestItemRawValue.selectedAmount),
      maxAmount: new FormControl(custRequestItemRawValue.maxAmount),
      description: new FormControl(custRequestItemRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      story: new FormControl(custRequestItemRawValue.story, {
        validators: [Validators.maxLength(2000)],
      }),
      custRequest: new FormControl(custRequestItemRawValue.custRequest, {
        validators: [Validators.required],
      }),
      status: new FormControl(custRequestItemRawValue.status),
      product: new FormControl(custRequestItemRawValue.product),
      uom: new FormControl(custRequestItemRawValue.uom),
      shoppingListItem: new FormControl(custRequestItemRawValue.shoppingListItem),
    });
  }

  getCustRequestItem(form: CustRequestItemFormGroup): ICustRequestItem | NewCustRequestItem {
    return this.convertCustRequestItemRawValueToCustRequestItem(form.getRawValue());
  }

  resetForm(form: CustRequestItemFormGroup, custRequestItem: CustRequestItemFormGroupInput): void {
    const custRequestItemRawValue = this.convertCustRequestItemToCustRequestItemRawValue({ ...this.getFormDefaults(), ...custRequestItem });
    form.reset({
      ...custRequestItemRawValue,
      id: { value: custRequestItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requiredByDate: currentTime,
    };
  }

  private convertCustRequestItemRawValueToCustRequestItem(
    rawCustRequestItem: CustRequestItemFormRawValue | NewCustRequestItemFormRawValue,
  ): ICustRequestItem | NewCustRequestItem {
    return {
      ...rawCustRequestItem,
      requiredByDate: dayjs(rawCustRequestItem.requiredByDate, DATE_TIME_FORMAT),
    };
  }

  private convertCustRequestItemToCustRequestItemRawValue(
    custRequestItem: ICustRequestItem | (Partial<NewCustRequestItem> & CustRequestItemFormDefaults),
  ): CustRequestItemFormRawValue | PartialWithRequiredKeyOf<NewCustRequestItemFormRawValue> {
    return {
      ...custRequestItem,
      requiredByDate: custRequestItem.requiredByDate ? custRequestItem.requiredByDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
