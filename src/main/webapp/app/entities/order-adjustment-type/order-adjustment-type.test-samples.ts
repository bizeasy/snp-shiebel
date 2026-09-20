import { IOrderAdjustmentType, NewOrderAdjustmentType } from './order-adjustment-type.model';

export const sampleWithRequiredData: IOrderAdjustmentType = {
  id: 6811,
  name: 'barring',
};

export const sampleWithPartialData: IOrderAdjustmentType = {
  id: 9016,
  name: 'potentially yum',
};

export const sampleWithFullData: IOrderAdjustmentType = {
  id: 22869,
  name: 'gladly drat',
  description: 'fortunately amid',
};

export const sampleWithNewData: NewOrderAdjustmentType = {
  name: 'yippee',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
