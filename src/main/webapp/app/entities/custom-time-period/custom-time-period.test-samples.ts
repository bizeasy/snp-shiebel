import dayjs from 'dayjs/esm';

import { ICustomTimePeriod, NewCustomTimePeriod } from './custom-time-period.model';

export const sampleWithRequiredData: ICustomTimePeriod = {
  id: 8967,
};

export const sampleWithPartialData: ICustomTimePeriod = {
  id: 9722,
  name: 'probable sand whoa',
  thruDate: dayjs('2023-12-14'),
};

export const sampleWithFullData: ICustomTimePeriod = {
  id: 14118,
  name: 'treasure',
  fromDate: dayjs('2023-12-14'),
  thruDate: dayjs('2023-12-14'),
};

export const sampleWithNewData: NewCustomTimePeriod = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
