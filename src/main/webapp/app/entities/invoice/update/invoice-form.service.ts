import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'invoiceDate' | 'dueDate' | 'paidDate' | 'entryDate'> & {
  invoiceDate?: string | null;
  dueDate?: string | null;
  paidDate?: string | null;
  entryDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'invoiceDate' | 'dueDate' | 'paidDate' | 'entryDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  invoiceDate: FormControl<InvoiceFormRawValue['invoiceDate']>;
  dueDate: FormControl<InvoiceFormRawValue['dueDate']>;
  paidDate: FormControl<InvoiceFormRawValue['paidDate']>;
  referenceNumber: FormControl<InvoiceFormRawValue['referenceNumber']>;
  invoiceMessage: FormControl<InvoiceFormRawValue['invoiceMessage']>;
  externalId: FormControl<InvoiceFormRawValue['externalId']>;
  entryDate: FormControl<InvoiceFormRawValue['entryDate']>;
  grandTotal: FormControl<InvoiceFormRawValue['grandTotal']>;
  projectName: FormControl<InvoiceFormRawValue['projectName']>;
  description: FormControl<InvoiceFormRawValue['description']>;
  remarks: FormControl<InvoiceFormRawValue['remarks']>;
  paymentTerms: FormControl<InvoiceFormRawValue['paymentTerms']>;
  deliveryTerms: FormControl<InvoiceFormRawValue['deliveryTerms']>;
  info: FormControl<InvoiceFormRawValue['info']>;
  invoiceType: FormControl<InvoiceFormRawValue['invoiceType']>;
  order: FormControl<InvoiceFormRawValue['order']>;
  fromParty: FormControl<InvoiceFormRawValue['fromParty']>;
  toParty: FormControl<InvoiceFormRawValue['toParty']>;
  roleType: FormControl<InvoiceFormRawValue['roleType']>;
  status: FormControl<InvoiceFormRawValue['status']>;
  contactMech: FormControl<InvoiceFormRawValue['contactMech']>;
  currencyUom: FormControl<InvoiceFormRawValue['currencyUom']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Service()
export class InvoiceFormService {
  createInvoiceFormGroup(invoice?: InvoiceFormGroupInput): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...(invoice ?? { id: null }),
    });

    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceDate: new FormControl(invoiceRawValue.invoiceDate, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(invoiceRawValue.dueDate),
      paidDate: new FormControl(invoiceRawValue.paidDate),
      referenceNumber: new FormControl(invoiceRawValue.referenceNumber, {
        validators: [Validators.maxLength(60)],
      }),
      invoiceMessage: new FormControl(invoiceRawValue.invoiceMessage, {
        validators: [Validators.maxLength(255)],
      }),
      externalId: new FormControl(invoiceRawValue.externalId, {
        validators: [Validators.maxLength(25)],
      }),
      entryDate: new FormControl(invoiceRawValue.entryDate),
      grandTotal: new FormControl(invoiceRawValue.grandTotal),
      projectName: new FormControl(invoiceRawValue.projectName, {
        validators: [Validators.maxLength(255)],
      }),
      description: new FormControl(invoiceRawValue.description),
      remarks: new FormControl(invoiceRawValue.remarks, {
        validators: [Validators.maxLength(2000)],
      }),
      paymentTerms: new FormControl(invoiceRawValue.paymentTerms),
      deliveryTerms: new FormControl(invoiceRawValue.deliveryTerms),
      info: new FormControl(invoiceRawValue.info),
      invoiceType: new FormControl(invoiceRawValue.invoiceType, {
        validators: [Validators.required],
      }),
      order: new FormControl(invoiceRawValue.order),
      fromParty: new FormControl(invoiceRawValue.fromParty, {
        validators: [Validators.required],
      }),
      toParty: new FormControl(invoiceRawValue.toParty, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(invoiceRawValue.roleType),
      status: new FormControl(invoiceRawValue.status),
      contactMech: new FormControl(invoiceRawValue.contactMech),
      currencyUom: new FormControl(invoiceRawValue.currencyUom),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue());
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset({
      ...invoiceRawValue,
      id: { value: invoiceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      invoiceDate: currentTime,
      dueDate: currentTime,
      paidDate: currentTime,
      entryDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      invoiceDate: dayjs(rawInvoice.invoiceDate, DATE_TIME_FORMAT),
      dueDate: dayjs(rawInvoice.dueDate, DATE_TIME_FORMAT),
      paidDate: dayjs(rawInvoice.paidDate, DATE_TIME_FORMAT),
      entryDate: dayjs(rawInvoice.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      invoiceDate: invoice.invoiceDate ? invoice.invoiceDate.format(DATE_TIME_FORMAT) : undefined,
      dueDate: invoice.dueDate ? invoice.dueDate.format(DATE_TIME_FORMAT) : undefined,
      paidDate: invoice.paidDate ? invoice.paidDate.format(DATE_TIME_FORMAT) : undefined,
      entryDate: invoice.entryDate ? invoice.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
