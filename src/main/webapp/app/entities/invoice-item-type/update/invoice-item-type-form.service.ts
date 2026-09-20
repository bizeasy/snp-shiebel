import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInvoiceItemType, NewInvoiceItemType } from '../invoice-item-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItemType for edit and NewInvoiceItemTypeFormGroupInput for create.
 */
type InvoiceItemTypeFormGroupInput = IInvoiceItemType | PartialWithRequiredKeyOf<NewInvoiceItemType>;

type InvoiceItemTypeFormDefaults = Pick<NewInvoiceItemType, 'id'>;

type InvoiceItemTypeFormGroupContent = {
  id: FormControl<IInvoiceItemType['id'] | NewInvoiceItemType['id']>;
  name: FormControl<IInvoiceItemType['name']>;
  description: FormControl<IInvoiceItemType['description']>;
};

export type InvoiceItemTypeFormGroup = FormGroup<InvoiceItemTypeFormGroupContent>;

@Service()
export class InvoiceItemTypeFormService {
  createInvoiceItemTypeFormGroup(invoiceItemType?: InvoiceItemTypeFormGroupInput): InvoiceItemTypeFormGroup {
    const invoiceItemTypeRawValue = {
      ...this.getFormDefaults(),
      ...(invoiceItemType ?? { id: null }),
    };

    return new FormGroup<InvoiceItemTypeFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(invoiceItemTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(invoiceItemTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getInvoiceItemType(form: InvoiceItemTypeFormGroup): IInvoiceItemType | NewInvoiceItemType {
    return form.getRawValue();
  }

  resetForm(form: InvoiceItemTypeFormGroup, invoiceItemType: InvoiceItemTypeFormGroupInput): void {
    const invoiceItemTypeRawValue = { ...this.getFormDefaults(), ...invoiceItemType };
    form.reset({
      ...invoiceItemTypeRawValue,
      id: { value: invoiceItemTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InvoiceItemTypeFormDefaults {
    return {
      id: null,
    };
  }
}
