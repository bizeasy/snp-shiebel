import dayjs from 'dayjs/esm';

import { IFileFolder, NewFileFolder } from './file-folder.model';

export const sampleWithRequiredData: IFileFolder = {
  id: 32500,
  name: 'whose',
};

export const sampleWithPartialData: IFileFolder = {
  id: 25910,
  name: 'mad properly uh-huh',
  isTrashed: false,
  createdDate: dayjs('2023-12-13T23:11'),
};

export const sampleWithFullData: IFileFolder = {
  id: 28572,
  name: 'furthermore fluff',
  path: 'scarily or',
  isSystemFolder: false,
  isTrashed: true,
  createdDate: dayjs('2023-12-14T05:59'),
};

export const sampleWithNewData: NewFileFolder = {
  name: 'whoever eternity seriously',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
