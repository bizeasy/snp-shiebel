import { IProductFeatureGroup, NewProductFeatureGroup } from './product-feature-group.model';

export const sampleWithRequiredData: IProductFeatureGroup = {
  id: 19623,
  name: 'log mockingly',
};

export const sampleWithPartialData: IProductFeatureGroup = {
  id: 26786,
  name: 'gnaw acceptable',
  description: 'thunderbolt promise celebrated',
};

export const sampleWithFullData: IProductFeatureGroup = {
  id: 24347,
  name: 'yahoo',
  description: 'yowza bonnet anxiously',
};

export const sampleWithNewData: NewProductFeatureGroup = {
  name: 'vibration hoick',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
