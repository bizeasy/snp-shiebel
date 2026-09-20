import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IKeywordType, NewKeywordType } from '../keyword-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IKeywordType for edit and NewKeywordTypeFormGroupInput for create.
 */
type KeywordTypeFormGroupInput = IKeywordType | PartialWithRequiredKeyOf<NewKeywordType>;

type KeywordTypeFormDefaults = Pick<NewKeywordType, 'id'>;

type KeywordTypeFormGroupContent = {
  id: FormControl<IKeywordType['id'] | NewKeywordType['id']>;
  name: FormControl<IKeywordType['name']>;
};

export type KeywordTypeFormGroup = FormGroup<KeywordTypeFormGroupContent>;

@Service()
export class KeywordTypeFormService {
  createKeywordTypeFormGroup(keywordType?: KeywordTypeFormGroupInput): KeywordTypeFormGroup {
    const keywordTypeRawValue = {
      ...this.getFormDefaults(),
      ...(keywordType ?? { id: null }),
    };

    return new FormGroup<KeywordTypeFormGroupContent>({
      id: new FormControl(
        { value: keywordTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(keywordTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
    });
  }

  getKeywordType(form: KeywordTypeFormGroup): IKeywordType | NewKeywordType {
    return form.getRawValue();
  }

  resetForm(form: KeywordTypeFormGroup, keywordType: KeywordTypeFormGroupInput): void {
    const keywordTypeRawValue = { ...this.getFormDefaults(), ...keywordType };
    form.reset({
      ...keywordTypeRawValue,
      id: { value: keywordTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): KeywordTypeFormDefaults {
    return {
      id: null,
    };
  }
}
