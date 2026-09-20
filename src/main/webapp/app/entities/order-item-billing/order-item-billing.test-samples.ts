import { IOrderItemBilling, NewOrderItemBilling } from './order-item-billing.model';

export const sampleWithRequiredData: IOrderItemBilling = {
  id: 24541,
  quantity: 23649.46,
};

export const sampleWithPartialData: IOrderItemBilling = {
  id: 29646,
  quantity: 27457.1,
};

export const sampleWithFullData: IOrderItemBilling = {
  id: 14626,
  quantity: 27073.9,
  amount: 29364.43,
};

export const sampleWithNewData: NewOrderItemBilling = {
  quantity: 20470.56,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
