import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProductKeyword, NewProductKeyword } from '../product-keyword.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductKeyword for edit and NewProductKeywordFormGroupInput for create.
 */
type ProductKeywordFormGroupInput = IProductKeyword | PartialWithRequiredKeyOf<NewProductKeyword>;

type ProductKeywordFormDefaults = Pick<NewProductKeyword, 'id'>;

type ProductKeywordFormGroupContent = {
  id: FormControl<IProductKeyword['id'] | NewProductKeyword['id']>;
  keyword: FormControl<IProductKeyword['keyword']>;
  relevancyWeight: FormControl<IProductKeyword['relevancyWeight']>;
  product: FormControl<IProductKeyword['product']>;
  keywordType: FormControl<IProductKeyword['keywordType']>;
};

export type ProductKeywordFormGroup = FormGroup<ProductKeywordFormGroupContent>;

@Service()
export class ProductKeywordFormService {
  createProductKeywordFormGroup(productKeyword?: ProductKeywordFormGroupInput): ProductKeywordFormGroup {
    const productKeywordRawValue = {
      ...this.getFormDefaults(),
      ...(productKeyword ?? { id: null }),
    };

    return new FormGroup<ProductKeywordFormGroupContent>({
      id: new FormControl(
        { value: productKeywordRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      keyword: new FormControl(productKeywordRawValue.keyword, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      relevancyWeight: new FormControl(productKeywordRawValue.relevancyWeight),
      product: new FormControl(productKeywordRawValue.product, {
        validators: [Validators.required],
      }),
      keywordType: new FormControl(productKeywordRawValue.keywordType),
    });
  }

  getProductKeyword(form: ProductKeywordFormGroup): IProductKeyword | NewProductKeyword {
    return form.getRawValue();
  }

  resetForm(form: ProductKeywordFormGroup, productKeyword: ProductKeywordFormGroupInput): void {
    const productKeywordRawValue = { ...this.getFormDefaults(), ...productKeyword };
    form.reset({
      ...productKeywordRawValue,
      id: { value: productKeywordRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductKeywordFormDefaults {
    return {
      id: null,
    };
  }
}
