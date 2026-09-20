import dayjs from 'dayjs/esm';

import { IQuote, NewQuote } from './quote.model';

export const sampleWithRequiredData: IQuote = {
  id: 24402,
  issueDate: dayjs('2023-12-14T15:40'),
};

export const sampleWithPartialData: IQuote = {
  id: 24965,
  issueDate: dayjs('2023-12-14T21:18'),
  validFromDate: dayjs('2023-12-14T09:31'),
};

export const sampleWithFullData: IQuote = {
  id: 28771,
  name: 'phew awkwardly',
  offerNo: 'whereas why',
  issueDate: dayjs('2023-12-14T19:51'),
  validFromDate: dayjs('2023-12-14T12:51'),
  validThruDate: dayjs('2023-12-14T12:00'),
  description: 'concerning arrogantly puritan',
  offerValue: 32672.41,
  remarks: 'yawningly since',
};

export const sampleWithNewData: NewQuote = {
  issueDate: dayjs('2023-12-14T04:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
