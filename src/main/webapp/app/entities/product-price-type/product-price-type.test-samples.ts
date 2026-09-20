import { IProductPriceType, NewProductPriceType } from './product-price-type.model';

export const sampleWithRequiredData: IProductPriceType = {
  id: 31904,
  name: 'coolly',
};

export const sampleWithPartialData: IProductPriceType = {
  id: 20788,
  name: 'traditionalism',
  description: 'lest what',
};

export const sampleWithFullData: IProductPriceType = {
  id: 31929,
  name: 'entwine',
  description: 'loosely joshingly',
};

export const sampleWithNewData: NewProductPriceType = {
  name: 'ferret next',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
