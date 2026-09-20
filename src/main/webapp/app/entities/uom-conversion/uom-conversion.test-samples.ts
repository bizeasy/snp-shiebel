import dayjs from 'dayjs/esm';

import { IUomConversion, NewUomConversion } from './uom-conversion.model';

export const sampleWithRequiredData: IUomConversion = {
  id: 21005,
  name: 'apropos yieldingly inject',
  conversionFactor: 1777.01,
};

export const sampleWithPartialData: IUomConversion = {
  id: 16047,
  name: 'norm qua',
  conversionFactor: 22335.9,
  fromDate: dayjs('2023-12-14T21:11'),
};

export const sampleWithFullData: IUomConversion = {
  id: 25253,
  name: 'asset',
  conversionFactor: 26689.98,
  fromDate: dayjs('2023-12-14T09:42'),
  toDate: dayjs('2023-12-14T04:08'),
};

export const sampleWithNewData: NewUomConversion = {
  name: 'morning',
  conversionFactor: 1943.47,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
