import { ICatalogueCategoryType, NewCatalogueCategoryType } from './catalogue-category-type.model';

export const sampleWithRequiredData: ICatalogueCategoryType = {
  id: 28664,
  name: 'whoa fussy',
};

export const sampleWithPartialData: ICatalogueCategoryType = {
  id: 3658,
  name: 'during nor',
  description: 'extra-large deceivingly sweetly',
};

export const sampleWithFullData: ICatalogueCategoryType = {
  id: 3664,
  name: 'castanet confusion',
  description: 'defrag',
};

export const sampleWithNewData: NewCatalogueCategoryType = {
  name: 'hence',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
