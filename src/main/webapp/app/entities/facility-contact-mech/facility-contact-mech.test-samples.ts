import dayjs from 'dayjs/esm';

import { IFacilityContactMech, NewFacilityContactMech } from './facility-contact-mech.model';

export const sampleWithRequiredData: IFacilityContactMech = {
  id: 25122,
  fromDate: dayjs('2023-12-14T14:21'),
};

export const sampleWithPartialData: IFacilityContactMech = {
  id: 12690,
  fromDate: dayjs('2023-12-14T13:47'),
  thruDate: dayjs('2023-12-14T21:58'),
};

export const sampleWithFullData: IFacilityContactMech = {
  id: 19173,
  fromDate: dayjs('2023-12-14T19:23'),
  thruDate: dayjs('2023-12-14T03:12'),
};

export const sampleWithNewData: NewFacilityContactMech = {
  fromDate: dayjs('2023-12-14T14:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
