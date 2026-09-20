import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReturnHeaderType, NewReturnHeaderType } from '../return-header-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReturnHeaderType for edit and NewReturnHeaderTypeFormGroupInput for create.
 */
type ReturnHeaderTypeFormGroupInput = IReturnHeaderType | PartialWithRequiredKeyOf<NewReturnHeaderType>;

type ReturnHeaderTypeFormDefaults = Pick<NewReturnHeaderType, 'id'>;

type ReturnHeaderTypeFormGroupContent = {
  id: FormControl<IReturnHeaderType['id'] | NewReturnHeaderType['id']>;
  name: FormControl<IReturnHeaderType['name']>;
  description: FormControl<IReturnHeaderType['description']>;
};

export type ReturnHeaderTypeFormGroup = FormGroup<ReturnHeaderTypeFormGroupContent>;

@Service()
export class ReturnHeaderTypeFormService {
  createReturnHeaderTypeFormGroup(returnHeaderType?: ReturnHeaderTypeFormGroupInput): ReturnHeaderTypeFormGroup {
    const returnHeaderTypeRawValue = {
      ...this.getFormDefaults(),
      ...(returnHeaderType ?? { id: null }),
    };

    return new FormGroup<ReturnHeaderTypeFormGroupContent>({
      id: new FormControl(
        { value: returnHeaderTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(returnHeaderTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(returnHeaderTypeRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getReturnHeaderType(form: ReturnHeaderTypeFormGroup): IReturnHeaderType | NewReturnHeaderType {
    return form.getRawValue();
  }

  resetForm(form: ReturnHeaderTypeFormGroup, returnHeaderType: ReturnHeaderTypeFormGroupInput): void {
    const returnHeaderTypeRawValue = { ...this.getFormDefaults(), ...returnHeaderType };
    form.reset({
      ...returnHeaderTypeRawValue,
      id: { value: returnHeaderTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ReturnHeaderTypeFormDefaults {
    return {
      id: null,
    };
  }
}
