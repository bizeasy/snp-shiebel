import { IInvoiceItemTypeMap, NewInvoiceItemTypeMap } from './invoice-item-type-map.model';

export const sampleWithRequiredData: IInvoiceItemTypeMap = {
  id: 21976,
  invoiceItemMapKey: 'separate eek',
};

export const sampleWithPartialData: IInvoiceItemTypeMap = {
  id: 27876,
  invoiceItemMapKey: 'fund',
};

export const sampleWithFullData: IInvoiceItemTypeMap = {
  id: 8721,
  invoiceItemMapKey: 'repurpose teammate',
};

export const sampleWithNewData: NewInvoiceItemTypeMap = {
  invoiceItemMapKey: 'covenant',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
