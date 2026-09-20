import { IKeywordType, NewKeywordType } from './keyword-type.model';

export const sampleWithRequiredData: IKeywordType = {
  id: 15903,
  name: 'though perfectly',
};

export const sampleWithPartialData: IKeywordType = {
  id: 27791,
  name: 'never baseboard firsthand',
};

export const sampleWithFullData: IKeywordType = {
  id: 12858,
  name: 'unfurl whispered',
};

export const sampleWithNewData: NewKeywordType = {
  name: 'freely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
