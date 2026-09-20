import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICashBook, NewCashBook } from '../cash-book.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICashBook for edit and NewCashBookFormGroupInput for create.
 */
type CashBookFormGroupInput = ICashBook | PartialWithRequiredKeyOf<NewCashBook>;

type CashBookFormDefaults = Pick<NewCashBook, 'id' | 'recurringTransaction'>;

type CashBookFormGroupContent = {
  id: FormControl<ICashBook['id'] | NewCashBook['id']>;
  transactionDate: FormControl<ICashBook['transactionDate']>;
  dueDate: FormControl<ICashBook['dueDate']>;
  amount: FormControl<ICashBook['amount']>;
  description: FormControl<ICashBook['description']>;
  vendorCustomerName: FormControl<ICashBook['vendorCustomerName']>;
  invoiceNo: FormControl<ICashBook['invoiceNo']>;
  invoiceDate: FormControl<ICashBook['invoiceDate']>;
  purpose: FormControl<ICashBook['purpose']>;
  remarks: FormControl<ICashBook['remarks']>;
  project: FormControl<ICashBook['project']>;
  usageUnits: FormControl<ICashBook['usageUnits']>;
  recurringTransaction: FormControl<ICashBook['recurringTransaction']>;
  frequency: FormControl<ICashBook['frequency']>;
  transactionType: FormControl<ICashBook['transactionType']>;
  transactionCategory: FormControl<ICashBook['transactionCategory']>;
  paymentMethodType: FormControl<ICashBook['paymentMethodType']>;
  department: FormControl<ICashBook['department']>;
  currencyUom: FormControl<ICashBook['currencyUom']>;
  status: FormControl<ICashBook['status']>;
  order: FormControl<ICashBook['order']>;
  invoice: FormControl<ICashBook['invoice']>;
  fromParty: FormControl<ICashBook['fromParty']>;
  toParty: FormControl<ICashBook['toParty']>;
};

export type CashBookFormGroup = FormGroup<CashBookFormGroupContent>;

@Service()
export class CashBookFormService {
  createCashBookFormGroup(cashBook?: CashBookFormGroupInput): CashBookFormGroup {
    const cashBookRawValue = {
      ...this.getFormDefaults(),
      ...(cashBook ?? { id: null }),
    };

    return new FormGroup<CashBookFormGroupContent>({
      id: new FormControl(
        { value: cashBookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      transactionDate: new FormControl(cashBookRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(cashBookRawValue.dueDate),
      amount: new FormControl(cashBookRawValue.amount, {
        validators: [Validators.required],
      }),
      description: new FormControl(cashBookRawValue.description, {
        validators: [Validators.maxLength(2000)],
      }),
      vendorCustomerName: new FormControl(cashBookRawValue.vendorCustomerName, {
        validators: [Validators.maxLength(200)],
      }),
      invoiceNo: new FormControl(cashBookRawValue.invoiceNo, {
        validators: [Validators.maxLength(60)],
      }),
      invoiceDate: new FormControl(cashBookRawValue.invoiceDate),
      purpose: new FormControl(cashBookRawValue.purpose, {
        validators: [Validators.maxLength(255)],
      }),
      remarks: new FormControl(cashBookRawValue.remarks, {
        validators: [Validators.maxLength(2000)],
      }),
      project: new FormControl(cashBookRawValue.project, {
        validators: [Validators.maxLength(255)],
      }),
      usageUnits: new FormControl(cashBookRawValue.usageUnits, {
        validators: [Validators.maxLength(60)],
      }),
      recurringTransaction: new FormControl(cashBookRawValue.recurringTransaction),
      frequency: new FormControl(cashBookRawValue.frequency),
      transactionType: new FormControl(cashBookRawValue.transactionType, {
        validators: [Validators.required],
      }),
      transactionCategory: new FormControl(cashBookRawValue.transactionCategory),
      paymentMethodType: new FormControl(cashBookRawValue.paymentMethodType),
      department: new FormControl(cashBookRawValue.department),
      currencyUom: new FormControl(cashBookRawValue.currencyUom),
      status: new FormControl(cashBookRawValue.status),
      order: new FormControl(cashBookRawValue.order),
      invoice: new FormControl(cashBookRawValue.invoice),
      fromParty: new FormControl(cashBookRawValue.fromParty),
      toParty: new FormControl(cashBookRawValue.toParty),
    });
  }

  getCashBook(form: CashBookFormGroup): ICashBook | NewCashBook {
    return form.getRawValue();
  }

  resetForm(form: CashBookFormGroup, cashBook: CashBookFormGroupInput): void {
    const cashBookRawValue = { ...this.getFormDefaults(), ...cashBook };
    form.reset({
      ...cashBookRawValue,
      id: { value: cashBookRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CashBookFormDefaults {
    return {
      id: null,
      recurringTransaction: false,
    };
  }
}
