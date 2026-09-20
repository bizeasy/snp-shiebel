import dayjs from 'dayjs/esm';

import { IProductPrice, NewProductPrice } from './product-price.model';

export const sampleWithRequiredData: IProductPrice = {
  id: 26442,
  fromDate: dayjs('2023-12-14T01:41'),
  price: 2319.48,
};

export const sampleWithPartialData: IProductPrice = {
  id: 20965,
  fromDate: dayjs('2023-12-14T11:59'),
  thruDate: dayjs('2023-12-14T06:07'),
  price: 27602.25,
  cgst: 29974.07,
  sgst: 17690.76,
};

export const sampleWithFullData: IProductPrice = {
  id: 26501,
  fromDate: dayjs('2023-12-13T23:50'),
  thruDate: dayjs('2023-12-14T08:15'),
  price: 29704.98,
  cgst: 8102.45,
  igst: 13738.88,
  sgst: 29763.53,
  totalPrice: 8013.34,
};

export const sampleWithNewData: NewProductPrice = {
  fromDate: dayjs('2023-12-14T11:46'),
  price: 28475.81,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
