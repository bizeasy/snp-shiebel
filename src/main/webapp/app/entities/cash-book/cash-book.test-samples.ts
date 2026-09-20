import dayjs from 'dayjs/esm';

import { ICashBook, NewCashBook } from './cash-book.model';

export const sampleWithRequiredData: ICashBook = {
  id: 31372,
  transactionDate: dayjs('2023-12-14'),
  amount: 14029.85,
};

export const sampleWithPartialData: ICashBook = {
  id: 8914,
  transactionDate: dayjs('2023-12-14'),
  dueDate: dayjs('2023-12-14'),
  amount: 969.09,
  vendorCustomerName: 'formamide up',
  invoiceNo: 'astride',
};

export const sampleWithFullData: ICashBook = {
  id: 23750,
  transactionDate: dayjs('2023-12-14'),
  dueDate: dayjs('2023-12-14'),
  amount: 32316.12,
  description: 'versus helpfully monstrous',
  vendorCustomerName: 'gerbil',
  invoiceNo: 'provided lest nearly',
  invoiceDate: dayjs('2023-12-14'),
  purpose: 'eek sun',
  remarks: 'yuck in',
  project: 'gray faithfully',
  usageUnits: 'although',
  recurringTransaction: false,
  frequency: 'MONTHLY',
};

export const sampleWithNewData: NewCashBook = {
  transactionDate: dayjs('2023-12-14'),
  amount: 13841.46,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
