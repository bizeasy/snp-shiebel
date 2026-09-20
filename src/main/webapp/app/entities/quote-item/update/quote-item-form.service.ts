import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IQuoteItem, NewQuoteItem } from '../quote-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuoteItem for edit and NewQuoteItemFormGroupInput for create.
 */
type QuoteItemFormGroupInput = IQuoteItem | PartialWithRequiredKeyOf<NewQuoteItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuoteItem | NewQuoteItem> = Omit<T, 'estimatedDeliveryDate'> & {
  estimatedDeliveryDate?: string | null;
};

type QuoteItemFormRawValue = FormValueOf<IQuoteItem>;

type NewQuoteItemFormRawValue = FormValueOf<NewQuoteItem>;

type QuoteItemFormDefaults = Pick<NewQuoteItem, 'id' | 'estimatedDeliveryDate'>;

type QuoteItemFormGroupContent = {
  id: FormControl<QuoteItemFormRawValue['id'] | NewQuoteItem['id']>;
  sequenceNo: FormControl<QuoteItemFormRawValue['sequenceNo']>;
  quantity: FormControl<QuoteItemFormRawValue['quantity']>;
  selectedAmount: FormControl<QuoteItemFormRawValue['selectedAmount']>;
  quoteUnitPrice: FormControl<QuoteItemFormRawValue['quoteUnitPrice']>;
  totalAmount: FormControl<QuoteItemFormRawValue['totalAmount']>;
  estimatedDeliveryDate: FormControl<QuoteItemFormRawValue['estimatedDeliveryDate']>;
  leadTimeDays: FormControl<QuoteItemFormRawValue['leadTimeDays']>;
  comments: FormControl<QuoteItemFormRawValue['comments']>;
  quote: FormControl<QuoteItemFormRawValue['quote']>;
  product: FormControl<QuoteItemFormRawValue['product']>;
  productFeature: FormControl<QuoteItemFormRawValue['productFeature']>;
  uom: FormControl<QuoteItemFormRawValue['uom']>;
  status: FormControl<QuoteItemFormRawValue['status']>;
  custRequest: FormControl<QuoteItemFormRawValue['custRequest']>;
  custRequestItem: FormControl<QuoteItemFormRawValue['custRequestItem']>;
};

export type QuoteItemFormGroup = FormGroup<QuoteItemFormGroupContent>;

@Service()
export class QuoteItemFormService {
  createQuoteItemFormGroup(quoteItem?: QuoteItemFormGroupInput): QuoteItemFormGroup {
    const quoteItemRawValue = this.convertQuoteItemToQuoteItemRawValue({
      ...this.getFormDefaults(),
      ...(quoteItem ?? { id: null }),
    });

    return new FormGroup<QuoteItemFormGroupContent>({
      id: new FormControl(
        { value: quoteItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(quoteItemRawValue.sequenceNo),
      quantity: new FormControl(quoteItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      selectedAmount: new FormControl(quoteItemRawValue.selectedAmount),
      quoteUnitPrice: new FormControl(quoteItemRawValue.quoteUnitPrice),
      totalAmount: new FormControl(quoteItemRawValue.totalAmount),
      estimatedDeliveryDate: new FormControl(quoteItemRawValue.estimatedDeliveryDate),
      leadTimeDays: new FormControl(quoteItemRawValue.leadTimeDays),
      comments: new FormControl(quoteItemRawValue.comments, {
        validators: [Validators.maxLength(2000)],
      }),
      quote: new FormControl(quoteItemRawValue.quote, {
        validators: [Validators.required],
      }),
      product: new FormControl(quoteItemRawValue.product),
      productFeature: new FormControl(quoteItemRawValue.productFeature),
      uom: new FormControl(quoteItemRawValue.uom),
      status: new FormControl(quoteItemRawValue.status),
      custRequest: new FormControl(quoteItemRawValue.custRequest),
      custRequestItem: new FormControl(quoteItemRawValue.custRequestItem),
    });
  }

  getQuoteItem(form: QuoteItemFormGroup): IQuoteItem | NewQuoteItem {
    return this.convertQuoteItemRawValueToQuoteItem(form.getRawValue());
  }

  resetForm(form: QuoteItemFormGroup, quoteItem: QuoteItemFormGroupInput): void {
    const quoteItemRawValue = this.convertQuoteItemToQuoteItemRawValue({ ...this.getFormDefaults(), ...quoteItem });
    form.reset({
      ...quoteItemRawValue,
      id: { value: quoteItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuoteItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      estimatedDeliveryDate: currentTime,
    };
  }

  private convertQuoteItemRawValueToQuoteItem(rawQuoteItem: QuoteItemFormRawValue | NewQuoteItemFormRawValue): IQuoteItem | NewQuoteItem {
    return {
      ...rawQuoteItem,
      estimatedDeliveryDate: dayjs(rawQuoteItem.estimatedDeliveryDate, DATE_TIME_FORMAT),
    };
  }

  private convertQuoteItemToQuoteItemRawValue(
    quoteItem: IQuoteItem | (Partial<NewQuoteItem> & QuoteItemFormDefaults),
  ): QuoteItemFormRawValue | PartialWithRequiredKeyOf<NewQuoteItemFormRawValue> {
    return {
      ...quoteItem,
      estimatedDeliveryDate: quoteItem.estimatedDeliveryDate ? quoteItem.estimatedDeliveryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
