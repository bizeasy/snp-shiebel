import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  invoiceDate: dayjs('2023-12-14T10:02'),
};

export const sampleWithPartialData: IInvoice = {
  id: 30948,
  invoiceDate: dayjs('2023-12-14T09:46'),
  paidDate: dayjs('2023-12-14T13:14'),
  grandTotal: 31505.64,
  projectName: 'jet',
  info: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  invoiceDate: dayjs('2023-12-14T10:49'),
  dueDate: dayjs('2023-12-14T09:32'),
  paidDate: dayjs('2023-12-14T01:54'),
  referenceNumber: 'fraternise hourly carpool',
  invoiceMessage: 'tame cork showboat',
  externalId: 'too',
  entryDate: dayjs('2023-12-14T14:34'),
  grandTotal: 28433.63,
  projectName: 'cap indeed',
  description: '../fake-data/blob/hipster.txt',
  remarks: 'depart',
  paymentTerms: '../fake-data/blob/hipster.txt',
  deliveryTerms: '../fake-data/blob/hipster.txt',
  info: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewInvoice = {
  invoiceDate: dayjs('2023-12-14T10:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
