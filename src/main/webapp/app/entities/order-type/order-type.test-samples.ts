import { IOrderType, NewOrderType } from './order-type.model';

export const sampleWithRequiredData: IOrderType = {
  id: 4310,
  name: 'once clamor',
};

export const sampleWithPartialData: IOrderType = {
  id: 8655,
  name: 'ack',
};

export const sampleWithFullData: IOrderType = {
  id: 32332,
  name: 'since ha',
  description: 'under whoever',
};

export const sampleWithNewData: NewOrderType = {
  name: 'yahoo wallop',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
