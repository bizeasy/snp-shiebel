import { IGeoPoint, NewGeoPoint } from './geo-point.model';

export const sampleWithRequiredData: IGeoPoint = {
  id: 18932,
};

export const sampleWithPartialData: IGeoPoint = {
  id: 18041,
  latitude: 3179.45,
};

export const sampleWithFullData: IGeoPoint = {
  id: 24447,
  latitude: 17290.92,
  longitude: 32400.5,
};

export const sampleWithNewData: NewGeoPoint = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
