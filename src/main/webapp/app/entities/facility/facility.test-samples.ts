import { IFacility, NewFacility } from './facility.model';

export const sampleWithRequiredData: IFacility = {
  id: 28148,
  name: 'besides ick if',
};

export const sampleWithPartialData: IFacility = {
  id: 1802,
  name: 'anesthetize unusual',
  description: 'pfft veto galvanize',
};

export const sampleWithFullData: IFacility = {
  id: 5460,
  name: 'friendly',
  description: 'engage regarding',
  facilitySize: 29506.29,
};

export const sampleWithNewData: NewFacility = {
  name: 'dimly rot',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
