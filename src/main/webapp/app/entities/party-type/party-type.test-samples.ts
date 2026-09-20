import { IPartyType, NewPartyType } from './party-type.model';

export const sampleWithRequiredData: IPartyType = {
  id: 12698,
  name: 'enchanted overconfidently',
};

export const sampleWithPartialData: IPartyType = {
  id: 28559,
  name: 'convince tabulate',
};

export const sampleWithFullData: IPartyType = {
  id: 20144,
  name: 'than toothbrush',
  description: 'devise whether characterization',
};

export const sampleWithNewData: NewPartyType = {
  name: 'confirm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
