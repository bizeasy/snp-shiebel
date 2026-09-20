import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuoteType, NewQuoteType } from '../quote-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuoteType for edit and NewQuoteTypeFormGroupInput for create.
 */
type QuoteTypeFormGroupInput = IQuoteType | PartialWithRequiredKeyOf<NewQuoteType>;

type QuoteTypeFormDefaults = Pick<NewQuoteType, 'id'>;

type QuoteTypeFormGroupContent = {
  id: FormControl<IQuoteType['id'] | NewQuoteType['id']>;
  name: FormControl<IQuoteType['name']>;
  description: FormControl<IQuoteType['description']>;
};

export type QuoteTypeFormGroup = FormGroup<QuoteTypeFormGroupContent>;

@Service()
export class QuoteTypeFormService {
  createQuoteTypeFormGroup(quoteType?: QuoteTypeFormGroupInput): QuoteTypeFormGroup {
    const quoteTypeRawValue = {
      ...this.getFormDefaults(),
      ...(quoteType ?? { id: null }),
    };

    return new FormGroup<QuoteTypeFormGroupContent>({
      id: new FormControl(
        { value: quoteTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(quoteTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(quoteTypeRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getQuoteType(form: QuoteTypeFormGroup): IQuoteType | NewQuoteType {
    return form.getRawValue();
  }

  resetForm(form: QuoteTypeFormGroup, quoteType: QuoteTypeFormGroupInput): void {
    const quoteTypeRawValue = { ...this.getFormDefaults(), ...quoteType };
    form.reset({
      ...quoteTypeRawValue,
      id: { value: quoteTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuoteTypeFormDefaults {
    return {
      id: null,
    };
  }
}
