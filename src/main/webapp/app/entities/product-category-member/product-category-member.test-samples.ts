import dayjs from 'dayjs/esm';

import { IProductCategoryMember, NewProductCategoryMember } from './product-category-member.model';

export const sampleWithRequiredData: IProductCategoryMember = {
  id: 31330,
  fromDate: dayjs('2023-12-14T11:48'),
};

export const sampleWithPartialData: IProductCategoryMember = {
  id: 19853,
  fromDate: dayjs('2023-12-13T23:55'),
  thruDate: dayjs('2023-12-14T00:16'),
};

export const sampleWithFullData: IProductCategoryMember = {
  id: 3694,
  fromDate: dayjs('2023-12-13T23:09'),
  thruDate: dayjs('2023-12-14T20:38'),
  sequenceNo: 10673,
};

export const sampleWithNewData: NewProductCategoryMember = {
  fromDate: dayjs('2023-12-14T21:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
