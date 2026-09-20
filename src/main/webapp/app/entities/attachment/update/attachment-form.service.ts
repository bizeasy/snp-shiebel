import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IAttachment, NewAttachment } from '../attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttachment for edit and NewAttachmentFormGroupInput for create.
 */
type AttachmentFormGroupInput = IAttachment | PartialWithRequiredKeyOf<NewAttachment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAttachment | NewAttachment> = Omit<T, 'uploadedDate'> & {
  uploadedDate?: string | null;
};

type AttachmentFormRawValue = FormValueOf<IAttachment>;

type NewAttachmentFormRawValue = FormValueOf<NewAttachment>;

type AttachmentFormDefaults = Pick<NewAttachment, 'id' | 'uploadedDate' | 'isTrashed'>;

type AttachmentFormGroupContent = {
  id: FormControl<AttachmentFormRawValue['id'] | NewAttachment['id']>;
  name: FormControl<AttachmentFormRawValue['name']>;
  fileAttachment: FormControl<AttachmentFormRawValue['fileAttachment']>;
  fileAttachmentContentType: FormControl<AttachmentFormRawValue['fileAttachmentContentType']>;
  attachmentUrl: FormControl<AttachmentFormRawValue['attachmentUrl']>;
  mimeType: FormControl<AttachmentFormRawValue['mimeType']>;
  fileSize: FormControl<AttachmentFormRawValue['fileSize']>;
  uploadedDate: FormControl<AttachmentFormRawValue['uploadedDate']>;
  isTrashed: FormControl<AttachmentFormRawValue['isTrashed']>;
  checksum: FormControl<AttachmentFormRawValue['checksum']>;
  contentType: FormControl<AttachmentFormRawValue['contentType']>;
  folder: FormControl<AttachmentFormRawValue['folder']>;
  owner: FormControl<AttachmentFormRawValue['owner']>;
};

export type AttachmentFormGroup = FormGroup<AttachmentFormGroupContent>;

@Service()
export class AttachmentFormService {
  createAttachmentFormGroup(attachment?: AttachmentFormGroupInput): AttachmentFormGroup {
    const attachmentRawValue = this.convertAttachmentToAttachmentRawValue({
      ...this.getFormDefaults(),
      ...(attachment ?? { id: null }),
    });

    return new FormGroup<AttachmentFormGroupContent>({
      id: new FormControl(
        { value: attachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(attachmentRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      fileAttachment: new FormControl(attachmentRawValue.fileAttachment),
      fileAttachmentContentType: new FormControl(attachmentRawValue.fileAttachmentContentType),
      attachmentUrl: new FormControl(attachmentRawValue.attachmentUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      mimeType: new FormControl(attachmentRawValue.mimeType, {
        validators: [Validators.maxLength(100)],
      }),
      fileSize: new FormControl(attachmentRawValue.fileSize),
      uploadedDate: new FormControl(attachmentRawValue.uploadedDate),
      isTrashed: new FormControl(attachmentRawValue.isTrashed),
      checksum: new FormControl(attachmentRawValue.checksum, {
        validators: [Validators.maxLength(128)],
      }),
      contentType: new FormControl(attachmentRawValue.contentType),
      folder: new FormControl(attachmentRawValue.folder),
      owner: new FormControl(attachmentRawValue.owner),
    });
  }

  getAttachment(form: AttachmentFormGroup): IAttachment | NewAttachment {
    return this.convertAttachmentRawValueToAttachment(form.getRawValue());
  }

  resetForm(form: AttachmentFormGroup, attachment: AttachmentFormGroupInput): void {
    const attachmentRawValue = this.convertAttachmentToAttachmentRawValue({ ...this.getFormDefaults(), ...attachment });
    form.reset({
      ...attachmentRawValue,
      id: { value: attachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AttachmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadedDate: currentTime,
      isTrashed: false,
    };
  }

  private convertAttachmentRawValueToAttachment(
    rawAttachment: AttachmentFormRawValue | NewAttachmentFormRawValue,
  ): IAttachment | NewAttachment {
    return {
      ...rawAttachment,
      uploadedDate: dayjs(rawAttachment.uploadedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAttachmentToAttachmentRawValue(
    attachment: IAttachment | (Partial<NewAttachment> & AttachmentFormDefaults),
  ): AttachmentFormRawValue | PartialWithRequiredKeyOf<NewAttachmentFormRawValue> {
    return {
      ...attachment,
      uploadedDate: attachment.uploadedDate ? attachment.uploadedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
