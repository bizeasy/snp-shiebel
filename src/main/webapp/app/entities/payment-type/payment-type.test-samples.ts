import { IPaymentType, NewPaymentType } from './payment-type.model';

export const sampleWithRequiredData: IPaymentType = {
  id: 7249,
  name: 'after beside obstruct',
};

export const sampleWithPartialData: IPaymentType = {
  id: 67,
  name: 'well scared',
  description: 'tooth expert',
};

export const sampleWithFullData: IPaymentType = {
  id: 21759,
  name: 'knuckle certainly ah',
  description: 'commodity',
};

export const sampleWithNewData: NewPaymentType = {
  name: 'shimmering who',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
