import { IProductPricePurpose, NewProductPricePurpose } from './product-price-purpose.model';

export const sampleWithRequiredData: IProductPricePurpose = {
  id: 4347,
  name: 'turret eek',
};

export const sampleWithPartialData: IProductPricePurpose = {
  id: 13763,
  name: 'sonar lumpy',
};

export const sampleWithFullData: IProductPricePurpose = {
  id: 8604,
  name: 'into brr joyously',
  description: 'chime even',
};

export const sampleWithNewData: NewProductPricePurpose = {
  name: 'around nor analyse',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
