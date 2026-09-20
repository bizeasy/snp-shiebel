import { IInventoryItemVariance, NewInventoryItemVariance } from './inventory-item-variance.model';

export const sampleWithRequiredData: IInventoryItemVariance = {
  id: 13638,
};

export const sampleWithPartialData: IInventoryItemVariance = {
  id: 16559,
  varianceReason: 'scram unnecessarily',
  atpVar: 11545.92,
  comments: 'ah cleaner',
};

export const sampleWithFullData: IInventoryItemVariance = {
  id: 17564,
  varianceReason: 'near aboard yet',
  atpVar: 29839.16,
  qohVar: 21510.72,
  comments: 'lively classic tedious',
};

export const sampleWithNewData: NewInventoryItemVariance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
