import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuoteRole, NewQuoteRole } from '../quote-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuoteRole for edit and NewQuoteRoleFormGroupInput for create.
 */
type QuoteRoleFormGroupInput = IQuoteRole | PartialWithRequiredKeyOf<NewQuoteRole>;

type QuoteRoleFormDefaults = Pick<NewQuoteRole, 'id'>;

type QuoteRoleFormGroupContent = {
  id: FormControl<IQuoteRole['id'] | NewQuoteRole['id']>;
  quote: FormControl<IQuoteRole['quote']>;
  quoteItem: FormControl<IQuoteRole['quoteItem']>;
  party: FormControl<IQuoteRole['party']>;
  roleType: FormControl<IQuoteRole['roleType']>;
};

export type QuoteRoleFormGroup = FormGroup<QuoteRoleFormGroupContent>;

@Service()
export class QuoteRoleFormService {
  createQuoteRoleFormGroup(quoteRole?: QuoteRoleFormGroupInput): QuoteRoleFormGroup {
    const quoteRoleRawValue = {
      ...this.getFormDefaults(),
      ...(quoteRole ?? { id: null }),
    };

    return new FormGroup<QuoteRoleFormGroupContent>({
      id: new FormControl(
        { value: quoteRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quote: new FormControl(quoteRoleRawValue.quote),
      quoteItem: new FormControl(quoteRoleRawValue.quoteItem),
      party: new FormControl(quoteRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(quoteRoleRawValue.roleType),
    });
  }

  getQuoteRole(form: QuoteRoleFormGroup): IQuoteRole | NewQuoteRole {
    return form.getRawValue();
  }

  resetForm(form: QuoteRoleFormGroup, quoteRole: QuoteRoleFormGroupInput): void {
    const quoteRoleRawValue = { ...this.getFormDefaults(), ...quoteRole };
    form.reset({
      ...quoteRoleRawValue,
      id: { value: quoteRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuoteRoleFormDefaults {
    return {
      id: null,
    };
  }
}
