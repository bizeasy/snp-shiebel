import { IQuoteAttachment, NewQuoteAttachment } from './quote-attachment.model';

export const sampleWithRequiredData: IQuoteAttachment = {
  id: 28259,
};

export const sampleWithPartialData: IQuoteAttachment = {
  id: 23651,
};

export const sampleWithFullData: IQuoteAttachment = {
  id: 28503,
  name: 'finally',
};

export const sampleWithNewData: NewQuoteAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
