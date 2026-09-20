import { IProductAssocType, NewProductAssocType } from './product-assoc-type.model';

export const sampleWithRequiredData: IProductAssocType = {
  id: 1969,
  name: 'ah quirkily',
};

export const sampleWithPartialData: IProductAssocType = {
  id: 4392,
  name: 'whine',
};

export const sampleWithFullData: IProductAssocType = {
  id: 17118,
  name: 'boo',
  description: 'gentle pave swing',
};

export const sampleWithNewData: NewProductAssocType = {
  name: 'patiently loose',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
