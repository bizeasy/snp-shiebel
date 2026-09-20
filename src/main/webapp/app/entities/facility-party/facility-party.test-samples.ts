import dayjs from 'dayjs/esm';

import { IFacilityParty, NewFacilityParty } from './facility-party.model';

export const sampleWithRequiredData: IFacilityParty = {
  id: 20998,
  fromDate: dayjs('2023-12-13T22:27'),
};

export const sampleWithPartialData: IFacilityParty = {
  id: 27941,
  fromDate: dayjs('2023-12-14T10:17'),
  thruDate: dayjs('2023-12-14T01:42'),
};

export const sampleWithFullData: IFacilityParty = {
  id: 5267,
  fromDate: dayjs('2023-12-14T05:12'),
  thruDate: dayjs('2023-12-13T23:10'),
};

export const sampleWithNewData: NewFacilityParty = {
  fromDate: dayjs('2023-12-14T18:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
