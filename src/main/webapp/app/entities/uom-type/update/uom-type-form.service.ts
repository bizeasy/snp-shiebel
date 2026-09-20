import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUomType, NewUomType } from '../uom-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUomType for edit and NewUomTypeFormGroupInput for create.
 */
type UomTypeFormGroupInput = IUomType | PartialWithRequiredKeyOf<NewUomType>;

type UomTypeFormDefaults = Pick<NewUomType, 'id'>;

type UomTypeFormGroupContent = {
  id: FormControl<IUomType['id'] | NewUomType['id']>;
  name: FormControl<IUomType['name']>;
  description: FormControl<IUomType['description']>;
};

export type UomTypeFormGroup = FormGroup<UomTypeFormGroupContent>;

@Service()
export class UomTypeFormService {
  createUomTypeFormGroup(uomType?: UomTypeFormGroupInput): UomTypeFormGroup {
    const uomTypeRawValue = {
      ...this.getFormDefaults(),
      ...(uomType ?? { id: null }),
    };

    return new FormGroup<UomTypeFormGroupContent>({
      id: new FormControl(
        { value: uomTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(uomTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(uomTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getUomType(form: UomTypeFormGroup): IUomType | NewUomType {
    return form.getRawValue();
  }

  resetForm(form: UomTypeFormGroup, uomType: UomTypeFormGroupInput): void {
    const uomTypeRawValue = { ...this.getFormDefaults(), ...uomType };
    form.reset({
      ...uomTypeRawValue,
      id: { value: uomTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UomTypeFormDefaults {
    return {
      id: null,
    };
  }
}
