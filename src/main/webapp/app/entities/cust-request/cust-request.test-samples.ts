import dayjs from 'dayjs/esm';

import { ICustRequest, NewCustRequest } from './cust-request.model';

export const sampleWithRequiredData: ICustRequest = {
  id: 1950,
  name: 'drat dependable',
  custRequestDate: dayjs('2023-12-14T11:27'),
};

export const sampleWithPartialData: ICustRequest = {
  id: 17898,
  name: 'strong',
  description: 'amid lecture lest',
  priority: 8,
  custRequestDate: dayjs('2023-12-14T18:34'),
  openDateTime: dayjs('2023-12-14T02:51'),
  closedDateTime: dayjs('2023-12-14T21:41'),
  internalComment: 'carboxyl beautifully',
};

export const sampleWithFullData: ICustRequest = {
  id: 26171,
  name: 'vice',
  description: 'er',
  priority: 12,
  custRequestDate: dayjs('2023-12-14T21:57'),
  responseRequiredDate: dayjs('2023-12-14T12:42'),
  openDateTime: dayjs('2023-12-14T07:30'),
  closedDateTime: dayjs('2023-12-14T19:08'),
  internalComment: 'crystallize phew',
  billed: true,
  custEstimatedDurationDays: 5533,
};

export const sampleWithNewData: NewCustRequest = {
  name: 'following wash corrupt',
  custRequestDate: dayjs('2023-12-14T05:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
