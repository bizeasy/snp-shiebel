import { IProductKeyword, NewProductKeyword } from './product-keyword.model';

export const sampleWithRequiredData: IProductKeyword = {
  id: 15559,
  keyword: 'what massive concerning',
};

export const sampleWithPartialData: IProductKeyword = {
  id: 16685,
  keyword: 'railway',
  relevancyWeight: 9422,
};

export const sampleWithFullData: IProductKeyword = {
  id: 18073,
  keyword: 'construe coaxingly meh',
  relevancyWeight: 28788,
};

export const sampleWithNewData: NewProductKeyword = {
  keyword: 'anti up',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
