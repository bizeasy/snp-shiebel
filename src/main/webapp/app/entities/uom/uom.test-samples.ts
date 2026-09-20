import { IUom, NewUom } from './uom.model';

export const sampleWithRequiredData: IUom = {
  id: 8916,
  name: 'sleepily gleaming',
};

export const sampleWithPartialData: IUom = {
  id: 4390,
  name: 'obnoxiously biodegrade gulp',
  abbreviation: 'suitcase f',
};

export const sampleWithFullData: IUom = {
  id: 25356,
  name: 'hence sonata wriggler',
  description: 'outset',
  sequenceNo: 8679,
  abbreviation: 'when',
};

export const sampleWithNewData: NewUom = {
  name: 'if diligently',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
