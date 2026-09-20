import { IOrderItemAttachment, NewOrderItemAttachment } from './order-item-attachment.model';

export const sampleWithRequiredData: IOrderItemAttachment = {
  id: 25961,
};

export const sampleWithPartialData: IOrderItemAttachment = {
  id: 14304,
  name: 'which',
};

export const sampleWithFullData: IOrderItemAttachment = {
  id: 18347,
  name: 'between vet dusk',
};

export const sampleWithNewData: NewOrderItemAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
