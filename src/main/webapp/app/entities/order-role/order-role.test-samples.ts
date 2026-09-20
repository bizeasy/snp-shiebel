import { IOrderRole, NewOrderRole } from './order-role.model';

export const sampleWithRequiredData: IOrderRole = {
  id: 32438,
};

export const sampleWithPartialData: IOrderRole = {
  id: 14558,
};

export const sampleWithFullData: IOrderRole = {
  id: 18247,
};

export const sampleWithNewData: NewOrderRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
