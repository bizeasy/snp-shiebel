import dayjs from 'dayjs/esm';

import { IQuoteItem, NewQuoteItem } from './quote-item.model';

export const sampleWithRequiredData: IQuoteItem = {
  id: 19994,
  quantity: 32512.34,
};

export const sampleWithPartialData: IQuoteItem = {
  id: 14469,
  quantity: 11194.15,
  quoteUnitPrice: 15147.98,
  totalAmount: 30251.26,
  estimatedDeliveryDate: dayjs('2023-12-14T00:45'),
};

export const sampleWithFullData: IQuoteItem = {
  id: 30472,
  sequenceNo: 11283,
  quantity: 4185.32,
  selectedAmount: 14409.56,
  quoteUnitPrice: 15662.49,
  totalAmount: 21699.32,
  estimatedDeliveryDate: dayjs('2023-12-14T09:01'),
  leadTimeDays: 1576,
  comments: 'without ew',
};

export const sampleWithNewData: NewQuoteItem = {
  quantity: 14753.91,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
