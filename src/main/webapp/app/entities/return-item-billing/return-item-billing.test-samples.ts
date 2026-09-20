import { IReturnItemBilling, NewReturnItemBilling } from './return-item-billing.model';

export const sampleWithRequiredData: IReturnItemBilling = {
  id: 930,
  quantity: 15131.82,
};

export const sampleWithPartialData: IReturnItemBilling = {
  id: 4111,
  quantity: 2630.92,
};

export const sampleWithFullData: IReturnItemBilling = {
  id: 25907,
  quantity: 9548.6,
  amount: 24716.86,
};

export const sampleWithNewData: NewReturnItemBilling = {
  quantity: 28859.52,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
