import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IFileShare, NewFileShare } from '../file-share.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFileShare for edit and NewFileShareFormGroupInput for create.
 */
type FileShareFormGroupInput = IFileShare | PartialWithRequiredKeyOf<NewFileShare>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFileShare | NewFileShare> = Omit<T, 'sharedDate' | 'expiryDate'> & {
  sharedDate?: string | null;
  expiryDate?: string | null;
};

type FileShareFormRawValue = FormValueOf<IFileShare>;

type NewFileShareFormRawValue = FormValueOf<NewFileShare>;

type FileShareFormDefaults = Pick<NewFileShare, 'id' | 'sharedDate' | 'expiryDate' | 'isRevoked'>;

type FileShareFormGroupContent = {
  id: FormControl<FileShareFormRawValue['id'] | NewFileShare['id']>;
  permission: FormControl<FileShareFormRawValue['permission']>;
  sharedWithEmail: FormControl<FileShareFormRawValue['sharedWithEmail']>;
  sharedDate: FormControl<FileShareFormRawValue['sharedDate']>;
  expiryDate: FormControl<FileShareFormRawValue['expiryDate']>;
  accessToken: FormControl<FileShareFormRawValue['accessToken']>;
  isRevoked: FormControl<FileShareFormRawValue['isRevoked']>;
  folder: FormControl<FileShareFormRawValue['folder']>;
  attachment: FormControl<FileShareFormRawValue['attachment']>;
  sharedWith: FormControl<FileShareFormRawValue['sharedWith']>;
  sharedBy: FormControl<FileShareFormRawValue['sharedBy']>;
};

export type FileShareFormGroup = FormGroup<FileShareFormGroupContent>;

@Service()
export class FileShareFormService {
  createFileShareFormGroup(fileShare?: FileShareFormGroupInput): FileShareFormGroup {
    const fileShareRawValue = this.convertFileShareToFileShareRawValue({
      ...this.getFormDefaults(),
      ...(fileShare ?? { id: null }),
    });

    return new FormGroup<FileShareFormGroupContent>({
      id: new FormControl(
        { value: fileShareRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      permission: new FormControl(fileShareRawValue.permission, {
        validators: [Validators.required],
      }),
      sharedWithEmail: new FormControl(fileShareRawValue.sharedWithEmail, {
        validators: [Validators.minLength(5), Validators.maxLength(100)],
      }),
      sharedDate: new FormControl(fileShareRawValue.sharedDate, {
        validators: [Validators.required],
      }),
      expiryDate: new FormControl(fileShareRawValue.expiryDate),
      accessToken: new FormControl(fileShareRawValue.accessToken, {
        validators: [Validators.maxLength(100)],
      }),
      isRevoked: new FormControl(fileShareRawValue.isRevoked),
      folder: new FormControl(fileShareRawValue.folder),
      attachment: new FormControl(fileShareRawValue.attachment),
      sharedWith: new FormControl(fileShareRawValue.sharedWith),
      sharedBy: new FormControl(fileShareRawValue.sharedBy),
    });
  }

  getFileShare(form: FileShareFormGroup): IFileShare | NewFileShare {
    return this.convertFileShareRawValueToFileShare(form.getRawValue());
  }

  resetForm(form: FileShareFormGroup, fileShare: FileShareFormGroupInput): void {
    const fileShareRawValue = this.convertFileShareToFileShareRawValue({ ...this.getFormDefaults(), ...fileShare });
    form.reset({
      ...fileShareRawValue,
      id: { value: fileShareRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FileShareFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      sharedDate: currentTime,
      expiryDate: currentTime,
      isRevoked: false,
    };
  }

  private convertFileShareRawValueToFileShare(rawFileShare: FileShareFormRawValue | NewFileShareFormRawValue): IFileShare | NewFileShare {
    return {
      ...rawFileShare,
      sharedDate: dayjs(rawFileShare.sharedDate, DATE_TIME_FORMAT),
      expiryDate: dayjs(rawFileShare.expiryDate, DATE_TIME_FORMAT),
    };
  }

  private convertFileShareToFileShareRawValue(
    fileShare: IFileShare | (Partial<NewFileShare> & FileShareFormDefaults),
  ): FileShareFormRawValue | PartialWithRequiredKeyOf<NewFileShareFormRawValue> {
    return {
      ...fileShare,
      sharedDate: fileShare.sharedDate ? fileShare.sharedDate.format(DATE_TIME_FORMAT) : undefined,
      expiryDate: fileShare.expiryDate ? fileShare.expiryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
