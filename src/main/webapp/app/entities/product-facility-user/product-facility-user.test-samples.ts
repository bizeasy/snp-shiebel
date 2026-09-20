import dayjs from 'dayjs/esm';

import { IProductFacilityUser, NewProductFacilityUser } from './product-facility-user.model';

export const sampleWithRequiredData: IProductFacilityUser = {
  id: 14701,
  fromDate: dayjs('2023-12-14T15:26'),
};

export const sampleWithPartialData: IProductFacilityUser = {
  id: 29363,
  fromDate: dayjs('2023-12-14T03:31'),
  thruDate: dayjs('2023-12-14T12:36'),
};

export const sampleWithFullData: IProductFacilityUser = {
  id: 11259,
  fromDate: dayjs('2023-12-14T07:27'),
  thruDate: dayjs('2023-12-14T13:25'),
  costPerDay: 30198.02,
  totalCost: 28510.91,
  discount: 11072.39,
};

export const sampleWithNewData: NewProductFacilityUser = {
  fromDate: dayjs('2023-12-14T18:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
