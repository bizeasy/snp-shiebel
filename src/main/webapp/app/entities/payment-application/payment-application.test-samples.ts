import { IPaymentApplication, NewPaymentApplication } from './payment-application.model';

export const sampleWithRequiredData: IPaymentApplication = {
  id: 28109,
  amountApplied: 22755.88,
};

export const sampleWithPartialData: IPaymentApplication = {
  id: 16155,
  amountApplied: 4572.25,
};

export const sampleWithFullData: IPaymentApplication = {
  id: 25615,
  amountApplied: 22279.97,
};

export const sampleWithNewData: NewPaymentApplication = {
  amountApplied: 29921.74,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
