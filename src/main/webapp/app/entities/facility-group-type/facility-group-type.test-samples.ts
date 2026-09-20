import { IFacilityGroupType, NewFacilityGroupType } from './facility-group-type.model';

export const sampleWithRequiredData: IFacilityGroupType = {
  id: 3304,
  name: 'youthfully duh',
};

export const sampleWithPartialData: IFacilityGroupType = {
  id: 23956,
  name: 'glimmer ha',
};

export const sampleWithFullData: IFacilityGroupType = {
  id: 18368,
  name: 'livid warm bashfully',
  description: 'uh-huh',
};

export const sampleWithNewData: NewFacilityGroupType = {
  name: 'siege',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
