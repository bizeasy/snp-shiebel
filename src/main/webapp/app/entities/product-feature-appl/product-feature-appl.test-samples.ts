import dayjs from 'dayjs/esm';

import { IProductFeatureAppl, NewProductFeatureAppl } from './product-feature-appl.model';

export const sampleWithRequiredData: IProductFeatureAppl = {
  id: 16451,
  fromDate: dayjs('2023-12-14T01:29'),
};

export const sampleWithPartialData: IProductFeatureAppl = {
  id: 31962,
  fromDate: dayjs('2023-12-14T14:39'),
  thruDate: dayjs('2023-12-14T07:11'),
  amount: 4252.41,
};

export const sampleWithFullData: IProductFeatureAppl = {
  id: 20140,
  fromDate: dayjs('2023-12-14T01:04'),
  thruDate: dayjs('2023-12-14T13:09'),
  sequenceNo: 2072,
  amount: 2215.55,
};

export const sampleWithNewData: NewProductFeatureAppl = {
  fromDate: dayjs('2023-12-14T11:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
