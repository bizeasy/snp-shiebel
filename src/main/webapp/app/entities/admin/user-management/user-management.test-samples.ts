import dayjs from 'dayjs/esm';

import { IUserManagement, NewUserManagement } from './user-management.model';

export const sampleWithRequiredData: IUserManagement = {
  login: 'Randall.Sauer',
  email: 'Lucienne5@hotmail.com',
};

export const sampleWithPartialData: IUserManagement = {
  id: 18824,
  login: 'Sasha.McCullough4',
  firstName: 'Parker',
  lastName: 'Dietrich',
  email: 'Janessa44@hotmail.com',
  activated: false,
  langKey: 'hi',
  lastModifiedBy: 'upon around',
  lastModifiedDate: dayjs('2023-12-14T20:41'),
};

export const sampleWithFullData: IUserManagement = {
  id: 26558,
  login: 'Brisa72',
  firstName: 'Lorena',
  lastName: 'Bode',
  email: 'Rylan_Altenwerth65@hotmail.com',
  activated: false,
  langKey: 'te',
  imageUrl: 'the',
  createdBy: 'yahoo',
  createdDate: dayjs('2023-12-14T12:04'),
  lastModifiedBy: 'airline',
  lastModifiedDate: dayjs('2023-12-14T12:33'),
};

export const sampleWithNewData: NewUserManagement = {
  email: 'Jasen82@yahoo.com',
  login: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
