import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITransactionCategory, NewTransactionCategory } from '../transaction-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionCategory for edit and NewTransactionCategoryFormGroupInput for create.
 */
type TransactionCategoryFormGroupInput = ITransactionCategory | PartialWithRequiredKeyOf<NewTransactionCategory>;

type TransactionCategoryFormDefaults = Pick<NewTransactionCategory, 'id'>;

type TransactionCategoryFormGroupContent = {
  id: FormControl<ITransactionCategory['id'] | NewTransactionCategory['id']>;
  name: FormControl<ITransactionCategory['name']>;
  description: FormControl<ITransactionCategory['description']>;
};

export type TransactionCategoryFormGroup = FormGroup<TransactionCategoryFormGroupContent>;

@Service()
export class TransactionCategoryFormService {
  createTransactionCategoryFormGroup(transactionCategory?: TransactionCategoryFormGroupInput): TransactionCategoryFormGroup {
    const transactionCategoryRawValue = {
      ...this.getFormDefaults(),
      ...(transactionCategory ?? { id: null }),
    };

    return new FormGroup<TransactionCategoryFormGroupContent>({
      id: new FormControl(
        { value: transactionCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(transactionCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(transactionCategoryRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
    });
  }

  getTransactionCategory(form: TransactionCategoryFormGroup): ITransactionCategory | NewTransactionCategory {
    return form.getRawValue();
  }

  resetForm(form: TransactionCategoryFormGroup, transactionCategory: TransactionCategoryFormGroupInput): void {
    const transactionCategoryRawValue = { ...this.getFormDefaults(), ...transactionCategory };
    form.reset({
      ...transactionCategoryRawValue,
      id: { value: transactionCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TransactionCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
