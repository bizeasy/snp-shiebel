import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductCategory, NewProductCategory } from '../product-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductCategory for edit and NewProductCategoryFormGroupInput for create.
 */
type ProductCategoryFormGroupInput = IProductCategory | PartialWithRequiredKeyOf<NewProductCategory>;

type ProductCategoryFormDefaults = Pick<NewProductCategory, 'id' | 'isBanner'>;

type ProductCategoryFormGroupContent = {
  id: FormControl<IProductCategory['id'] | NewProductCategory['id']>;
  name: FormControl<IProductCategory['name']>;
  description: FormControl<IProductCategory['description']>;
  longDescription: FormControl<IProductCategory['longDescription']>;
  attribute: FormControl<IProductCategory['attribute']>;
  sequenceNo: FormControl<IProductCategory['sequenceNo']>;
  imageUrl: FormControl<IProductCategory['imageUrl']>;
  altImageUrl: FormControl<IProductCategory['altImageUrl']>;
  bannerUrl: FormControl<IProductCategory['bannerUrl']>;
  largeImageUrl: FormControl<IProductCategory['largeImageUrl']>;
  altLargeImageUrl: FormControl<IProductCategory['altLargeImageUrl']>;
  info: FormControl<IProductCategory['info']>;
  isBanner: FormControl<IProductCategory['isBanner']>;
  productCategoryType: FormControl<IProductCategory['productCategoryType']>;
  parent: FormControl<IProductCategory['parent']>;
};

export type ProductCategoryFormGroup = FormGroup<ProductCategoryFormGroupContent>;

@Service()
export class ProductCategoryFormService {
  createProductCategoryFormGroup(productCategory?: ProductCategoryFormGroupInput): ProductCategoryFormGroup {
    const productCategoryRawValue = {
      ...this.getFormDefaults(),
      ...(productCategory ?? { id: null }),
    };

    return new FormGroup<ProductCategoryFormGroupContent>({
      id: new FormControl(
        { value: productCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(productCategoryRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      longDescription: new FormControl(productCategoryRawValue.longDescription, {
        validators: [Validators.maxLength(2000)],
      }),
      attribute: new FormControl(productCategoryRawValue.attribute, {
        validators: [Validators.maxLength(25)],
      }),
      sequenceNo: new FormControl(productCategoryRawValue.sequenceNo),
      imageUrl: new FormControl(productCategoryRawValue.imageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl: new FormControl(productCategoryRawValue.altImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      bannerUrl: new FormControl(productCategoryRawValue.bannerUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      largeImageUrl: new FormControl(productCategoryRawValue.largeImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      altLargeImageUrl: new FormControl(productCategoryRawValue.altLargeImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      info: new FormControl(productCategoryRawValue.info),
      isBanner: new FormControl(productCategoryRawValue.isBanner),
      productCategoryType: new FormControl(productCategoryRawValue.productCategoryType),
      parent: new FormControl(productCategoryRawValue.parent),
    });
  }

  getProductCategory(form: ProductCategoryFormGroup): IProductCategory | NewProductCategory {
    return form.getRawValue();
  }

  resetForm(form: ProductCategoryFormGroup, productCategory: ProductCategoryFormGroupInput): void {
    const productCategoryRawValue = { ...this.getFormDefaults(), ...productCategory };
    form.reset({
      ...productCategoryRawValue,
      id: { value: productCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductCategoryFormDefaults {
    return {
      id: null,
      isBanner: false,
    };
  }
}
