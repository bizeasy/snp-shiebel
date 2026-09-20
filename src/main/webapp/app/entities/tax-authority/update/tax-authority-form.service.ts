import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITaxAuthority, NewTaxAuthority } from '../tax-authority.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITaxAuthority for edit and NewTaxAuthorityFormGroupInput for create.
 */
type TaxAuthorityFormGroupInput = ITaxAuthority | PartialWithRequiredKeyOf<NewTaxAuthority>;

type TaxAuthorityFormDefaults = Pick<NewTaxAuthority, 'id'>;

type TaxAuthorityFormGroupContent = {
  id: FormControl<ITaxAuthority['id'] | NewTaxAuthority['id']>;
  name: FormControl<ITaxAuthority['name']>;
  geo: FormControl<ITaxAuthority['geo']>;
};

export type TaxAuthorityFormGroup = FormGroup<TaxAuthorityFormGroupContent>;

@Service()
export class TaxAuthorityFormService {
  createTaxAuthorityFormGroup(taxAuthority?: TaxAuthorityFormGroupInput): TaxAuthorityFormGroup {
    const taxAuthorityRawValue = {
      ...this.getFormDefaults(),
      ...(taxAuthority ?? { id: null }),
    };

    return new FormGroup<TaxAuthorityFormGroupContent>({
      id: new FormControl(
        { value: taxAuthorityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(taxAuthorityRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      geo: new FormControl(taxAuthorityRawValue.geo),
    });
  }

  getTaxAuthority(form: TaxAuthorityFormGroup): ITaxAuthority | NewTaxAuthority {
    return form.getRawValue();
  }

  resetForm(form: TaxAuthorityFormGroup, taxAuthority: TaxAuthorityFormGroupInput): void {
    const taxAuthorityRawValue = { ...this.getFormDefaults(), ...taxAuthority };
    form.reset({
      ...taxAuthorityRawValue,
      id: { value: taxAuthorityRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TaxAuthorityFormDefaults {
    return {
      id: null,
    };
  }
}
