import { IPartyRelationshipType, NewPartyRelationshipType } from './party-relationship-type.model';

export const sampleWithRequiredData: IPartyRelationshipType = {
  id: 29367,
  name: 'vacation what',
};

export const sampleWithPartialData: IPartyRelationshipType = {
  id: 25351,
  name: 'willfully',
  description: 'ouch embalm frankly',
};

export const sampleWithFullData: IPartyRelationshipType = {
  id: 26477,
  name: 'fen ditch metabolite',
  description: 'commemorate preside',
};

export const sampleWithNewData: NewPartyRelationshipType = {
  name: 'whether lone upon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
