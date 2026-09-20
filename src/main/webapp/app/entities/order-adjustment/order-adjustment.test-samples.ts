import dayjs from 'dayjs/esm';

import { IOrderAdjustment, NewOrderAdjustment } from './order-adjustment.model';

export const sampleWithRequiredData: IOrderAdjustment = {
  id: 4036,
};

export const sampleWithPartialData: IOrderAdjustment = {
  id: 24510,
  amount: 16335.22,
  sourcePercentage: 30456.37,
  sourceRefId: 'bend gym',
  exemptAmount: 13621.24,
  includeInTax: false,
  includeInShipping: false,
  includedInPrice: false,
};

export const sampleWithFullData: IOrderAdjustment = {
  id: 29718,
  description: 'popularize',
  comments: 'quickly wholly',
  amount: 24988.97,
  sourcePercentage: 7197.86,
  sourceRefId: 'mechanically',
  exemptAmount: 28428.81,
  includeInTax: false,
  includeInShipping: true,
  includedInPrice: true,
  entryDate: dayjs('2023-12-14T19:52'),
};

export const sampleWithNewData: NewOrderAdjustment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
