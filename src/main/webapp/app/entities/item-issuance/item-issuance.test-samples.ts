import dayjs from 'dayjs/esm';

import { IItemIssuance, NewItemIssuance } from './item-issuance.model';

export const sampleWithRequiredData: IItemIssuance = {
  id: 19866,
  quantity: 28038.74,
};

export const sampleWithPartialData: IItemIssuance = {
  id: 8838,
  issuedBy: 'below brightly disarm',
  quantity: 22047.19,
};

export const sampleWithFullData: IItemIssuance = {
  id: 27810,
  message: 'circulate which amazing',
  issuedDate: dayjs('2023-12-14T05:29'),
  issuedBy: 'drag',
  quantity: 4034.57,
  cancelQuantity: 18094.69,
};

export const sampleWithNewData: NewItemIssuance = {
  quantity: 2584.73,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
