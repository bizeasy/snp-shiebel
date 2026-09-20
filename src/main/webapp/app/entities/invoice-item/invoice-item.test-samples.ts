import dayjs from 'dayjs/esm';

import { IInvoiceItem, NewInvoiceItem } from './invoice-item.model';

export const sampleWithRequiredData: IInvoiceItem = {
  id: 29900,
  quantity: 3310.4,
};

export const sampleWithPartialData: IInvoiceItem = {
  id: 27115,
  quantity: 21116.22,
  amount: 29495.9,
  cancelQuantity: 28411,
  selectedAmount: 14334.17,
  estimatedShipDate: dayjs('2023-12-14T15:29'),
  estimatedDeliveryDate: dayjs('2023-12-14T16:01'),
  shipBeforeDate: dayjs('2023-12-14T17:14'),
  shipAfterDate: dayjs('2023-12-14T14:02'),
  entryDate: dayjs('2023-12-14T04:06'),
  attr1: 'than object than',
  attr2: 'soon lest mixture',
  attr3: 'because though',
};

export const sampleWithFullData: IInvoiceItem = {
  id: 20248,
  sequenceNo: 27032,
  quantity: 4018.18,
  amount: 7972.04,
  cancelQuantity: 28575.8,
  selectedAmount: 7477.11,
  unitPrice: 123.1,
  unitListPrice: 26467.36,
  isModifiedPrice: true,
  estimatedShipDate: dayjs('2023-12-14T07:09'),
  estimatedDeliveryDate: dayjs('2023-12-14T02:25'),
  shipBeforeDate: dayjs('2023-12-14T03:26'),
  shipAfterDate: dayjs('2023-12-14T18:30'),
  entryDate: dayjs('2023-12-14T09:41'),
  attr1: 'at mechanic',
  attr2: 'despite innocently',
  attr3: 'excited wilt gown',
};

export const sampleWithNewData: NewInvoiceItem = {
  quantity: 31746.24,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
