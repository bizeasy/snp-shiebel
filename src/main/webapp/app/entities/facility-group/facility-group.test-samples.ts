import { IFacilityGroup, NewFacilityGroup } from './facility-group.model';

export const sampleWithRequiredData: IFacilityGroup = {
  id: 23764,
  name: 'generally blaring amend',
};

export const sampleWithPartialData: IFacilityGroup = {
  id: 19021,
  name: 'tail astride characterization',
};

export const sampleWithFullData: IFacilityGroup = {
  id: 957,
  name: 'to within',
  description: 'eek from thoroughly',
};

export const sampleWithNewData: NewFacilityGroup = {
  name: 'opposite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
