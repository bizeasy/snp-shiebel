import { IProductCategoryType, NewProductCategoryType } from './product-category-type.model';

export const sampleWithRequiredData: IProductCategoryType = {
  id: 12691,
  name: 'oh tight incidentally',
};

export const sampleWithPartialData: IProductCategoryType = {
  id: 16599,
  name: 'unlike',
  description: 'allegation mmm',
};

export const sampleWithFullData: IProductCategoryType = {
  id: 3809,
  name: 'wash hunt',
  description: 'qualified across',
};

export const sampleWithNewData: NewProductCategoryType = {
  name: 'strictly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
