import { IQuoteRole, NewQuoteRole } from './quote-role.model';

export const sampleWithRequiredData: IQuoteRole = {
  id: 18287,
};

export const sampleWithPartialData: IQuoteRole = {
  id: 17924,
};

export const sampleWithFullData: IQuoteRole = {
  id: 30321,
};

export const sampleWithNewData: NewQuoteRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
