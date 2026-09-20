import { IProductFeatureType, NewProductFeatureType } from './product-feature-type.model';

export const sampleWithRequiredData: IProductFeatureType = {
  id: 25153,
  name: 'hourly whenever',
};

export const sampleWithPartialData: IProductFeatureType = {
  id: 15715,
  name: 'qua obnoxiously um',
};

export const sampleWithFullData: IProductFeatureType = {
  id: 15067,
  name: 'finally careless inside',
  description: 'amid custody',
};

export const sampleWithNewData: NewProductFeatureType = {
  name: 'sharply whether',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
