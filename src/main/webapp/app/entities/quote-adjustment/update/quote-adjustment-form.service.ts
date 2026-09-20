import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IQuoteAdjustment, NewQuoteAdjustment } from '../quote-adjustment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuoteAdjustment for edit and NewQuoteAdjustmentFormGroupInput for create.
 */
type QuoteAdjustmentFormGroupInput = IQuoteAdjustment | PartialWithRequiredKeyOf<NewQuoteAdjustment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuoteAdjustment | NewQuoteAdjustment> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

type QuoteAdjustmentFormRawValue = FormValueOf<IQuoteAdjustment>;

type NewQuoteAdjustmentFormRawValue = FormValueOf<NewQuoteAdjustment>;

type QuoteAdjustmentFormDefaults = Pick<NewQuoteAdjustment, 'id' | 'includeInTax' | 'includeInShipping' | 'includedInPrice' | 'entryDate'>;

type QuoteAdjustmentFormGroupContent = {
  id: FormControl<QuoteAdjustmentFormRawValue['id'] | NewQuoteAdjustment['id']>;
  description: FormControl<QuoteAdjustmentFormRawValue['description']>;
  comments: FormControl<QuoteAdjustmentFormRawValue['comments']>;
  amount: FormControl<QuoteAdjustmentFormRawValue['amount']>;
  sourcePercentage: FormControl<QuoteAdjustmentFormRawValue['sourcePercentage']>;
  sourceRefId: FormControl<QuoteAdjustmentFormRawValue['sourceRefId']>;
  exemptAmount: FormControl<QuoteAdjustmentFormRawValue['exemptAmount']>;
  includeInTax: FormControl<QuoteAdjustmentFormRawValue['includeInTax']>;
  includeInShipping: FormControl<QuoteAdjustmentFormRawValue['includeInShipping']>;
  includedInPrice: FormControl<QuoteAdjustmentFormRawValue['includedInPrice']>;
  entryDate: FormControl<QuoteAdjustmentFormRawValue['entryDate']>;
  quote: FormControl<QuoteAdjustmentFormRawValue['quote']>;
  quoteItem: FormControl<QuoteAdjustmentFormRawValue['quoteItem']>;
  adjustmentType: FormControl<QuoteAdjustmentFormRawValue['adjustmentType']>;
  product: FormControl<QuoteAdjustmentFormRawValue['product']>;
  productFeature: FormControl<QuoteAdjustmentFormRawValue['productFeature']>;
  taxAuthority: FormControl<QuoteAdjustmentFormRawValue['taxAuthority']>;
};

export type QuoteAdjustmentFormGroup = FormGroup<QuoteAdjustmentFormGroupContent>;

@Service()
export class QuoteAdjustmentFormService {
  createQuoteAdjustmentFormGroup(quoteAdjustment?: QuoteAdjustmentFormGroupInput): QuoteAdjustmentFormGroup {
    const quoteAdjustmentRawValue = this.convertQuoteAdjustmentToQuoteAdjustmentRawValue({
      ...this.getFormDefaults(),
      ...(quoteAdjustment ?? { id: null }),
    });

    return new FormGroup<QuoteAdjustmentFormGroupContent>({
      id: new FormControl(
        { value: quoteAdjustmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(quoteAdjustmentRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      comments: new FormControl(quoteAdjustmentRawValue.comments, {
        validators: [Validators.maxLength(2000)],
      }),
      amount: new FormControl(quoteAdjustmentRawValue.amount),
      sourcePercentage: new FormControl(quoteAdjustmentRawValue.sourcePercentage),
      sourceRefId: new FormControl(quoteAdjustmentRawValue.sourceRefId, {
        validators: [Validators.maxLength(60)],
      }),
      exemptAmount: new FormControl(quoteAdjustmentRawValue.exemptAmount),
      includeInTax: new FormControl(quoteAdjustmentRawValue.includeInTax),
      includeInShipping: new FormControl(quoteAdjustmentRawValue.includeInShipping),
      includedInPrice: new FormControl(quoteAdjustmentRawValue.includedInPrice),
      entryDate: new FormControl(quoteAdjustmentRawValue.entryDate),
      quote: new FormControl(quoteAdjustmentRawValue.quote),
      quoteItem: new FormControl(quoteAdjustmentRawValue.quoteItem),
      adjustmentType: new FormControl(quoteAdjustmentRawValue.adjustmentType),
      product: new FormControl(quoteAdjustmentRawValue.product),
      productFeature: new FormControl(quoteAdjustmentRawValue.productFeature),
      taxAuthority: new FormControl(quoteAdjustmentRawValue.taxAuthority),
    });
  }

  getQuoteAdjustment(form: QuoteAdjustmentFormGroup): IQuoteAdjustment | NewQuoteAdjustment {
    return this.convertQuoteAdjustmentRawValueToQuoteAdjustment(form.getRawValue());
  }

  resetForm(form: QuoteAdjustmentFormGroup, quoteAdjustment: QuoteAdjustmentFormGroupInput): void {
    const quoteAdjustmentRawValue = this.convertQuoteAdjustmentToQuoteAdjustmentRawValue({ ...this.getFormDefaults(), ...quoteAdjustment });
    form.reset({
      ...quoteAdjustmentRawValue,
      id: { value: quoteAdjustmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuoteAdjustmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      includeInTax: false,
      includeInShipping: false,
      includedInPrice: false,
      entryDate: currentTime,
    };
  }

  private convertQuoteAdjustmentRawValueToQuoteAdjustment(
    rawQuoteAdjustment: QuoteAdjustmentFormRawValue | NewQuoteAdjustmentFormRawValue,
  ): IQuoteAdjustment | NewQuoteAdjustment {
    return {
      ...rawQuoteAdjustment,
      entryDate: dayjs(rawQuoteAdjustment.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertQuoteAdjustmentToQuoteAdjustmentRawValue(
    quoteAdjustment: IQuoteAdjustment | (Partial<NewQuoteAdjustment> & QuoteAdjustmentFormDefaults),
  ): QuoteAdjustmentFormRawValue | PartialWithRequiredKeyOf<NewQuoteAdjustmentFormRawValue> {
    return {
      ...quoteAdjustment,
      entryDate: quoteAdjustment.entryDate ? quoteAdjustment.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
