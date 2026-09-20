import { IQuoteType, NewQuoteType } from './quote-type.model';

export const sampleWithRequiredData: IQuoteType = {
  id: 4521,
  name: 'before revitalise whether',
};

export const sampleWithPartialData: IQuoteType = {
  id: 5792,
  name: 'until',
  description: 'reorient before underneath',
};

export const sampleWithFullData: IQuoteType = {
  id: 22765,
  name: 'to',
  description: 'qua ha',
};

export const sampleWithNewData: NewQuoteType = {
  name: 'brr',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
