import { IInvoiceRole, NewInvoiceRole } from './invoice-role.model';

export const sampleWithRequiredData: IInvoiceRole = {
  id: 30079,
};

export const sampleWithPartialData: IInvoiceRole = {
  id: 27375,
};

export const sampleWithFullData: IInvoiceRole = {
  id: 20306,
};

export const sampleWithNewData: NewInvoiceRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
