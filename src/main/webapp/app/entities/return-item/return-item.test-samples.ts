import { IReturnItem, NewReturnItem } from './return-item.model';

export const sampleWithRequiredData: IReturnItem = {
  id: 21671,
  returnQuantity: 20883.83,
};

export const sampleWithPartialData: IReturnItem = {
  id: 6265,
  sequenceNo: 8892,
  returnQuantity: 22053.21,
  returnItemPurpose: 'horn what',
};

export const sampleWithFullData: IReturnItem = {
  id: 378,
  sequenceNo: 3205,
  description: 'nutritious',
  returnQuantity: 2388.66,
  receivedQuantity: 5104.74,
  returnPrice: 24285.57,
  returnItemPurpose: 'yahoo cook than',
};

export const sampleWithNewData: NewReturnItem = {
  returnQuantity: 24194.52,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
