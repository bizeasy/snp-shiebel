import dayjs from 'dayjs/esm';

import { IFileShare, NewFileShare } from './file-share.model';

export const sampleWithRequiredData: IFileShare = {
  id: 24674,
  permission: 'COMMENT',
  sharedDate: dayjs('2023-12-14T15:12'),
};

export const sampleWithPartialData: IFileShare = {
  id: 31517,
  permission: 'OWNER',
  sharedWithEmail: 'disloyal',
  sharedDate: dayjs('2023-12-14T06:25'),
};

export const sampleWithFullData: IFileShare = {
  id: 20917,
  permission: 'EDIT',
  sharedWithEmail: 'cinder absentmindedly finally',
  sharedDate: dayjs('2023-12-14T01:47'),
  expiryDate: dayjs('2023-12-14T19:28'),
  accessToken: 'aha',
  isRevoked: false,
};

export const sampleWithNewData: NewFileShare = {
  permission: 'VIEW',
  sharedDate: dayjs('2023-12-14T07:23'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
