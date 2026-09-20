import { IInvoiceItemType, NewInvoiceItemType } from './invoice-item-type.model';

export const sampleWithRequiredData: IInvoiceItemType = {
  id: 22784,
  name: 'to yowza enraged',
};

export const sampleWithPartialData: IInvoiceItemType = {
  id: 30723,
  name: 'including cap',
  description: 'however only',
};

export const sampleWithFullData: IInvoiceItemType = {
  id: 22053,
  name: 'especially wriggler suddenly',
  description: 'joyously preregister miserably',
};

export const sampleWithNewData: NewInvoiceItemType = {
  name: 'galvanize apud brr',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
