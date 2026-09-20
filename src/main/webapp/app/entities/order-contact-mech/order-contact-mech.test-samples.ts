import dayjs from 'dayjs/esm';

import { IOrderContactMech, NewOrderContactMech } from './order-contact-mech.model';

export const sampleWithRequiredData: IOrderContactMech = {
  id: 25676,
  fromDate: dayjs('2023-12-14T05:00'),
};

export const sampleWithPartialData: IOrderContactMech = {
  id: 24252,
  fromDate: dayjs('2023-12-14T11:41'),
  thruDate: dayjs('2023-12-14T00:06'),
};

export const sampleWithFullData: IOrderContactMech = {
  id: 16138,
  fromDate: dayjs('2023-12-14T11:50'),
  thruDate: dayjs('2023-12-14T01:17'),
};

export const sampleWithNewData: NewOrderContactMech = {
  fromDate: dayjs('2023-12-14T15:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
