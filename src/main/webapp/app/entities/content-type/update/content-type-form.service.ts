import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContentType, NewContentType } from '../content-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContentType for edit and NewContentTypeFormGroupInput for create.
 */
type ContentTypeFormGroupInput = IContentType | PartialWithRequiredKeyOf<NewContentType>;

type ContentTypeFormDefaults = Pick<NewContentType, 'id'>;

type ContentTypeFormGroupContent = {
  id: FormControl<IContentType['id'] | NewContentType['id']>;
  name: FormControl<IContentType['name']>;
};

export type ContentTypeFormGroup = FormGroup<ContentTypeFormGroupContent>;

@Service()
export class ContentTypeFormService {
  createContentTypeFormGroup(contentType?: ContentTypeFormGroupInput): ContentTypeFormGroup {
    const contentTypeRawValue = {
      ...this.getFormDefaults(),
      ...(contentType ?? { id: null }),
    };

    return new FormGroup<ContentTypeFormGroupContent>({
      id: new FormControl(
        { value: contentTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(contentTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
    });
  }

  getContentType(form: ContentTypeFormGroup): IContentType | NewContentType {
    return form.getRawValue();
  }

  resetForm(form: ContentTypeFormGroup, contentType: ContentTypeFormGroupInput): void {
    const contentTypeRawValue = { ...this.getFormDefaults(), ...contentType };
    form.reset({
      ...contentTypeRawValue,
      id: { value: contentTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContentTypeFormDefaults {
    return {
      id: null,
    };
  }
}
