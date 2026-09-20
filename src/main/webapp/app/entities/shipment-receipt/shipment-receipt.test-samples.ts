import dayjs from 'dayjs/esm';

import { IShipmentReceipt, NewShipmentReceipt } from './shipment-receipt.model';

export const sampleWithRequiredData: IShipmentReceipt = {
  id: 5078,
  quantity: 23141.74,
};

export const sampleWithPartialData: IShipmentReceipt = {
  id: 23195,
  sequenceNo: 16611,
  quantity: 21556.44,
  quantityRejected: 9441.63,
};

export const sampleWithFullData: IShipmentReceipt = {
  id: 6097,
  sequenceNo: 27887,
  quantity: 10006.37,
  quantityAccepted: 31639.78,
  quantityRejected: 26448.34,
  receivedDate: dayjs('2023-12-14T17:40'),
  message: 'bah decryption',
};

export const sampleWithNewData: NewShipmentReceipt = {
  quantity: 10719.92,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
