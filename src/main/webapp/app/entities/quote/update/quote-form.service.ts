import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IQuote, NewQuote } from '../quote.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuote for edit and NewQuoteFormGroupInput for create.
 */
type QuoteFormGroupInput = IQuote | PartialWithRequiredKeyOf<NewQuote>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuote | NewQuote> = Omit<T, 'issueDate' | 'validFromDate' | 'validThruDate'> & {
  issueDate?: string | null;
  validFromDate?: string | null;
  validThruDate?: string | null;
};

type QuoteFormRawValue = FormValueOf<IQuote>;

type NewQuoteFormRawValue = FormValueOf<NewQuote>;

type QuoteFormDefaults = Pick<NewQuote, 'id' | 'issueDate' | 'validFromDate' | 'validThruDate'>;

type QuoteFormGroupContent = {
  id: FormControl<QuoteFormRawValue['id'] | NewQuote['id']>;
  name: FormControl<QuoteFormRawValue['name']>;
  offerNo: FormControl<QuoteFormRawValue['offerNo']>;
  issueDate: FormControl<QuoteFormRawValue['issueDate']>;
  validFromDate: FormControl<QuoteFormRawValue['validFromDate']>;
  validThruDate: FormControl<QuoteFormRawValue['validThruDate']>;
  description: FormControl<QuoteFormRawValue['description']>;
  offerValue: FormControl<QuoteFormRawValue['offerValue']>;
  remarks: FormControl<QuoteFormRawValue['remarks']>;
  quoteType: FormControl<QuoteFormRawValue['quoteType']>;
  custRequest: FormControl<QuoteFormRawValue['custRequest']>;
  party: FormControl<QuoteFormRawValue['party']>;
  status: FormControl<QuoteFormRawValue['status']>;
  productStore: FormControl<QuoteFormRawValue['productStore']>;
  salesChannel: FormControl<QuoteFormRawValue['salesChannel']>;
  currencyUom: FormControl<QuoteFormRawValue['currencyUom']>;
  currencyConversion: FormControl<QuoteFormRawValue['currencyConversion']>;
};

export type QuoteFormGroup = FormGroup<QuoteFormGroupContent>;

@Service()
export class QuoteFormService {
  createQuoteFormGroup(quote?: QuoteFormGroupInput): QuoteFormGroup {
    const quoteRawValue = this.convertQuoteToQuoteRawValue({
      ...this.getFormDefaults(),
      ...(quote ?? { id: null }),
    });

    return new FormGroup<QuoteFormGroupContent>({
      id: new FormControl(
        { value: quoteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(quoteRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      offerNo: new FormControl(quoteRawValue.offerNo, {
        validators: [Validators.maxLength(60)],
      }),
      issueDate: new FormControl(quoteRawValue.issueDate, {
        validators: [Validators.required],
      }),
      validFromDate: new FormControl(quoteRawValue.validFromDate),
      validThruDate: new FormControl(quoteRawValue.validThruDate),
      description: new FormControl(quoteRawValue.description, {
        validators: [Validators.maxLength(2000)],
      }),
      offerValue: new FormControl(quoteRawValue.offerValue),
      remarks: new FormControl(quoteRawValue.remarks, {
        validators: [Validators.maxLength(2000)],
      }),
      quoteType: new FormControl(quoteRawValue.quoteType),
      custRequest: new FormControl(quoteRawValue.custRequest),
      party: new FormControl(quoteRawValue.party, {
        validators: [Validators.required],
      }),
      status: new FormControl(quoteRawValue.status),
      productStore: new FormControl(quoteRawValue.productStore),
      salesChannel: new FormControl(quoteRawValue.salesChannel),
      currencyUom: new FormControl(quoteRawValue.currencyUom),
      currencyConversion: new FormControl(quoteRawValue.currencyConversion),
    });
  }

  getQuote(form: QuoteFormGroup): IQuote | NewQuote {
    return this.convertQuoteRawValueToQuote(form.getRawValue());
  }

  resetForm(form: QuoteFormGroup, quote: QuoteFormGroupInput): void {
    const quoteRawValue = this.convertQuoteToQuoteRawValue({ ...this.getFormDefaults(), ...quote });
    form.reset({
      ...quoteRawValue,
      id: { value: quoteRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuoteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
      validFromDate: currentTime,
      validThruDate: currentTime,
    };
  }

  private convertQuoteRawValueToQuote(rawQuote: QuoteFormRawValue | NewQuoteFormRawValue): IQuote | NewQuote {
    return {
      ...rawQuote,
      issueDate: dayjs(rawQuote.issueDate, DATE_TIME_FORMAT),
      validFromDate: dayjs(rawQuote.validFromDate, DATE_TIME_FORMAT),
      validThruDate: dayjs(rawQuote.validThruDate, DATE_TIME_FORMAT),
    };
  }

  private convertQuoteToQuoteRawValue(
    quote: IQuote | (Partial<NewQuote> & QuoteFormDefaults),
  ): QuoteFormRawValue | PartialWithRequiredKeyOf<NewQuoteFormRawValue> {
    return {
      ...quote,
      issueDate: quote.issueDate ? quote.issueDate.format(DATE_TIME_FORMAT) : undefined,
      validFromDate: quote.validFromDate ? quote.validFromDate.format(DATE_TIME_FORMAT) : undefined,
      validThruDate: quote.validThruDate ? quote.validThruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
