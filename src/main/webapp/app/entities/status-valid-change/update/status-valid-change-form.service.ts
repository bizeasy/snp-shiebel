import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStatusValidChange, NewStatusValidChange } from '../status-valid-change.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatusValidChange for edit and NewStatusValidChangeFormGroupInput for create.
 */
type StatusValidChangeFormGroupInput = IStatusValidChange | PartialWithRequiredKeyOf<NewStatusValidChange>;

type StatusValidChangeFormDefaults = Pick<NewStatusValidChange, 'id'>;

type StatusValidChangeFormGroupContent = {
  id: FormControl<IStatusValidChange['id'] | NewStatusValidChange['id']>;
  transitionName: FormControl<IStatusValidChange['transitionName']>;
  status: FormControl<IStatusValidChange['status']>;
  statusTo: FormControl<IStatusValidChange['statusTo']>;
};

export type StatusValidChangeFormGroup = FormGroup<StatusValidChangeFormGroupContent>;

@Service()
export class StatusValidChangeFormService {
  createStatusValidChangeFormGroup(statusValidChange?: StatusValidChangeFormGroupInput): StatusValidChangeFormGroup {
    const statusValidChangeRawValue = {
      ...this.getFormDefaults(),
      ...(statusValidChange ?? { id: null }),
    };

    return new FormGroup<StatusValidChangeFormGroupContent>({
      id: new FormControl(
        { value: statusValidChangeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      transitionName: new FormControl(statusValidChangeRawValue.transitionName, {
        validators: [Validators.maxLength(60)],
      }),
      status: new FormControl(statusValidChangeRawValue.status, {
        validators: [Validators.required],
      }),
      statusTo: new FormControl(statusValidChangeRawValue.statusTo, {
        validators: [Validators.required],
      }),
    });
  }

  getStatusValidChange(form: StatusValidChangeFormGroup): IStatusValidChange | NewStatusValidChange {
    return form.getRawValue();
  }

  resetForm(form: StatusValidChangeFormGroup, statusValidChange: StatusValidChangeFormGroupInput): void {
    const statusValidChangeRawValue = { ...this.getFormDefaults(), ...statusValidChange };
    form.reset({
      ...statusValidChangeRawValue,
      id: { value: statusValidChangeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StatusValidChangeFormDefaults {
    return {
      id: null,
    };
  }
}
