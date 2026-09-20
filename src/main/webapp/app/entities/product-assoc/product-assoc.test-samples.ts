import dayjs from 'dayjs/esm';

import { IProductAssoc, NewProductAssoc } from './product-assoc.model';

export const sampleWithRequiredData: IProductAssoc = {
  id: 32693,
  fromDate: dayjs('2023-12-14T17:55'),
};

export const sampleWithPartialData: IProductAssoc = {
  id: 12441,
  fromDate: dayjs('2023-12-14T16:25'),
  thruDate: dayjs('2023-12-14T11:45'),
  sequenceNo: 2810,
  quantity: 22682.49,
};

export const sampleWithFullData: IProductAssoc = {
  id: 2876,
  fromDate: dayjs('2023-12-14T03:02'),
  thruDate: dayjs('2023-12-14T21:16'),
  sequenceNo: 16150,
  quantity: 841.17,
};

export const sampleWithNewData: NewProductAssoc = {
  fromDate: dayjs('2023-12-14T21:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
