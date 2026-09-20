import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUom, NewUom } from '../uom.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUom for edit and NewUomFormGroupInput for create.
 */
type UomFormGroupInput = IUom | PartialWithRequiredKeyOf<NewUom>;

type UomFormDefaults = Pick<NewUom, 'id'>;

type UomFormGroupContent = {
  id: FormControl<IUom['id'] | NewUom['id']>;
  name: FormControl<IUom['name']>;
  description: FormControl<IUom['description']>;
  sequenceNo: FormControl<IUom['sequenceNo']>;
  abbreviation: FormControl<IUom['abbreviation']>;
  uomType: FormControl<IUom['uomType']>;
};

export type UomFormGroup = FormGroup<UomFormGroupContent>;

@Service()
export class UomFormService {
  createUomFormGroup(uom?: UomFormGroupInput): UomFormGroup {
    const uomRawValue = {
      ...this.getFormDefaults(),
      ...(uom ?? { id: null }),
    };

    return new FormGroup<UomFormGroupContent>({
      id: new FormControl(
        { value: uomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(uomRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(uomRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      sequenceNo: new FormControl(uomRawValue.sequenceNo),
      abbreviation: new FormControl(uomRawValue.abbreviation, {
        validators: [Validators.maxLength(10)],
      }),
      uomType: new FormControl(uomRawValue.uomType),
    });
  }

  getUom(form: UomFormGroup): IUom | NewUom {
    return form.getRawValue();
  }

  resetForm(form: UomFormGroup, uom: UomFormGroupInput): void {
    const uomRawValue = { ...this.getFormDefaults(), ...uom };
    form.reset({
      ...uomRawValue,
      id: { value: uomRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UomFormDefaults {
    return {
      id: null,
    };
  }
}
