import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IInvoiceItem, NewInvoiceItem } from '../invoice-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItem for edit and NewInvoiceItemFormGroupInput for create.
 */
type InvoiceItemFormGroupInput = IInvoiceItem | PartialWithRequiredKeyOf<NewInvoiceItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoiceItem | NewInvoiceItem> = Omit<
  T,
  'estimatedShipDate' | 'estimatedDeliveryDate' | 'shipBeforeDate' | 'shipAfterDate' | 'entryDate'
> & {
  estimatedShipDate?: string | null;
  estimatedDeliveryDate?: string | null;
  shipBeforeDate?: string | null;
  shipAfterDate?: string | null;
  entryDate?: string | null;
};

type InvoiceItemFormRawValue = FormValueOf<IInvoiceItem>;

type NewInvoiceItemFormRawValue = FormValueOf<NewInvoiceItem>;

type InvoiceItemFormDefaults = Pick<
  NewInvoiceItem,
  'id' | 'isModifiedPrice' | 'estimatedShipDate' | 'estimatedDeliveryDate' | 'shipBeforeDate' | 'shipAfterDate' | 'entryDate'
>;

type InvoiceItemFormGroupContent = {
  id: FormControl<InvoiceItemFormRawValue['id'] | NewInvoiceItem['id']>;
  sequenceNo: FormControl<InvoiceItemFormRawValue['sequenceNo']>;
  quantity: FormControl<InvoiceItemFormRawValue['quantity']>;
  amount: FormControl<InvoiceItemFormRawValue['amount']>;
  cancelQuantity: FormControl<InvoiceItemFormRawValue['cancelQuantity']>;
  selectedAmount: FormControl<InvoiceItemFormRawValue['selectedAmount']>;
  unitPrice: FormControl<InvoiceItemFormRawValue['unitPrice']>;
  unitListPrice: FormControl<InvoiceItemFormRawValue['unitListPrice']>;
  isModifiedPrice: FormControl<InvoiceItemFormRawValue['isModifiedPrice']>;
  estimatedShipDate: FormControl<InvoiceItemFormRawValue['estimatedShipDate']>;
  estimatedDeliveryDate: FormControl<InvoiceItemFormRawValue['estimatedDeliveryDate']>;
  shipBeforeDate: FormControl<InvoiceItemFormRawValue['shipBeforeDate']>;
  shipAfterDate: FormControl<InvoiceItemFormRawValue['shipAfterDate']>;
  entryDate: FormControl<InvoiceItemFormRawValue['entryDate']>;
  attr1: FormControl<InvoiceItemFormRawValue['attr1']>;
  attr2: FormControl<InvoiceItemFormRawValue['attr2']>;
  attr3: FormControl<InvoiceItemFormRawValue['attr3']>;
  invoice: FormControl<InvoiceItemFormRawValue['invoice']>;
  invoiceItemType: FormControl<InvoiceItemFormRawValue['invoiceItemType']>;
  product: FormControl<InvoiceItemFormRawValue['product']>;
  uom: FormControl<InvoiceItemFormRawValue['uom']>;
  productFeature: FormControl<InvoiceItemFormRawValue['productFeature']>;
  productFacility: FormControl<InvoiceItemFormRawValue['productFacility']>;
  fromInventoryItem: FormControl<InvoiceItemFormRawValue['fromInventoryItem']>;
  taxAuthRate: FormControl<InvoiceItemFormRawValue['taxAuthRate']>;
  parentInvoice: FormControl<InvoiceItemFormRawValue['parentInvoice']>;
  parentInvoiceItem: FormControl<InvoiceItemFormRawValue['parentInvoiceItem']>;
};

export type InvoiceItemFormGroup = FormGroup<InvoiceItemFormGroupContent>;

