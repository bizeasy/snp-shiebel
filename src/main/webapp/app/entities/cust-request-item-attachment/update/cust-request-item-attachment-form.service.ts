import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustRequestItemAttachment, NewCustRequestItemAttachment } from '../cust-request-item-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequestItemAttachment for edit and NewCustRequestItemAttachmentFormGroupInput for create.
 */
type CustRequestItemAttachmentFormGroupInput = ICustRequestItemAttachment | PartialWithRequiredKeyOf<NewCustRequestItemAttachment>;

type CustRequestItemAttachmentFormDefaults = Pick<NewCustRequestItemAttachment, 'id'>;

type CustRequestItemAttachmentFormGroupContent = {
  id: FormControl<ICustRequestItemAttachment['id'] | NewCustRequestItemAttachment['id']>;
  name: FormControl<ICustRequestItemAttachment['name']>;
  custRequestItem: FormControl<ICustRequestItemAttachment['custRequestItem']>;
  attachment: FormControl<ICustRequestItemAttachment['attachment']>;
};

export type CustRequestItemAttachmentFormGroup = FormGroup<CustRequestItemAttachmentFormGroupContent>;

@Service()
export class CustRequestItemAttachmentFormService {
  createCustRequestItemAttachmentFormGroup(
    custRequestItemAttachment?: CustRequestItemAttachmentFormGroupInput,
  ): CustRequestItemAttachmentFormGroup {
    const custRequestItemAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(custRequestItemAttachment ?? { id: null }),
    };

    return new FormGroup<CustRequestItemAttachmentFormGroupContent>({
      id: new FormControl(
        { value: custRequestItemAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(custRequestItemAttachmentRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      custRequestItem: new FormControl(custRequestItemAttachmentRawValue.custRequestItem, {
        validators: [Validators.required],
      }),
      attachment: new FormControl(custRequestItemAttachmentRawValue.attachment, {
        validators: [Validators.required],
      }),
    });
  }

  getCustRequestItemAttachment(form: CustRequestItemAttachmentFormGroup): ICustRequestItemAttachment | NewCustRequestItemAttachment {
    return form.getRawValue();
  }

  resetForm(form: CustRequestItemAttachmentFormGroup, custRequestItemAttachment: CustRequestItemAttachmentFormGroupInput): void {
    const custRequestItemAttachmentRawValue = { ...this.getFormDefaults(), ...custRequestItemAttachment };
    form.reset({
      ...custRequestItemAttachmentRawValue,
      id: { value: custRequestItemAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestItemAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
