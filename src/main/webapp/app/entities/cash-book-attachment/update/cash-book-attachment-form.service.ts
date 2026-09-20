import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICashBookAttachment, NewCashBookAttachment } from '../cash-book-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICashBookAttachment for edit and NewCashBookAttachmentFormGroupInput for create.
 */
type CashBookAttachmentFormGroupInput = ICashBookAttachment | PartialWithRequiredKeyOf<NewCashBookAttachment>;

type CashBookAttachmentFormDefaults = Pick<NewCashBookAttachment, 'id'>;

type CashBookAttachmentFormGroupContent = {
  id: FormControl<ICashBookAttachment['id'] | NewCashBookAttachment['id']>;
  name: FormControl<ICashBookAttachment['name']>;
  cashBook: FormControl<ICashBookAttachment['cashBook']>;
  attachment: FormControl<ICashBookAttachment['attachment']>;
};

export type CashBookAttachmentFormGroup = FormGroup<CashBookAttachmentFormGroupContent>;

@Service()
export class CashBookAttachmentFormService {
  createCashBookAttachmentFormGroup(cashBookAttachment?: CashBookAttachmentFormGroupInput): CashBookAttachmentFormGroup {
    const cashBookAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(cashBookAttachment ?? { id: null }),
    };

    return new FormGroup<CashBookAttachmentFormGroupContent>({
      id: new FormControl(
        { value: cashBookAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(cashBookAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      cashBook: new FormControl(cashBookAttachmentRawValue.cashBook, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(cashBookAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getCashBookAttachment(form: CashBookAttachmentFormGroup): ICashBookAttachment | NewCashBookAttachment {
    return form.getRawValue();
  }

  resetForm(form: CashBookAttachmentFormGroup, cashBookAttachment: CashBookAttachmentFormGroupInput): void {
    const cashBookAttachmentRawValue = { ...this.getFormDefaults(), ...cashBookAttachment };
    form.reset({
      ...cashBookAttachmentRawValue,
      id: { value: cashBookAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CashBookAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
