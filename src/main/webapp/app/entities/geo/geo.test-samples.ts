import { IGeo, NewGeo } from './geo.model';

export const sampleWithRequiredData: IGeo = {
  id: 7538,
  name: 'past',
  code: 'heartbeat ',
};

export const sampleWithPartialData: IGeo = {
  id: 195,
  name: 'petal for',
  code: 'yahoo',
  abbreviation: 'absent',
};

export const sampleWithFullData: IGeo = {
  id: 14728,
  name: 'tag',
  code: 'excitedly ',
  abbreviation: 'polarisati',
};

export const sampleWithNewData: NewGeo = {
  name: 'that peony omelet',
  code: 'against',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
