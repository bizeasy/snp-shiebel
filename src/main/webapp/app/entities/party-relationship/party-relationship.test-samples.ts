import dayjs from 'dayjs/esm';

import { IPartyRelationship, NewPartyRelationship } from './party-relationship.model';

export const sampleWithRequiredData: IPartyRelationship = {
  id: 7498,
  fromDate: dayjs('2023-12-14T08:54'),
};

export const sampleWithPartialData: IPartyRelationship = {
  id: 18578,
  fromDate: dayjs('2023-12-14T18:33'),
  relationshipName: 'since video',
};

export const sampleWithFullData: IPartyRelationship = {
  id: 24244,
  fromDate: dayjs('2023-12-14T02:13'),
  thruDate: dayjs('2023-12-14T06:21'),
  relationshipName: 'skeleton',
};

export const sampleWithNewData: NewPartyRelationship = {
  fromDate: dayjs('2023-12-14T00:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
