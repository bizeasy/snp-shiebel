import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustRequestCategory, NewCustRequestCategory } from '../cust-request-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequestCategory for edit and NewCustRequestCategoryFormGroupInput for create.
 */
type CustRequestCategoryFormGroupInput = ICustRequestCategory | PartialWithRequiredKeyOf<NewCustRequestCategory>;

type CustRequestCategoryFormDefaults = Pick<NewCustRequestCategory, 'id'>;

type CustRequestCategoryFormGroupContent = {
  id: FormControl<ICustRequestCategory['id'] | NewCustRequestCategory['id']>;
  name: FormControl<ICustRequestCategory['name']>;
  description: FormControl<ICustRequestCategory['description']>;
  custRequestType: FormControl<ICustRequestCategory['custRequestType']>;
};

export type CustRequestCategoryFormGroup = FormGroup<CustRequestCategoryFormGroupContent>;

@Service()
export class CustRequestCategoryFormService {
  createCustRequestCategoryFormGroup(custRequestCategory?: CustRequestCategoryFormGroupInput): CustRequestCategoryFormGroup {
    const custRequestCategoryRawValue = {
      ...this.getFormDefaults(),
      ...(custRequestCategory ?? { id: null }),
    };

    return new FormGroup<CustRequestCategoryFormGroupContent>({
      id: new FormControl(
        { value: custRequestCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(custRequestCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(custRequestCategoryRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      custRequestType: new FormControl(custRequestCategoryRawValue.custRequestType),
    });
  }

  getCustRequestCategory(form: CustRequestCategoryFormGroup): ICustRequestCategory | NewCustRequestCategory {
    return form.getRawValue();
  }

  resetForm(form: CustRequestCategoryFormGroup, custRequestCategory: CustRequestCategoryFormGroupInput): void {
    const custRequestCategoryRawValue = { ...this.getFormDefaults(), ...custRequestCategory };
    form.reset({
      ...custRequestCategoryRawValue,
      id: { value: custRequestCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
