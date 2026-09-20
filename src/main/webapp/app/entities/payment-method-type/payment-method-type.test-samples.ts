import { IPaymentMethodType, NewPaymentMethodType } from './payment-method-type.model';

export const sampleWithRequiredData: IPaymentMethodType = {
  id: 30246,
  name: 'phony',
};

export const sampleWithPartialData: IPaymentMethodType = {
  id: 18808,
  name: 'ah newsletter',
};

export const sampleWithFullData: IPaymentMethodType = {
  id: 10871,
  name: 'versus',
  description: 'whack even',
};

export const sampleWithNewData: NewPaymentMethodType = {
  name: 'tune',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
