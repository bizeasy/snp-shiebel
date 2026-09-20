import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITaxAuthorityRateType, NewTaxAuthorityRateType } from '../tax-authority-rate-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITaxAuthorityRateType for edit and NewTaxAuthorityRateTypeFormGroupInput for create.
 */
type TaxAuthorityRateTypeFormGroupInput = ITaxAuthorityRateType | PartialWithRequiredKeyOf<NewTaxAuthorityRateType>;

type TaxAuthorityRateTypeFormDefaults = Pick<NewTaxAuthorityRateType, 'id'>;

type TaxAuthorityRateTypeFormGroupContent = {
  id: FormControl<ITaxAuthorityRateType['id'] | NewTaxAuthorityRateType['id']>;
  name: FormControl<ITaxAuthorityRateType['name']>;
  description: FormControl<ITaxAuthorityRateType['description']>;
  taxAuthority: FormControl<ITaxAuthorityRateType['taxAuthority']>;
  taxSlab: FormControl<ITaxAuthorityRateType['taxSlab']>;
};

export type TaxAuthorityRateTypeFormGroup = FormGroup<TaxAuthorityRateTypeFormGroupContent>;

@Service()
export class TaxAuthorityRateTypeFormService {
  createTaxAuthorityRateTypeFormGroup(taxAuthorityRateType?: TaxAuthorityRateTypeFormGroupInput): TaxAuthorityRateTypeFormGroup {
    const taxAuthorityRateTypeRawValue = {
      ...this.getFormDefaults(),
      ...(taxAuthorityRateType ?? { id: null }),
    };

    return new FormGroup<TaxAuthorityRateTypeFormGroupContent>({
      id: new FormControl(
        { value: taxAuthorityRateTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(taxAuthorityRateTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(taxAuthorityRateTypeRawValue.description, {
        validators: [Validators.maxLength(60)],
      }),
      taxAuthority: new FormControl(taxAuthorityRateTypeRawValue.taxAuthority, {
        validators: [Validators.required],
      }),
      taxSlab: new FormControl(taxAuthorityRateTypeRawValue.taxSlab, {
        validators: [Validators.required],
      }),
    });
  }

  getTaxAuthorityRateType(form: TaxAuthorityRateTypeFormGroup): ITaxAuthorityRateType | NewTaxAuthorityRateType {
    return form.getRawValue();
  }

  resetForm(form: TaxAuthorityRateTypeFormGroup, taxAuthorityRateType: TaxAuthorityRateTypeFormGroupInput): void {
    const taxAuthorityRateTypeRawValue = { ...this.getFormDefaults(), ...taxAuthorityRateType };
    form.reset({
      ...taxAuthorityRateTypeRawValue,
      id: { value: taxAuthorityRateTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TaxAuthorityRateTypeFormDefaults {
    return {
      id: null,
    };
  }
}
