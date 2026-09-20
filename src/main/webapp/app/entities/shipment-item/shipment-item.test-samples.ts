import { IShipmentItem, NewShipmentItem } from './shipment-item.model';

export const sampleWithRequiredData: IShipmentItem = {
  id: 23832,
  quantity: 1253.04,
};

export const sampleWithPartialData: IShipmentItem = {
  id: 4637,
  quantity: 7126,
  message: 'dispense ick sugary',
};

export const sampleWithFullData: IShipmentItem = {
  id: 32058,
  sequenceNo: 22149,
  quantity: 21930.7,
  message: 'magnetize couch',
};

export const sampleWithNewData: NewShipmentItem = {
  quantity: 13505.91,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
