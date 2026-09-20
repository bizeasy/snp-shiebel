import { IProductFacility, NewProductFacility } from './product-facility.model';

export const sampleWithRequiredData: IProductFacility = {
  id: 11727,
};

export const sampleWithPartialData: IProductFacility = {
  id: 26669,
  reorderQty: 26263.72,
  daysToShip: 1000,
};

export const sampleWithFullData: IProductFacility = {
  id: 1105,
  minimumStock: 24304.77,
  reorderQty: 15466.43,
  daysToShip: 26933,
  lastInventoryCount: 20913.27,
};

export const sampleWithNewData: NewProductFacility = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
