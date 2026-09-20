import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICatalogueCategoryType, NewCatalogueCategoryType } from '../catalogue-category-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalogueCategoryType for edit and NewCatalogueCategoryTypeFormGroupInput for create.
 */
type CatalogueCategoryTypeFormGroupInput = ICatalogueCategoryType | PartialWithRequiredKeyOf<NewCatalogueCategoryType>;

type CatalogueCategoryTypeFormDefaults = Pick<NewCatalogueCategoryType, 'id'>;

type CatalogueCategoryTypeFormGroupContent = {
  id: FormControl<ICatalogueCategoryType['id'] | NewCatalogueCategoryType['id']>;
  name: FormControl<ICatalogueCategoryType['name']>;
  description: FormControl<ICatalogueCategoryType['description']>;
};

export type CatalogueCategoryTypeFormGroup = FormGroup<CatalogueCategoryTypeFormGroupContent>;

@Service()
export class CatalogueCategoryTypeFormService {
  createCatalogueCategoryTypeFormGroup(catalogueCategoryType?: CatalogueCategoryTypeFormGroupInput): CatalogueCategoryTypeFormGroup {
    const catalogueCategoryTypeRawValue = {
      ...this.getFormDefaults(),
      ...(catalogueCategoryType ?? { id: null }),
    };

    return new FormGroup<CatalogueCategoryTypeFormGroupContent>({
      id: new FormControl(
        { value: catalogueCategoryTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(catalogueCategoryTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(catalogueCategoryTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getCatalogueCategoryType(form: CatalogueCategoryTypeFormGroup): ICatalogueCategoryType | NewCatalogueCategoryType {
    return form.getRawValue();
  }

  resetForm(form: CatalogueCategoryTypeFormGroup, catalogueCategoryType: CatalogueCategoryTypeFormGroupInput): void {
    const catalogueCategoryTypeRawValue = { ...this.getFormDefaults(), ...catalogueCategoryType };
    form.reset({
      ...catalogueCategoryTypeRawValue,
      id: { value: catalogueCategoryTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CatalogueCategoryTypeFormDefaults {
    return {
      id: null,
    };
  }
}
