import { IPicklistRole, NewPicklistRole } from './picklist-role.model';

export const sampleWithRequiredData: IPicklistRole = {
  id: 30086,
};

export const sampleWithPartialData: IPicklistRole = {
  id: 25875,
};

export const sampleWithFullData: IPicklistRole = {
  id: 8636,
};

export const sampleWithNewData: NewPicklistRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
