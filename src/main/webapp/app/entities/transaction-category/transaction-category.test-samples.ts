import { ITransactionCategory, NewTransactionCategory } from './transaction-category.model';

export const sampleWithRequiredData: ITransactionCategory = {
  id: 11666,
  name: 'anenst',
};

export const sampleWithPartialData: ITransactionCategory = {
  id: 29951,
  name: 'wearily because',
  description: 'for',
};

export const sampleWithFullData: ITransactionCategory = {
  id: 10628,
  name: 'phew violin',
  description: 'upon',
};

export const sampleWithNewData: NewTransactionCategory = {
  name: 'till planula',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
