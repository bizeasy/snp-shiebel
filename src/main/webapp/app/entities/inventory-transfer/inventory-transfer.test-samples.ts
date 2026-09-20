import dayjs from 'dayjs/esm';

import { IInventoryTransfer, NewInventoryTransfer } from './inventory-transfer.model';

export const sampleWithRequiredData: IInventoryTransfer = {
  id: 6181,
};

export const sampleWithPartialData: IInventoryTransfer = {
  id: 15300,
  comments: 'without why',
};

export const sampleWithFullData: IInventoryTransfer = {
  id: 6233,
  sentDate: dayjs('2023-12-14T01:28'),
  receivedDate: dayjs('2023-12-14T16:29'),
  comments: 'than joyfully mousse',
};

export const sampleWithNewData: NewInventoryTransfer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
