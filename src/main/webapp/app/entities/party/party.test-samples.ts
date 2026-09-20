import dayjs from 'dayjs/esm';

import { IParty, NewParty } from './party.model';

export const sampleWithRequiredData: IParty = {
  id: 32187,
};

export const sampleWithPartialData: IParty = {
  id: 1046,
  isOrganisation: false,
  salutation: 'cemetery affect vali',
  lastName: 'Jerde',
  gender: 'MALE',
  primaryEmail: 'difficult because signature',
  isTemporaryPassword: false,
  notes: '../fake-data/blob/hipster.txt',
  postalAddress: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IParty = {
  id: 8279,
  isOrganisation: true,
  organisationName: 'incidentally',
  organisationShortName: 'kindly',
  salutation: 'curiously excluding ',
  firstName: 'Milan',
  middleName: 'stark eek',
  lastName: 'Becker',
  gender: 'FEMALE',
  birthDate: dayjs('2023-12-14'),
  primaryPhone: 'triumphantly stained',
  primaryEmail: 'except without boohoo',
  isTemporaryPassword: true,
  logoImageUrl: 'wallop',
  profileImageUrl: 'spork nor',
  notes: '../fake-data/blob/hipster.txt',
  postalAddress: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewParty = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
