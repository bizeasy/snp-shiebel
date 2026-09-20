import { IOrderAttachment, NewOrderAttachment } from './order-attachment.model';

export const sampleWithRequiredData: IOrderAttachment = {
  id: 17202,
};

export const sampleWithPartialData: IOrderAttachment = {
  id: 15759,
};

export const sampleWithFullData: IOrderAttachment = {
  id: 1171,
  name: 'valuable of',
};

export const sampleWithNewData: NewOrderAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
