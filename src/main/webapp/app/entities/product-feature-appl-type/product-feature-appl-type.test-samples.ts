import { IProductFeatureApplType, NewProductFeatureApplType } from './product-feature-appl-type.model';

export const sampleWithRequiredData: IProductFeatureApplType = {
  id: 17218,
  name: 'plastic like',
};

export const sampleWithPartialData: IProductFeatureApplType = {
  id: 24711,
  name: 'given amid',
  description: 'oh doing',
};

export const sampleWithFullData: IProductFeatureApplType = {
  id: 18162,
  name: 'likewise gosh',
  description: 'following',
};

export const sampleWithNewData: NewProductFeatureApplType = {
  name: 'until',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
