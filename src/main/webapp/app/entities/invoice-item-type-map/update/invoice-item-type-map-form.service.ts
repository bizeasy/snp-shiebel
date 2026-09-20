import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInvoiceItemTypeMap, NewInvoiceItemTypeMap } from '../invoice-item-type-map.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItemTypeMap for edit and NewInvoiceItemTypeMapFormGroupInput for create.
 */
type InvoiceItemTypeMapFormGroupInput = IInvoiceItemTypeMap | PartialWithRequiredKeyOf<NewInvoiceItemTypeMap>;

type InvoiceItemTypeMapFormDefaults = Pick<NewInvoiceItemTypeMap, 'id'>;

type InvoiceItemTypeMapFormGroupContent = {
  id: FormControl<IInvoiceItemTypeMap['id'] | NewInvoiceItemTypeMap['id']>;
  invoiceItemMapKey: FormControl<IInvoiceItemTypeMap['invoiceItemMapKey']>;
  invoiceType: FormControl<IInvoiceItemTypeMap['invoiceType']>;
  invoiceItemType: FormControl<IInvoiceItemTypeMap['invoiceItemType']>;
};

export type InvoiceItemTypeMapFormGroup = FormGroup<InvoiceItemTypeMapFormGroupContent>;

@Service()
export class InvoiceItemTypeMapFormService {
  createInvoiceItemTypeMapFormGroup(invoiceItemTypeMap?: InvoiceItemTypeMapFormGroupInput): InvoiceItemTypeMapFormGroup {
    const invoiceItemTypeMapRawValue = {
      ...this.getFormDefaults(),
      ...(invoiceItemTypeMap ?? { id: null }),
    };

    return new FormGroup<InvoiceItemTypeMapFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemTypeMapRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceItemMapKey: new FormControl(invoiceItemTypeMapRawValue.invoiceItemMapKey, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      invoiceType: new FormControl(invoiceItemTypeMapRawValue.invoiceType, {
        validators: [Validators.required],
      }),
      invoiceItemType: new FormControl(invoiceItemTypeMapRawValue.invoiceItemType, {
        validators: [Validators.required],
      }),
    });
  }

  getInvoiceItemTypeMap(form: InvoiceItemTypeMapFormGroup): IInvoiceItemTypeMap | NewInvoiceItemTypeMap {
    return form.getRawValue();
  }

  resetForm(form: InvoiceItemTypeMapFormGroup, invoiceItemTypeMap: InvoiceItemTypeMapFormGroupInput): void {
    const invoiceItemTypeMapRawValue = { ...this.getFormDefaults(), ...invoiceItemTypeMap };
    form.reset({
      ...invoiceItemTypeMapRawValue,
      id: { value: invoiceItemTypeMapRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InvoiceItemTypeMapFormDefaults {
    return {
      id: null,
    };
  }
}
