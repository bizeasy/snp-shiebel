import { ITaxAuthority, NewTaxAuthority } from './tax-authority.model';

export const sampleWithRequiredData: ITaxAuthority = {
  id: 6916,
  name: 'oof bravely unless',
};

export const sampleWithPartialData: ITaxAuthority = {
  id: 10828,
  name: 'mmm license',
};

export const sampleWithFullData: ITaxAuthority = {
  id: 3425,
  name: 'augment whether',
};

export const sampleWithNewData: NewTaxAuthority = {
  name: 'perfectly eek ouch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
