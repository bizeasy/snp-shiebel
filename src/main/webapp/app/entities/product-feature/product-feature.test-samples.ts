import { IProductFeature, NewProductFeature } from './product-feature.model';

export const sampleWithRequiredData: IProductFeature = {
  id: 549,
  name: 'hmph speedily lox',
};

export const sampleWithPartialData: IProductFeature = {
  id: 18303,
  name: 'yuck',
};

export const sampleWithFullData: IProductFeature = {
  id: 31274,
  name: 'underachieve gosh',
  description: 'along uh-huh healthily',
  amount: 17484.36,
  sequenceNo: 5974,
  abbrev: 'likewise jaywalk',
};

export const sampleWithNewData: NewProductFeature = {
  name: 'ick',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
