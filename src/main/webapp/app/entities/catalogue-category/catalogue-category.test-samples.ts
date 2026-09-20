import { ICatalogueCategory, NewCatalogueCategory } from './catalogue-category.model';

export const sampleWithRequiredData: ICatalogueCategory = {
  id: 17340,
};

export const sampleWithPartialData: ICatalogueCategory = {
  id: 26065,
  sequenceNo: 13033,
};

export const sampleWithFullData: ICatalogueCategory = {
  id: 23870,
  sequenceNo: 14781,
};

export const sampleWithNewData: NewCatalogueCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
