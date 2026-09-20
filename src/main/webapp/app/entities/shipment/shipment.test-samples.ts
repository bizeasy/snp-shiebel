import dayjs from 'dayjs/esm';

import { IShipment, NewShipment } from './shipment.model';

export const sampleWithRequiredData: IShipment = {
  id: 27253,
};

export const sampleWithPartialData: IShipment = {
  id: 15151,
  estimatedShipDate: dayjs('2023-12-14T02:40'),
  estimatedReadyDate: dayjs('2023-12-14T11:46'),
  estimatedArrivalDate: dayjs('2023-12-14T10:57'),
};

export const sampleWithFullData: IShipment = {
  id: 31355,
  estimatedShipDate: dayjs('2023-12-14T08:24'),
  estimatedReadyDate: dayjs('2023-12-14T08:30'),
  estimatedArrivalDate: dayjs('2023-12-14T14:24'),
  estimatedShipCost: 23011.93,
};

export const sampleWithNewData: NewShipment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
