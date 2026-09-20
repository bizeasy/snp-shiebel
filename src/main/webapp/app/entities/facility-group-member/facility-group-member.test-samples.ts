import dayjs from 'dayjs/esm';

import { IFacilityGroupMember, NewFacilityGroupMember } from './facility-group-member.model';

export const sampleWithRequiredData: IFacilityGroupMember = {
  id: 10690,
  fromDate: dayjs('2023-12-14T18:15'),
};

export const sampleWithPartialData: IFacilityGroupMember = {
  id: 9104,
  fromDate: dayjs('2023-12-14T15:39'),
  thruDate: dayjs('2023-12-14T01:27'),
  sequenceNo: 6129,
};

export const sampleWithFullData: IFacilityGroupMember = {
  id: 31205,
  fromDate: dayjs('2023-12-14T18:02'),
  thruDate: dayjs('2023-12-14T06:31'),
  sequenceNo: 15690,
};

export const sampleWithNewData: NewFacilityGroupMember = {
  fromDate: dayjs('2023-12-14T20:09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
