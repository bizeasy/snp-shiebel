import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInvoiceRole, NewInvoiceRole } from '../invoice-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceRole for edit and NewInvoiceRoleFormGroupInput for create.
 */
type InvoiceRoleFormGroupInput = IInvoiceRole | PartialWithRequiredKeyOf<NewInvoiceRole>;

type InvoiceRoleFormDefaults = Pick<NewInvoiceRole, 'id'>;

type InvoiceRoleFormGroupContent = {
  id: FormControl<IInvoiceRole['id'] | NewInvoiceRole['id']>;
  invoice: FormControl<IInvoiceRole['invoice']>;
  invoiceItem: FormControl<IInvoiceRole['invoiceItem']>;
  party: FormControl<IInvoiceRole['party']>;
  roleType: FormControl<IInvoiceRole['roleType']>;
};

export type InvoiceRoleFormGroup = FormGroup<InvoiceRoleFormGroupContent>;

@Service()
export class InvoiceRoleFormService {
  createInvoiceRoleFormGroup(invoiceRole?: InvoiceRoleFormGroupInput): InvoiceRoleFormGroup {
    const invoiceRoleRawValue = {
      ...this.getFormDefaults(),
      ...(invoiceRole ?? { id: null }),
    };

    return new FormGroup<InvoiceRoleFormGroupContent>({
      id: new FormControl(
        { value: invoiceRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoice: new FormControl(invoiceRoleRawValue.invoice),
      invoiceItem: new FormControl(invoiceRoleRawValue.invoiceItem),
      party: new FormControl(invoiceRoleRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(invoiceRoleRawValue.roleType),
    });
  }

  getInvoiceRole(form: InvoiceRoleFormGroup): IInvoiceRole | NewInvoiceRole {
    return form.getRawValue();
  }

  resetForm(form: InvoiceRoleFormGroup, invoiceRole: InvoiceRoleFormGroupInput): void {
    const invoiceRoleRawValue = { ...this.getFormDefaults(), ...invoiceRole };
    form.reset({
      ...invoiceRoleRawValue,
      id: { value: invoiceRoleRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InvoiceRoleFormDefaults {
    return {
      id: null,
    };
  }
}
