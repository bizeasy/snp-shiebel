import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IFileFolder, NewFileFolder } from '../file-folder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFileFolder for edit and NewFileFolderFormGroupInput for create.
 */
type FileFolderFormGroupInput = IFileFolder | PartialWithRequiredKeyOf<NewFileFolder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFileFolder | NewFileFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type FileFolderFormRawValue = FormValueOf<IFileFolder>;

type NewFileFolderFormRawValue = FormValueOf<NewFileFolder>;

type FileFolderFormDefaults = Pick<NewFileFolder, 'id' | 'isSystemFolder' | 'isTrashed' | 'createdDate'>;

type FileFolderFormGroupContent = {
  id: FormControl<FileFolderFormRawValue['id'] | NewFileFolder['id']>;
  name: FormControl<FileFolderFormRawValue['name']>;
  path: FormControl<FileFolderFormRawValue['path']>;
  isSystemFolder: FormControl<FileFolderFormRawValue['isSystemFolder']>;
  isTrashed: FormControl<FileFolderFormRawValue['isTrashed']>;
  createdDate: FormControl<FileFolderFormRawValue['createdDate']>;
  parent: FormControl<FileFolderFormRawValue['parent']>;
  owner: FormControl<FileFolderFormRawValue['owner']>;
};

export type FileFolderFormGroup = FormGroup<FileFolderFormGroupContent>;

@Service()
export class FileFolderFormService {
  createFileFolderFormGroup(fileFolder?: FileFolderFormGroupInput): FileFolderFormGroup {
    const fileFolderRawValue = this.convertFileFolderToFileFolderRawValue({
      ...this.getFormDefaults(),
      ...(fileFolder ?? { id: null }),
    });

    return new FormGroup<FileFolderFormGroupContent>({
      id: new FormControl(
        { value: fileFolderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(fileFolderRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      path: new FormControl(fileFolderRawValue.path, {
        validators: [Validators.maxLength(2000)],
      }),
      isSystemFolder: new FormControl(fileFolderRawValue.isSystemFolder),
      isTrashed: new FormControl(fileFolderRawValue.isTrashed),
      createdDate: new FormControl(fileFolderRawValue.createdDate),
      parent: new FormControl(fileFolderRawValue.parent),
      owner: new FormControl(fileFolderRawValue.owner),
    });
  }

  getFileFolder(form: FileFolderFormGroup): IFileFolder | NewFileFolder {
    return this.convertFileFolderRawValueToFileFolder(form.getRawValue());
  }

  resetForm(form: FileFolderFormGroup, fileFolder: FileFolderFormGroupInput): void {
    const fileFolderRawValue = this.convertFileFolderToFileFolderRawValue({ ...this.getFormDefaults(), ...fileFolder });
    form.reset({
      ...fileFolderRawValue,
      id: { value: fileFolderRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FileFolderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystemFolder: false,
      isTrashed: false,
      createdDate: currentTime,
    };
  }

  private convertFileFolderRawValueToFileFolder(
    rawFileFolder: FileFolderFormRawValue | NewFileFolderFormRawValue,
  ): IFileFolder | NewFileFolder {
    return {
      ...rawFileFolder,
      createdDate: dayjs(rawFileFolder.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertFileFolderToFileFolderRawValue(
    fileFolder: IFileFolder | (Partial<NewFileFolder> & FileFolderFormDefaults),
  ): FileFolderFormRawValue | PartialWithRequiredKeyOf<NewFileFolderFormRawValue> {
    return {
      ...fileFolder,
      createdDate: fileFolder.createdDate ? fileFolder.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
