import { ILocationType, NewLocationType } from './location-type.model';

export const sampleWithRequiredData: ILocationType = {
  id: 6653,
  name: 'stormy',
};

export const sampleWithPartialData: ILocationType = {
  id: 23775,
  name: 'wherever',
  description: 'if mysterious',
};

export const sampleWithFullData: ILocationType = {
  id: 21438,
  name: 'hm',
  description: 'nor',
};

export const sampleWithNewData: NewLocationType = {
  name: 'thoroughly decouple unaccountably',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
