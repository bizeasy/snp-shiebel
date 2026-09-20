import { IOrderShipment, NewOrderShipment } from './order-shipment.model';

export const sampleWithRequiredData: IOrderShipment = {
  id: 13006,
  quantity: 15559.19,
};

export const sampleWithPartialData: IOrderShipment = {
  id: 20966,
  quantity: 11754.01,
};

export const sampleWithFullData: IOrderShipment = {
  id: 23121,
  quantity: 6655.95,
};

export const sampleWithNewData: NewOrderShipment = {
  quantity: 11785.42,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
