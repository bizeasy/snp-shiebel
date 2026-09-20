import dayjs from 'dayjs/esm';

import { IProductFeatureCategoryAppl, NewProductFeatureCategoryAppl } from './product-feature-category-appl.model';

export const sampleWithRequiredData: IProductFeatureCategoryAppl = {
  id: 13281,
  fromDate: dayjs('2023-12-14T01:09'),
};

export const sampleWithPartialData: IProductFeatureCategoryAppl = {
  id: 29076,
  fromDate: dayjs('2023-12-14T17:20'),
  thruDate: dayjs('2023-12-14T06:38'),
};

export const sampleWithFullData: IProductFeatureCategoryAppl = {
  id: 31684,
  fromDate: dayjs('2023-12-14T07:49'),
  thruDate: dayjs('2023-12-14T11:27'),
};

export const sampleWithNewData: NewProductFeatureCategoryAppl = {
  fromDate: dayjs('2023-12-14T00:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
