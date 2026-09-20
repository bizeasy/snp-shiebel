import { IShipmentMethodType, NewShipmentMethodType } from './shipment-method-type.model';

export const sampleWithRequiredData: IShipmentMethodType = {
  id: 13935,
  name: 'queasy volunteer pleasing',
};

export const sampleWithPartialData: IShipmentMethodType = {
  id: 10876,
  name: 'as',
};

export const sampleWithFullData: IShipmentMethodType = {
  id: 7400,
  name: 'fooey since although',
  description: 'rosy despite',
  sequenceNo: 633,
};

export const sampleWithNewData: NewShipmentMethodType = {
  name: 'notwithstanding',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
