import dayjs from 'dayjs/esm';

import { ISupplierProduct, NewSupplierProduct } from './supplier-product.model';

export const sampleWithRequiredData: ISupplierProduct = {
  id: 12350,
  fromDate: dayjs('2023-12-14T08:18'),
};

export const sampleWithPartialData: ISupplierProduct = {
  id: 5548,
  fromDate: dayjs('2023-12-13T23:08'),
  lastPrice: 31312.24,
  shippingPrice: 6417.08,
  cgst: 25135.51,
  igst: 7467.11,
  totalPrice: 4336.57,
};

export const sampleWithFullData: ISupplierProduct = {
  id: 7732,
  fromDate: dayjs('2023-12-14T01:41'),
  thruDate: dayjs('2023-12-13T23:21'),
  supplierProductId: 'worth upliftingly ick',
  minOrderQty: 12226.64,
  lastPrice: 19671.44,
  shippingPrice: 12449.78,
  cgst: 32461.23,
  igst: 158.96,
  sgst: 17525.95,
  totalPrice: 10285.48,
  comments: 'dock with aha',
  leadTimeDays: 2487,
  unitsIncluded: 30284.66,
  canDropShip: false,
};

export const sampleWithNewData: NewSupplierProduct = {
  fromDate: dayjs('2023-12-14T13:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
