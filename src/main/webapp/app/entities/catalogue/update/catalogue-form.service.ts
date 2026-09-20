import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICatalogue, NewCatalogue } from '../catalogue.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalogue for edit and NewCatalogueFormGroupInput for create.
 */
type CatalogueFormGroupInput = ICatalogue | PartialWithRequiredKeyOf<NewCatalogue>;

type CatalogueFormDefaults = Pick<NewCatalogue, 'id' | 'isBanner'>;

type CatalogueFormGroupContent = {
  id: FormControl<ICatalogue['id'] | NewCatalogue['id']>;
  name: FormControl<ICatalogue['name']>;
  description: FormControl<ICatalogue['description']>;
  sequenceNo: FormControl<ICatalogue['sequenceNo']>;
  imagePath: FormControl<ICatalogue['imagePath']>;
  mobileImagePath: FormControl<ICatalogue['mobileImagePath']>;
  altImage1: FormControl<ICatalogue['altImage1']>;
  altImage2: FormControl<ICatalogue['altImage2']>;
  altImage3: FormControl<ICatalogue['altImage3']>;
  bannerUrl: FormControl<ICatalogue['bannerUrl']>;
  isBanner: FormControl<ICatalogue['isBanner']>;
};

export type CatalogueFormGroup = FormGroup<CatalogueFormGroupContent>;

@Service()
export class CatalogueFormService {
  createCatalogueFormGroup(catalogue?: CatalogueFormGroupInput): CatalogueFormGroup {
    const catalogueRawValue = {
      ...this.getFormDefaults(),
      ...(catalogue ?? { id: null }),
    };

    return new FormGroup<CatalogueFormGroupContent>({
      id: new FormControl(
        { value: catalogueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(catalogueRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(catalogueRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      sequenceNo: new FormControl(catalogueRawValue.sequenceNo),
      imagePath: new FormControl(catalogueRawValue.imagePath, {
        validators: [Validators.maxLength(2000)],
      }),
      mobileImagePath: new FormControl(catalogueRawValue.mobileImagePath, {
        validators: [Validators.maxLength(2000)],
      }),
      altImage1: new FormControl(catalogueRawValue.altImage1, {
        validators: [Validators.maxLength(2000)],
      }),
      altImage2: new FormControl(catalogueRawValue.altImage2, {
        validators: [Validators.maxLength(2000)],
      }),
      altImage3: new FormControl(catalogueRawValue.altImage3, {
        validators: [Validators.maxLength(2000)],
      }),
      bannerUrl: new FormControl(catalogueRawValue.bannerUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      isBanner: new FormControl(catalogueRawValue.isBanner),
    });
  }

  getCatalogue(form: CatalogueFormGroup): ICatalogue | NewCatalogue {
    return form.getRawValue();
  }

  resetForm(form: CatalogueFormGroup, catalogue: CatalogueFormGroupInput): void {
    const catalogueRawValue = { ...this.getFormDefaults(), ...catalogue };
    form.reset({
      ...catalogueRawValue,
      id: { value: catalogueRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CatalogueFormDefaults {
    return {
      id: null,
      isBanner: false,
    };
  }
}
