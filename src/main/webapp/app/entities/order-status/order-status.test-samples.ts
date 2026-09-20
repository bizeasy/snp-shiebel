import dayjs from 'dayjs/esm';

import { IOrderStatus, NewOrderStatus } from './order-status.model';

export const sampleWithRequiredData: IOrderStatus = {
  id: 24134,
  statusDateTime: dayjs('2023-12-14T16:44'),
};

export const sampleWithPartialData: IOrderStatus = {
  id: 5007,
  statusDateTime: dayjs('2023-12-14T08:10'),
  sequenceNo: 23880,
};

export const sampleWithFullData: IOrderStatus = {
  id: 11371,
  statusDateTime: dayjs('2023-12-14T12:48'),
  sequenceNo: 25025,
};

export const sampleWithNewData: NewOrderStatus = {
  statusDateTime: dayjs('2023-12-14T12:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