@Service()
export class InvoiceItemFormService {
  createInvoiceItemFormGroup(invoiceItem?: InvoiceItemFormGroupInput): InvoiceItemFormGroup {
    const invoiceItemRawValue = this.convertInvoiceItemToInvoiceItemRawValue({
      ...this.getFormDefaults(),
      ...(invoiceItem ?? { id: null }),
    });

    return new FormGroup<InvoiceItemFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sequenceNo: new FormControl(invoiceItemRawValue.sequenceNo),
      quantity: new FormControl(invoiceItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      amount: new FormControl(invoiceItemRawValue.amount),
      cancelQuantity: new FormControl(invoiceItemRawValue.cancelQuantity),
      selectedAmount: new FormControl(invoiceItemRawValue.selectedAmount),
      unitPrice: new FormControl(invoiceItemRawValue.unitPrice),
      unitListPrice: new FormControl(invoiceItemRawValue.unitListPrice),
      isModifiedPrice: new FormControl(invoiceItemRawValue.isModifiedPrice),
      estimatedShipDate: new FormControl(invoiceItemRawValue.estimatedShipDate),
      estimatedDeliveryDate: new FormControl(invoiceItemRawValue.estimatedDeliveryDate),
      shipBeforeDate: new FormControl(invoiceItemRawValue.shipBeforeDate),
      shipAfterDate: new FormControl(invoiceItemRawValue.shipAfterDate),
      entryDate: new FormControl(invoiceItemRawValue.entryDate),
      attr1: new FormControl(invoiceItemRawValue.attr1, {
        validators: [Validators.maxLength(255)],
      }),
      attr2: new FormControl(invoiceItemRawValue.attr2, {
        validators: [Validators.maxLength(255)],
      }),
      attr3: new FormControl(invoiceItemRawValue.attr3, {
        validators: [Validators.maxLength(255)],
      }),
      invoice: new FormControl(invoiceItemRawValue.invoice, {
        validators: [Validators.required],
      }),
      invoiceItemType: new FormControl(invoiceItemRawValue.invoiceItemType),
      product: new FormControl(invoiceItemRawValue.product),
      uom: new FormControl(invoiceItemRawValue.uom),
      productFeature: new FormControl(invoiceItemRawValue.productFeature),
      productFacility: new FormControl(invoiceItemRawValue.productFacility),
      fromInventoryItem: new FormControl(invoiceItemRawValue.fromInventoryItem),
      taxAuthRate: new FormControl(invoiceItemRawValue.taxAuthRate),
      parentInvoice: new FormControl(invoiceItemRawValue.parentInvoice),
      parentInvoiceItem: new FormControl(invoiceItemRawValue.parentInvoiceItem),
    });
  }

  getInvoiceItem(form: InvoiceItemFormGroup): IInvoiceItem | NewInvoiceItem {
    return this.convertInvoiceItemRawValueToInvoiceItem(form.getRawValue());
  }

  resetForm(form: InvoiceItemFormGroup, invoiceItem: InvoiceItemFormGroupInput): void {
    const invoiceItemRawValue = this.convertInvoiceItemToInvoiceItemRawValue({ ...this.getFormDefaults(), ...invoiceItem });
    form.reset({
      ...invoiceItemRawValue,
      id: { value: invoiceItemRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InvoiceItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isModifiedPrice: false,
      estimatedShipDate: currentTime,
      estimatedDeliveryDate: currentTime,
      shipBeforeDate: currentTime,
      shipAfterDate: currentTime,
      entryDate: currentTime,
    };
  }

  private convertInvoiceItemRawValueToInvoiceItem(
    rawInvoiceItem: InvoiceItemFormRawValue | NewInvoiceItemFormRawValue,
  ): IInvoiceItem | NewInvoiceItem {
    return {
      ...rawInvoiceItem,
      estimatedShipDate: dayjs(rawInvoiceItem.estimatedShipDate, DATE_TIME_FORMAT),
      estimatedDeliveryDate: dayjs(rawInvoiceItem.estimatedDeliveryDate, DATE_TIME_FORMAT),
      shipBeforeDate: dayjs(rawInvoiceItem.shipBeforeDate, DATE_TIME_FORMAT),
      shipAfterDate: dayjs(rawInvoiceItem.shipAfterDate, DATE_TIME_FORMAT),
      entryDate: dayjs(rawInvoiceItem.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceItemToInvoiceItemRawValue(
    invoiceItem: IInvoiceItem | (Partial<NewInvoiceItem> & InvoiceItemFormDefaults),
  ): InvoiceItemFormRawValue | PartialWithRequiredKeyOf<NewInvoiceItemFormRawValue> {
    return {
      ...invoiceItem,
      estimatedShipDate: invoiceItem.estimatedShipDate ? invoiceItem.estimatedShipDate.format(DATE_TIME_FORMAT) : undefined,
      estimatedDeliveryDate: invoiceItem.estimatedDeliveryDate ? invoiceItem.estimatedDeliveryDate.format(DATE_TIME_FORMAT) : undefined,
      shipBeforeDate: invoiceItem.shipBeforeDate ? invoiceItem.shipBeforeDate.format(DATE_TIME_FORMAT) : undefined,
      shipAfterDate: invoiceItem.shipAfterDate ? invoiceItem.shipAfterDate.format(DATE_TIME_FORMAT) : undefined,
      entryDate: invoiceItem.entryDate ? invoiceItem.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
