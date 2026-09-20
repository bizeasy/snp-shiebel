import dayjs from 'dayjs/esm';

import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: 16549,
  quantity: 23987.13,
};

export const sampleWithPartialData: IOrderItem = {
  id: 14633,
  quantity: 22284.73,
  unitListPrice: 12651.4,
  shipBeforeDate: dayjs('2023-12-14T09:12'),
  entryDate: dayjs('2023-12-14T00:51'),
  attr1: 'towards',
  attr2: 'wring croon',
  attr3: 'moist overvalue',
};

export const sampleWithFullData: IOrderItem = {
  id: 6728,
  sequenceNo: 17449,
  quantity: 20263.67,
  cancelQuantity: 10948.34,
  selectedAmount: 406.75,
  unitPrice: 31737.96,
  unitListPrice: 26112.24,
  isModifiedPrice: false,
  estimatedShipDate: dayjs('2023-12-14T01:15'),
  estimatedDeliveryDate: dayjs('2023-12-14T17:41'),
  shipBeforeDate: dayjs('2023-12-14T22:12'),
  shipAfterDate: dayjs('2023-12-14T06:23'),
  entryDate: dayjs('2023-12-14T06:02'),
  attr1: 'disapprove boring',
  attr2: 'zebra analyse',
  attr3: 'sate hmph',
};

export const sampleWithNewData: NewOrderItem = {
  quantity: 12164.23,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
