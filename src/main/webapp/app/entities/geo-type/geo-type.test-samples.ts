import { IGeoType, NewGeoType } from './geo-type.model';

export const sampleWithRequiredData: IGeoType = {
  id: 27638,
  name: 'strict gadzooks',
};

export const sampleWithPartialData: IGeoType = {
  id: 9403,
  name: 'scholarship',
};

export const sampleWithFullData: IGeoType = {
  id: 14451,
  name: 'miskey convince',
};

export const sampleWithNewData: NewGeoType = {
  name: 'birth brightly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
