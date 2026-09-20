import { IReturnHeaderType, NewReturnHeaderType } from './return-header-type.model';

export const sampleWithRequiredData: IReturnHeaderType = {
  id: 4570,
  name: 'reclassify unless about',
};

export const sampleWithPartialData: IReturnHeaderType = {
  id: 13690,
  name: 'aha unfortunately',
};

export const sampleWithFullData: IReturnHeaderType = {
  id: 22096,
  name: 'amid during',
  description: 'meh provided',
};

export const sampleWithNewData: NewReturnHeaderType = {
  name: 'pish likewise slump',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
