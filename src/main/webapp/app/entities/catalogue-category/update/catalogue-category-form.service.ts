import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICatalogueCategory, NewCatalogueCategory } from '../catalogue-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalogueCategory for edit and NewCatalogueCategoryFormGroupInput for create.
 */
type CatalogueCategoryFormGroupInput = ICatalogueCategory | PartialWithRequiredKeyOf<NewCatalogueCategory>;

type CatalogueCategoryFormDefaults = Pick<NewCatalogueCategory, 'id'>;

type CatalogueCategoryFormGroupContent = {
  id: FormControl<ICatalogueCategory['id'] | NewCatalogueCategory['id']>;
  sequenceNo: FormControl<ICatalogueCategory['sequenceNo']>;
  catalogue: FormControl<ICatalogueCategory['catalogue']>;
  productCategory: FormControl<ICatalogueCategory['productCategory']>;
  catalogueCategoryType: FormControl<ICatalogueCategory['catalogueCategoryType']>;
};

export type CatalogueCategoryFormGroup = FormGroup<CatalogueCategoryFormGroupContent>;

@Service()
export class CatalogueCategoryFormService {
  createCatalogueCategoryFormGroup(catalogueCategory?: CatalogueCategoryFormGroupInput): CatalogueCategoryFormGroup {
    const catalogueCategoryRawValue = {
      ...this.getFormDefaults(),
      ...(catalogueCategory ?? { id: null }),
    };

    return new FormGroup<CatalogueCategoryFormGroupContent>({
      id: new FormControl(
        { value: catalogueCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(catalogueCategoryRawValue.sequenceNo),
      catalogue: new FormControl(catalogueCategoryRawValue.catalogue, {
        validators: [Validators.required],
      }),
      productCategory: new FormControl(catalogueCategoryRawValue.productCategory, {
        validators: [Validators.required],
      }),
      catalogueCategoryType: new FormControl(catalogueCategoryRawValue.catalogueCategoryType),
    });
  }

  getCatalogueCategory(form: CatalogueCategoryFormGroup): ICatalogueCategory | NewCatalogueCategory {
    return form.getRawValue();
  }

  resetForm(form: CatalogueCategoryFormGroup, catalogueCategory: CatalogueCategoryFormGroupInput): void {
    const catalogueCategoryRawValue = { ...this.getFormDefaults(), ...catalogueCategory };
    form.reset({
      ...catalogueCategoryRawValue,
      id: { value: catalogueCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CatalogueCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
