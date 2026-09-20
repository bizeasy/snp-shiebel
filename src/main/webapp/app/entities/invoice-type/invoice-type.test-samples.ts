import { IInvoiceType, NewInvoiceType } from './invoice-type.model';

export const sampleWithRequiredData: IInvoiceType = {
  id: 23598,
  name: 'notwithstanding publication',
};

export const sampleWithPartialData: IInvoiceType = {
  id: 9896,
  name: 'lady',
  description: 'continually multicolored boo',
};

export const sampleWithFullData: IInvoiceType = {
  id: 10329,
  name: 'stealthily sunbeam than',
  description: 'although',
};

export const sampleWithNewData: NewInvoiceType = {
  name: 'that',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
