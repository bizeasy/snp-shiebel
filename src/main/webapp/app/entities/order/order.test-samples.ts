import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 26110,
  orderDate: dayjs('2023-12-14T14:16'),
};

export const sampleWithPartialData: IOrder = {
  id: 28311,
  externalId: 'pigsty ouch apud',
  orderDate: dayjs('2023-12-13T23:41'),
  entryDate: dayjs('2023-12-14T14:25'),
  priority: 10,
  isRushOrder: false,
  grandTotal: 1277.87,
  projectName: 'yuck',
  remarks: 'solemnly watery whoever',
  paymentTerms: '../fake-data/blob/hipster.txt',
  deliveryTerms: '../fake-data/blob/hipster.txt',
  info: '../fake-data/blob/hipster.txt',
  attr1: 'alert yippee',
};

export const sampleWithFullData: IOrder = {
  id: 27813,
  name: 'along against',
  orderRefNo: 'gray',
  externalId: 'if',
  orderDate: dayjs('2023-12-14T14:08'),
  entryDate: dayjs('2023-12-14T21:52'),
  deliveryDate: dayjs('2023-12-14'),
  priority: 5,
  isRushOrder: true,
  needsInventoryIssuance: true,
  remainingSubTotal: 22705.15,
  grandTotal: 12034.51,
  projectName: 'yum space',
  version: 8876,
  versionRef: 'destock monthly merge',
  description: '../fake-data/blob/hipster.txt',
  remarks: 'greatly',
  paymentTerms: '../fake-data/blob/hipster.txt',
  deliveryTerms: '../fake-data/blob/hipster.txt',
  info: '../fake-data/blob/hipster.txt',
  attr1: 'enfold yearningly',
  attr2: 'during far adjourn',
  attr3: 'yowza',
};

export const sampleWithNewData: NewOrder = {
  orderDate: dayjs('2023-12-14T07:58'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
