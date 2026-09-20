import { ICustRequestCategory, NewCustRequestCategory } from './cust-request-category.model';

export const sampleWithRequiredData: ICustRequestCategory = {
  id: 15395,
  name: 'splendid',
};

export const sampleWithPartialData: ICustRequestCategory = {
  id: 7297,
  name: 'foolish yowza',
  description: 'spork punctually gloss',
};

export const sampleWithFullData: ICustRequestCategory = {
  id: 27639,
  name: 'hm outrageous whoa',
  description: 'acidly forenenst anesthetize',
};

export const sampleWithNewData: NewCustRequestCategory = {
  name: 'wee cumbersome',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
