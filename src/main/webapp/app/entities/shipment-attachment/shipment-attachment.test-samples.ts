import { IShipmentAttachment, NewShipmentAttachment } from './shipment-attachment.model';

export const sampleWithRequiredData: IShipmentAttachment = {
  id: 8444,
};

export const sampleWithPartialData: IShipmentAttachment = {
  id: 8035,
};

export const sampleWithFullData: IShipmentAttachment = {
  id: 18176,
  name: 'but wear',
};

export const sampleWithNewData: NewShipmentAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
