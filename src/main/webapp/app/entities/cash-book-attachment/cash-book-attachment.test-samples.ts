import { ICashBookAttachment, NewCashBookAttachment } from './cash-book-attachment.model';

export const sampleWithRequiredData: ICashBookAttachment = {
  id: 338,
};

export const sampleWithPartialData: ICashBookAttachment = {
  id: 10339,
  name: 'hm option carefree',
};

export const sampleWithFullData: ICashBookAttachment = {
  id: 7353,
  name: 'wherever',
};

export const sampleWithNewData: NewCashBookAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
