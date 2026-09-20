import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 32001,
  name: 'phew',
};

export const sampleWithPartialData: IDepartment = {
  id: 22549,
  name: 'derby slip gah',
  isActive: true,
};

export const sampleWithFullData: IDepartment = {
  id: 13276,
  name: 'endow',
  code: 'headline save',
  description: 'until',
  isActive: true,
};

export const sampleWithNewData: NewDepartment = {
  name: 'peaceful if',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
