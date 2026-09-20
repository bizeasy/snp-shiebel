import { IPartyClassificationType, NewPartyClassificationType } from './party-classification-type.model';

export const sampleWithRequiredData: IPartyClassificationType = {
  id: 26144,
  name: 'oil since though',
};

export const sampleWithPartialData: IPartyClassificationType = {
  id: 22455,
  name: 'warm',
  description: 'trustworthy ah not',
};

export const sampleWithFullData: IPartyClassificationType = {
  id: 21806,
  name: 'on ugh',
  description: 'promptly',
};

export const sampleWithNewData: NewPartyClassificationType = {
  name: 'burdensome ruddy',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
