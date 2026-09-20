import dayjs from 'dayjs/esm';

import { IPartyClassification, NewPartyClassification } from './party-classification.model';

export const sampleWithRequiredData: IPartyClassification = {
  id: 5666,
  fromDate: dayjs('2023-12-14T00:42'),
};

export const sampleWithPartialData: IPartyClassification = {
  id: 3369,
  fromDate: dayjs('2023-12-14T11:02'),
  thruDate: dayjs('2023-12-14T07:42'),
};

export const sampleWithFullData: IPartyClassification = {
  id: 121,
  fromDate: dayjs('2023-12-14T02:06'),
  thruDate: dayjs('2023-12-13T21:58'),
};

export const sampleWithNewData: NewPartyClassification = {
  fromDate: dayjs('2023-12-14T16:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
