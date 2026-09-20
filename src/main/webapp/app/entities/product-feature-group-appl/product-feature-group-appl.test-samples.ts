import dayjs from 'dayjs/esm';

import { IProductFeatureGroupAppl, NewProductFeatureGroupAppl } from './product-feature-group-appl.model';

export const sampleWithRequiredData: IProductFeatureGroupAppl = {
  id: 18242,
  fromDate: dayjs('2023-12-14T08:01'),
};

export const sampleWithPartialData: IProductFeatureGroupAppl = {
  id: 18025,
  fromDate: dayjs('2023-12-14T05:53'),
  thruDate: dayjs('2023-12-14T02:22'),
  sequenceNo: 6081,
};

export const sampleWithFullData: IProductFeatureGroupAppl = {
  id: 25456,
  fromDate: dayjs('2023-12-14T17:14'),
  thruDate: dayjs('2023-12-14T09:50'),
  sequenceNo: 17135,
};

export const sampleWithNewData: NewProductFeatureGroupAppl = {
  fromDate: dayjs('2023-12-14T00:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
