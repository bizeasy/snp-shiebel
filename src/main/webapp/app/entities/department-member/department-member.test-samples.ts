import dayjs from 'dayjs/esm';

import { IDepartmentMember, NewDepartmentMember } from './department-member.model';

export const sampleWithRequiredData: IDepartmentMember = {
  id: 23299,
  fromDate: dayjs('2023-12-13T23:14'),
};

export const sampleWithPartialData: IDepartmentMember = {
  id: 27970,
  fromDate: dayjs('2023-12-14T18:40'),
};

export const sampleWithFullData: IDepartmentMember = {
  id: 7333,
  fromDate: dayjs('2023-12-14T17:48'),
  thruDate: dayjs('2023-12-14T05:59'),
  isPrimary: false,
};

export const sampleWithNewData: NewDepartmentMember = {
  fromDate: dayjs('2023-12-14T19:25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
