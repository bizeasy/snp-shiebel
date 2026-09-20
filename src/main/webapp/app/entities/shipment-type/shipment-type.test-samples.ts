import { IShipmentType, NewShipmentType } from './shipment-type.model';

export const sampleWithRequiredData: IShipmentType = {
  id: 27274,
  name: 'while untried wonderfully',
};

export const sampleWithPartialData: IShipmentType = {
  id: 31290,
  name: 'diligently CD superior',
};

export const sampleWithFullData: IShipmentType = {
  id: 1539,
  name: 'forenenst loyalty',
  description: 'boohoo whereas',
};

export const sampleWithNewData: NewShipmentType = {
  name: 'astride so dredger',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
