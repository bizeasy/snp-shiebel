import dayjs from 'dayjs/esm';

import { IPartyContactMech, NewPartyContactMech } from './party-contact-mech.model';

export const sampleWithRequiredData: IPartyContactMech = {
  id: 20496,
  fromDate: dayjs('2023-12-13T23:30'),
};

export const sampleWithPartialData: IPartyContactMech = {
  id: 24418,
  fromDate: dayjs('2023-12-14T11:35'),
};

export const sampleWithFullData: IPartyContactMech = {
  id: 8214,
  fromDate: dayjs('2023-12-14T17:20'),
  thruDate: dayjs('2023-12-14T16:03'),
};

export const sampleWithNewData: NewPartyContactMech = {
  fromDate: dayjs('2023-12-14T19:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
