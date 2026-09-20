import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStatusCategory, NewStatusCategory } from '../status-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatusCategory for edit and NewStatusCategoryFormGroupInput for create.
 */
type StatusCategoryFormGroupInput = IStatusCategory | PartialWithRequiredKeyOf<NewStatusCategory>;

type StatusCategoryFormDefaults = Pick<NewStatusCategory, 'id'>;

type StatusCategoryFormGroupContent = {
  id: FormControl<IStatusCategory['id'] | NewStatusCategory['id']>;
  name: FormControl<IStatusCategory['name']>;
  description: FormControl<IStatusCategory['description']>;
};

export type StatusCategoryFormGroup = FormGroup<StatusCategoryFormGroupContent>;

@Service()
export class StatusCategoryFormService {
  createStatusCategoryFormGroup(statusCategory?: StatusCategoryFormGroupInput): StatusCategoryFormGroup {
    const statusCategoryRawValue = {
      ...this.getFormDefaults(),
      ...(statusCategory ?? { id: null }),
    };

    return new FormGroup<StatusCategoryFormGroupContent>({
      id: new FormControl(
        { value: statusCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(statusCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(statusCategoryRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getStatusCategory(form: StatusCategoryFormGroup): IStatusCategory | NewStatusCategory {
    return form.getRawValue();
  }

  resetForm(form: StatusCategoryFormGroup, statusCategory: StatusCategoryFormGroupInput): void {
    const statusCategoryRawValue = { ...this.getFormDefaults(), ...statusCategory };
    form.reset({
      ...statusCategoryRawValue,
      id: { value: statusCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StatusCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
