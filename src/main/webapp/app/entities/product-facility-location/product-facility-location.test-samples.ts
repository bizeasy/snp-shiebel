import { IProductFacilityLocation, NewProductFacilityLocation } from './product-facility-location.model';

export const sampleWithRequiredData: IProductFacilityLocation = {
  id: 12409,
};

export const sampleWithPartialData: IProductFacilityLocation = {
  id: 27892,
  minimumStock: 26798,
};

export const sampleWithFullData: IProductFacilityLocation = {
  id: 10319,
  minimumStock: 9326.96,
  moveQuantity: 359.33,
};

export const sampleWithNewData: NewProductFacilityLocation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
