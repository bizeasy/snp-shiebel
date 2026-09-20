import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStatus, NewStatus } from '../status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatus for edit and NewStatusFormGroupInput for create.
 */
type StatusFormGroupInput = IStatus | PartialWithRequiredKeyOf<NewStatus>;

type StatusFormDefaults = Pick<NewStatus, 'id'>;

type StatusFormGroupContent = {
  id: FormControl<IStatus['id'] | NewStatus['id']>;
  name: FormControl<IStatus['name']>;
  sequenceNo: FormControl<IStatus['sequenceNo']>;
  description: FormControl<IStatus['description']>;
  category: FormControl<IStatus['category']>;
};

export type StatusFormGroup = FormGroup<StatusFormGroupContent>;

@Service()
export class StatusFormService {
  createStatusFormGroup(status?: StatusFormGroupInput): StatusFormGroup {
    const statusRawValue = {
      ...this.getFormDefaults(),
      ...(status ?? { id: null }),
    };

    return new FormGroup<StatusFormGroupContent>({
      id: new FormControl(
        { value: statusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(statusRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      sequenceNo: new FormControl(statusRawValue.sequenceNo),
      description: new FormControl(statusRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      category: new FormControl(statusRawValue.category),
    });
  }

  getStatus(form: StatusFormGroup): IStatus | NewStatus {
    return form.getRawValue();
  }

  resetForm(form: StatusFormGroup, status: StatusFormGroupInput): void {
    const statusRawValue = { ...this.getFormDefaults(), ...status };
    form.reset({
      ...statusRawValue,
      id: { value: statusRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StatusFormDefaults {
    return {
      id: null,
    };
  }
}
