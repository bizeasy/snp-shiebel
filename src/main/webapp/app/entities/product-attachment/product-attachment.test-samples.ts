import { IProductAttachment, NewProductAttachment } from './product-attachment.model';

export const sampleWithRequiredData: IProductAttachment = {
  id: 21640,
};

export const sampleWithPartialData: IProductAttachment = {
  id: 23782,
};

export const sampleWithFullData: IProductAttachment = {
  id: 3824,
  name: 'stuff intensely',
};

export const sampleWithNewData: NewProductAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
