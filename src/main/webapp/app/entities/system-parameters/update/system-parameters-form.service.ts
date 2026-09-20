import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISystemParameters, NewSystemParameters } from '../system-parameters.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISystemParameters for edit and NewSystemParametersFormGroupInput for create.
 */
type SystemParametersFormGroupInput = ISystemParameters | PartialWithRequiredKeyOf<NewSystemParameters>;

type SystemParametersFormDefaults = Pick<NewSystemParameters, 'id'>;

type SystemParametersFormGroupContent = {
  id: FormControl<ISystemParameters['id'] | NewSystemParameters['id']>;
  key: FormControl<ISystemParameters['key']>;
  value: FormControl<ISystemParameters['value']>;
  description: FormControl<ISystemParameters['description']>;
  environment: FormControl<ISystemParameters['environment']>;
};

export type SystemParametersFormGroup = FormGroup<SystemParametersFormGroupContent>;

@Service()
export class SystemParametersFormService {
  createSystemParametersFormGroup(systemParameters?: SystemParametersFormGroupInput): SystemParametersFormGroup {
    const systemParametersRawValue = {
      ...this.getFormDefaults(),
      ...(systemParameters ?? { id: null }),
    };

    return new FormGroup<SystemParametersFormGroupContent>({
      id: new FormControl(
        { value: systemParametersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      key: new FormControl(systemParametersRawValue.key, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      value: new FormControl(systemParametersRawValue.value, {
        validators: [Validators.maxLength(1000)],
      }),
      description: new FormControl(systemParametersRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      environment: new FormControl(systemParametersRawValue.environment, {
        validators: [Validators.maxLength(25)],
      }),
    });
  }

  getSystemParameters(form: SystemParametersFormGroup): ISystemParameters | NewSystemParameters {
    return form.getRawValue();
  }

  resetForm(form: SystemParametersFormGroup, systemParameters: SystemParametersFormGroupInput): void {
    const systemParametersRawValue = { ...this.getFormDefaults(), ...systemParameters };
    form.reset({
      ...systemParametersRawValue,
      id: { value: systemParametersRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SystemParametersFormDefaults {
    return {
      id: null,
    };
  }
}
