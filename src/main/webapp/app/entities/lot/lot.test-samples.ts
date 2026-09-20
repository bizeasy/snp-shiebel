import dayjs from 'dayjs/esm';

import { ILot, NewLot } from './lot.model';

export const sampleWithRequiredData: ILot = {
  id: 6773,
};

export const sampleWithPartialData: ILot = {
  id: 12042,
  quantity: 5429.19,
  expirationDate: dayjs('2023-12-13T23:06'),
};

export const sampleWithFullData: ILot = {
  id: 4111,
  creationDate: dayjs('2023-12-14T16:57'),
  quantity: 6470.86,
  expirationDate: dayjs('2023-12-14T04:33'),
  retestDate: dayjs('2023-12-14T10:39'),
};

export const sampleWithNewData: NewLot = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
