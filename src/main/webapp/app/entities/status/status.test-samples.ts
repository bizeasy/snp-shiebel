import { IStatus, NewStatus } from './status.model';

export const sampleWithRequiredData: IStatus = {
  id: 20359,
  name: 'eke',
};

export const sampleWithPartialData: IStatus = {
  id: 13685,
  name: 'supportive wallaby',
};

export const sampleWithFullData: IStatus = {
  id: 26853,
  name: 'yearly given',
  sequenceNo: 21054,
  description: 'midst forsaken exploration',
};

export const sampleWithNewData: NewStatus = {
  name: 'rationale wasteful beautifully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
