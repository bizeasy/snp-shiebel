import { IGeoAssocType, NewGeoAssocType } from './geo-assoc-type.model';

export const sampleWithRequiredData: IGeoAssocType = {
  id: 25533,
  name: 'blah versus however',
};

export const sampleWithPartialData: IGeoAssocType = {
  id: 29655,
  name: 'volleyball emboss',
};

export const sampleWithFullData: IGeoAssocType = {
  id: 1728,
  name: 'geez',
};

export const sampleWithNewData: NewGeoAssocType = {
  name: 'oh suitcase between',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
