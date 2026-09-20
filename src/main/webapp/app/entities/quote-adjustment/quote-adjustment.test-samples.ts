import dayjs from 'dayjs/esm';

import { IQuoteAdjustment, NewQuoteAdjustment } from './quote-adjustment.model';

export const sampleWithRequiredData: IQuoteAdjustment = {
  id: 29582,
};

export const sampleWithPartialData: IQuoteAdjustment = {
  id: 1279,
  comments: 'humble',
  amount: 14247.52,
  sourceRefId: 'uh-huh aside upwardly',
  includeInShipping: true,
  includedInPrice: true,
  entryDate: dayjs('2023-12-14T15:17'),
};

export const sampleWithFullData: IQuoteAdjustment = {
  id: 12416,
  description: 'forgery dilate that',
  comments: 'likewise',
  amount: 11011,
  sourcePercentage: 13870.51,
  sourceRefId: 'as singe rust',
  exemptAmount: 7276.13,
  includeInTax: false,
  includeInShipping: false,
  includedInPrice: true,
  entryDate: dayjs('2023-12-14T17:24'),
};

export const sampleWithNewData: NewQuoteAdjustment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
