import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustRequestAttachment, NewCustRequestAttachment } from '../cust-request-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequestAttachment for edit and NewCustRequestAttachmentFormGroupInput for create.
 */
type CustRequestAttachmentFormGroupInput = ICustRequestAttachment | PartialWithRequiredKeyOf<NewCustRequestAttachment>;

type CustRequestAttachmentFormDefaults = Pick<NewCustRequestAttachment, 'id'>;

type CustRequestAttachmentFormGroupContent = {
  id: FormControl<ICustRequestAttachment['id'] | NewCustRequestAttachment['id']>;
  name: FormControl<ICustRequestAttachment['name']>;
  custRequest: FormControl<ICustRequestAttachment['custRequest']>;
  attachment: FormControl<ICustRequestAttachment['attachment']>;
};

export type CustRequestAttachmentFormGroup = FormGroup<CustRequestAttachmentFormGroupContent>;

@Service()
export class CustRequestAttachmentFormService {
  createCustRequestAttachmentFormGroup(custRequestAttachment?: CustRequestAttachmentFormGroupInput): CustRequestAttachmentFormGroup {
    const custRequestAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(custRequestAttachment ?? { id: null }),
    };

    return new FormGroup<CustRequestAttachmentFormGroupContent>({
      id: new FormControl(
        { value: custRequestAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(custRequestAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      custRequest: new FormControl(custRequestAttachmentRawValue.custRequest, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(custRequestAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getCustRequestAttachment(form: CustRequestAttachmentFormGroup): ICustRequestAttachment | NewCustRequestAttachment {
    return form.getRawValue();
  }

  resetForm(form: CustRequestAttachmentFormGroup, custRequestAttachment: CustRequestAttachmentFormGroupInput): void {
    const custRequestAttachmentRawValue = { ...this.getFormDefaults(), ...custRequestAttachment };
    form.reset({
      ...custRequestAttachmentRawValue,
      id: { value: custRequestAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
