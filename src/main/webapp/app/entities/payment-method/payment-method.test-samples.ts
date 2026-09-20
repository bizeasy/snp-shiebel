import dayjs from 'dayjs/esm';

import { IPaymentMethod, NewPaymentMethod } from './payment-method.model';

export const sampleWithRequiredData: IPaymentMethod = {
  id: 21285,
  name: 'yum',
};

export const sampleWithPartialData: IPaymentMethod = {
  id: 27219,
  name: 'wallaby fooey',
  fromDate: dayjs('2023-12-14T05:06'),
};

export const sampleWithFullData: IPaymentMethod = {
  id: 4693,
  name: 'nor unusual now',
  description: 'frightened continually',
  fromDate: dayjs('2023-12-14T22:20'),
  thruDate: dayjs('2023-12-14T04:52'),
};

export const sampleWithNewData: NewPaymentMethod = {
  name: 'uh-huh well-made unlike',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
