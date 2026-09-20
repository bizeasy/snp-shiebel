import { IRoleType, NewRoleType } from './role-type.model';

export const sampleWithRequiredData: IRoleType = {
  id: 16690,
  name: 'inwardly bicycle when',
};

export const sampleWithPartialData: IRoleType = {
  id: 22832,
  name: 'concerning unto plain',
};

export const sampleWithFullData: IRoleType = {
  id: 10629,
  name: 'hm pfft dispense',
  description: 'authentic spring over',
};

export const sampleWithNewData: NewRoleType = {
  name: 'drat',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
