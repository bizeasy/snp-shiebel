import { ICarrierShipmentMethod, NewCarrierShipmentMethod } from './carrier-shipment-method.model';

export const sampleWithRequiredData: ICarrierShipmentMethod = {
  id: 5060,
};

export const sampleWithPartialData: ICarrierShipmentMethod = {
  id: 32402,
  sequenceNo: 10584,
};

export const sampleWithFullData: ICarrierShipmentMethod = {
  id: 18783,
  sequenceNo: 10195,
  carrierServiceCode: 'across event',
};

export const sampleWithNewData: NewCarrierShipmentMethod = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
