import { IPaymentGatewayConfig, NewPaymentGatewayConfig } from './payment-gateway-config.model';

export const sampleWithRequiredData: IPaymentGatewayConfig = {
  id: 21615,
  name: 'dash amendment',
};

export const sampleWithPartialData: IPaymentGatewayConfig = {
  id: 3331,
  name: 'indeed incidentally',
  authUrl: 'who save knickers',
  releaseUrl: 'aboard tenderly',
  refundUrl: 'jazz',
};

export const sampleWithFullData: IPaymentGatewayConfig = {
  id: 22598,
  name: 'goat provided',
  authUrl: 'steel',
  releaseUrl: 'ravel bar',
  refundUrl: 'given',
};

export const sampleWithNewData: NewPaymentGatewayConfig = {
  name: 'whoever',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
