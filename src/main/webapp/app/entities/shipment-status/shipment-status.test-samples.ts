import dayjs from 'dayjs/esm';

import { IShipmentStatus, NewShipmentStatus } from './shipment-status.model';

export const sampleWithRequiredData: IShipmentStatus = {
  id: 17241,
  statusDateTime: dayjs('2023-12-14T15:04'),
};

export const sampleWithPartialData: IShipmentStatus = {
  id: 8108,
  statusDateTime: dayjs('2023-12-14T05:03'),
  sequenceNo: 14168,
};

export const sampleWithFullData: IShipmentStatus = {
  id: 20521,
  statusDateTime: dayjs('2023-12-14T17:58'),
  sequenceNo: 10549,
};

export const sampleWithNewData: NewShipmentStatus = {
  statusDateTime: dayjs('2023-12-14T22:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
