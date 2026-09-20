import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITaxSlab, NewTaxSlab } from '../tax-slab.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITaxSlab for edit and NewTaxSlabFormGroupInput for create.
 */
type TaxSlabFormGroupInput = ITaxSlab | PartialWithRequiredKeyOf<NewTaxSlab>;

type TaxSlabFormDefaults = Pick<NewTaxSlab, 'id'>;

type TaxSlabFormGroupContent = {
  id: FormControl<ITaxSlab['id'] | NewTaxSlab['id']>;
  name: FormControl<ITaxSlab['name']>;
  rate: FormControl<ITaxSlab['rate']>;
};

export type TaxSlabFormGroup = FormGroup<TaxSlabFormGroupContent>;

@Service()
export class TaxSlabFormService {
  createTaxSlabFormGroup(taxSlab?: TaxSlabFormGroupInput): TaxSlabFormGroup {
    const taxSlabRawValue = {
      ...this.getFormDefaults(),
      ...(taxSlab ?? { id: null }),
    };

    return new FormGroup<TaxSlabFormGroupContent>({
      id: new FormControl(
        { value: taxSlabRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(taxSlabRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      rate: new FormControl(taxSlabRawValue.rate, {
        validators: [Validators.required],
      }),
    });
  }

  getTaxSlab(form: TaxSlabFormGroup): ITaxSlab | NewTaxSlab {
    return form.getRawValue();
  }

  resetForm(form: TaxSlabFormGroup, taxSlab: TaxSlabFormGroupInput): void {
    const taxSlabRawValue = { ...this.getFormDefaults(), ...taxSlab };
    form.reset({
      ...taxSlabRawValue,
      id: { value: taxSlabRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TaxSlabFormDefaults {
    return {
      id: null,
    };
  }
}
