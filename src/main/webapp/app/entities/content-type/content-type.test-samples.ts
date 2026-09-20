import { IContentType, NewContentType } from './content-type.model';

export const sampleWithRequiredData: IContentType = {
  id: 10079,
  name: 'supplier before',
};

export const sampleWithPartialData: IContentType = {
  id: 8856,
  name: 'ravel outflank',
};

export const sampleWithFullData: IContentType = {
  id: 21077,
  name: 'supposing um broadcast',
};

export const sampleWithNewData: NewContentType = {
  name: 'while uselessly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
