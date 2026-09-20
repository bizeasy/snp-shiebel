import { IProductType, NewProductType } from './product-type.model';

export const sampleWithRequiredData: IProductType = {
  id: 30743,
  name: 'chilly',
};

export const sampleWithPartialData: IProductType = {
  id: 4659,
  name: 'down',
  description: 'behind',
};

export const sampleWithFullData: IProductType = {
  id: 21539,
  name: 'readjust fun',
  description: 'probable',
};

export const sampleWithNewData: NewProductType = {
  name: 'cram',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
