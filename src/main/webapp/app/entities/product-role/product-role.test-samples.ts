import { IProductRole, NewProductRole } from './product-role.model';

export const sampleWithRequiredData: IProductRole = {
  id: 25681,
};

export const sampleWithPartialData: IProductRole = {
  id: 30400,
};

export const sampleWithFullData: IProductRole = {
  id: 15487,
};

export const sampleWithNewData: NewProductRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
