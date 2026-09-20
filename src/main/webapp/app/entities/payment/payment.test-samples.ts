import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 4942,
  amount: 18523.62,
};

export const sampleWithPartialData: IPayment = {
  id: 7255,
  effectiveDate: dayjs('2023-12-14T03:42'),
  amount: 22419.18,
  paymentStatus: 'kiddingly metal redact',
  phone: '920-434-9719',
};

export const sampleWithFullData: IPayment = {
  id: 28239,
  paymentRefNumber: 'hourly unwieldy',
  effectiveDate: dayjs('2023-12-14T01:46'),
  paymentDate: dayjs('2023-12-14T14:52'),
  amount: 24202.02,
  actualAmount: 29367.18,
  paymentStatus: 'remark yum tame',
  mihpayId: 'encouragement alligator oof',
  txnId: 'forgather now',
  email: 'Orpha45@yahoo.com',
  phone: '1-734-929-9978 x5189',
  productInfo: 'feminize hydrolyse energetically',
};

export const sampleWithNewData: NewPayment = {
  amount: 7354.94,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
