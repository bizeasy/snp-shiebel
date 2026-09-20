import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuoteAttachment, NewQuoteAttachment } from '../quote-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuoteAttachment for edit and NewQuoteAttachmentFormGroupInput for create.
 */
type QuoteAttachmentFormGroupInput = IQuoteAttachment | PartialWithRequiredKeyOf<NewQuoteAttachment>;

type QuoteAttachmentFormDefaults = Pick<NewQuoteAttachment, 'id'>;

type QuoteAttachmentFormGroupContent = {
  id: FormControl<IQuoteAttachment['id'] | NewQuoteAttachment['id']>;
  name: FormControl<IQuoteAttachment['name']>;
  quote: FormControl<IQuoteAttachment['quote']>;
  attachment: FormControl<IQuoteAttachment['attachment']>;
};

export type QuoteAttachmentFormGroup = FormGroup<QuoteAttachmentFormGroupContent>;

@Service()
export class QuoteAttachmentFormService {
  createQuoteAttachmentFormGroup(quoteAttachment?: QuoteAttachmentFormGroupInput): QuoteAttachmentFormGroup {
    const quoteAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(quoteAttachment ?? { id: null }),
    };

    return new FormGroup<QuoteAttachmentFormGroupContent>({
      id: new FormControl(
        { value: quoteAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(quoteAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      quote: new FormControl(quoteAttachmentRawValue.quote, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(quoteAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getQuoteAttachment(form: QuoteAttachmentFormGroup): IQuoteAttachment | NewQuoteAttachment {
    return form.getRawValue();
  }

  resetForm(form: QuoteAttachmentFormGroup, quoteAttachment: QuoteAttachmentFormGroupInput): void {
    const quoteAttachmentRawValue = { ...this.getFormDefaults(), ...quoteAttachment };
    form.reset({
      ...quoteAttachmentRawValue,
      id: { value: quoteAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuoteAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
