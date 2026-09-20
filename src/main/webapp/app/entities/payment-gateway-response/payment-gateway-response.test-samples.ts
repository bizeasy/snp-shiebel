import { IPaymentGatewayResponse, NewPaymentGatewayResponse } from './payment-gateway-response.model';

export const sampleWithRequiredData: IPaymentGatewayResponse = {
  id: 32698,
};

export const sampleWithPartialData: IPaymentGatewayResponse = {
  id: 4513,
  referenceNumber: 'customise',
};

export const sampleWithFullData: IPaymentGatewayResponse = {
  id: 29223,
  referenceNumber: 'plus',
  amount: 15412.65,
  gatewayMessage: 'emphasize',
};

export const sampleWithNewData: NewPaymentGatewayResponse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
