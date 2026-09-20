import dayjs from 'dayjs/esm';

import { ICustRequestItem, NewCustRequestItem } from './cust-request-item.model';

export const sampleWithRequiredData: ICustRequestItem = {
  id: 21423,
  quantity: 1216.63,
};

export const sampleWithPartialData: ICustRequestItem = {
  id: 389,
  sequenceNo: 9538,
  priority: 13,
  requiredByDate: dayjs('2023-12-14T08:38'),
  quantity: 1390.13,
  story: 'rewarding ornery',
};

export const sampleWithFullData: ICustRequestItem = {
  id: 32387,
  sequenceNo: 5133,
  priority: 15,
  requiredByDate: dayjs('2023-12-14T07:10'),
  quantity: 7426.64,
  selectedAmount: 22373.59,
  maxAmount: 14666.12,
  description: 'huzzah considering furthermore',
  story: 'upright',
};

export const sampleWithNewData: NewCustRequestItem = {
  quantity: 11196.66,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
