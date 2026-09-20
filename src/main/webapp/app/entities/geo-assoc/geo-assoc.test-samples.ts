import { IGeoAssoc, NewGeoAssoc } from './geo-assoc.model';

export const sampleWithRequiredData: IGeoAssoc = {
  id: 27106,
};

export const sampleWithPartialData: IGeoAssoc = {
  id: 23945,
};

export const sampleWithFullData: IGeoAssoc = {
  id: 12776,
};

export const sampleWithNewData: NewGeoAssoc = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
