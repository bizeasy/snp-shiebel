import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInvoiceType, NewInvoiceType } from '../invoice-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceType for edit and NewInvoiceTypeFormGroupInput for create.
 */
type InvoiceTypeFormGroupInput = IInvoiceType | PartialWithRequiredKeyOf<NewInvoiceType>;

type InvoiceTypeFormDefaults = Pick<NewInvoiceType, 'id'>;

type InvoiceTypeFormGroupContent = {
  id: FormControl<IInvoiceType['id'] | NewInvoiceType['id']>;
  name: FormControl<IInvoiceType['name']>;
  description: FormControl<IInvoiceType['description']>;
};

export type InvoiceTypeFormGroup = FormGroup<InvoiceTypeFormGroupContent>;

@Service()
export class InvoiceTypeFormService {
  createInvoiceTypeFormGroup(invoiceType?: InvoiceTypeFormGroupInput): InvoiceTypeFormGroup {
    const invoiceTypeRawValue = {
      ...this.getFormDefaults(),
      ...(invoiceType ?? { id: null }),
    };

    return new FormGroup<InvoiceTypeFormGroupContent>({
      id: new FormControl(
        { value: invoiceTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(invoiceTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(invoiceTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getInvoiceType(form: InvoiceTypeFormGroup): IInvoiceType | NewInvoiceType {
    return form.getRawValue();
  }

  resetForm(form: InvoiceTypeFormGroup, invoiceType: InvoiceTypeFormGroupInput): void {
    const invoiceTypeRawValue = { ...this.getFormDefaults(), ...invoiceType };
    form.reset({
      ...invoiceTypeRawValue,
      id: { value: invoiceTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InvoiceTypeFormDefaults {
    return {
      id: null,
    };
  }
}
