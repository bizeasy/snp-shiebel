import { IPartyRole, NewPartyRole } from './party-role.model';

export const sampleWithRequiredData: IPartyRole = {
  id: 4867,
};

export const sampleWithPartialData: IPartyRole = {
  id: 12551,
};

export const sampleWithFullData: IPartyRole = {
  id: 31062,
};

export const sampleWithNewData: NewPartyRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
