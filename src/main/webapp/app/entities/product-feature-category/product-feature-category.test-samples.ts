import { IProductFeatureCategory, NewProductFeatureCategory } from './product-feature-category.model';

export const sampleWithRequiredData: IProductFeatureCategory = {
  id: 25332,
  name: 'lest',
};

export const sampleWithPartialData: IProductFeatureCategory = {
  id: 16628,
  name: 'how',
};

export const sampleWithFullData: IProductFeatureCategory = {
  id: 14875,
  name: 'out including hard-to-find',
  description: 'pointless loosely draft',
};

export const sampleWithNewData: NewProductFeatureCategory = {
  name: 'machine',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
