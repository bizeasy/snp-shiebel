import { IStatusCategory, NewStatusCategory } from './status-category.model';

export const sampleWithRequiredData: IStatusCategory = {
  id: 20348,
  name: 'symbolise',
};

export const sampleWithPartialData: IStatusCategory = {
  id: 13967,
  name: 'whereas',
  description: 'longingly and',
};

export const sampleWithFullData: IStatusCategory = {
  id: 15958,
  name: 'phew modulo',
  description: 'truly focused',
};

export const sampleWithNewData: NewStatusCategory = {
  name: 'freely underneath',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
