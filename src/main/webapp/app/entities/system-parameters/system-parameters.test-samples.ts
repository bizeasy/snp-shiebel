import { ISystemParameters, NewSystemParameters } from './system-parameters.model';

export const sampleWithRequiredData: ISystemParameters = {
  id: 8364,
  key: 'huzzah',
};

export const sampleWithPartialData: ISystemParameters = {
  id: 25494,
  key: 'needily pluck',
  value: 'forearm circa squiggly',
  description: 'since hope',
};

export const sampleWithFullData: ISystemParameters = {
  id: 5021,
  key: 'jubilant finished desecrate',
  value: 'broadly',
  description: 'sedately boss wide',
  environment: 'questioningly octave',
};

export const sampleWithNewData: NewSystemParameters = {
  key: 'than sunbathe beside',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
