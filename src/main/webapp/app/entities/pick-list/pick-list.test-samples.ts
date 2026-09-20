import dayjs from 'dayjs/esm';

import { IPickList, NewPickList } from './pick-list.model';

export const sampleWithRequiredData: IPickList = {
  id: 31708,
  pickListDate: dayjs('2023-12-14T08:44'),
};

export const sampleWithPartialData: IPickList = {
  id: 26699,
  description: 'decode',
  pickListDate: dayjs('2023-12-14T19:45'),
};

export const sampleWithFullData: IPickList = {
  id: 21994,
  description: 'federate',
  pickListDate: dayjs('2023-12-14T11:45'),
};

export const sampleWithNewData: NewPickList = {
  pickListDate: dayjs('2023-12-14T22:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
