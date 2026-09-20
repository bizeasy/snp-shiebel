import { IPartyClassificationGroup, NewPartyClassificationGroup } from './party-classification-group.model';

export const sampleWithRequiredData: IPartyClassificationGroup = {
  id: 8543,
  name: 'dividend known ring',
};

export const sampleWithPartialData: IPartyClassificationGroup = {
  id: 26702,
  name: 'clone',
  description: 'heartache',
};

export const sampleWithFullData: IPartyClassificationGroup = {
  id: 20029,
  name: 'unselfish',
  description: 'after fail',
};

export const sampleWithNewData: NewPartyClassificationGroup = {
  name: 'despite if',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
