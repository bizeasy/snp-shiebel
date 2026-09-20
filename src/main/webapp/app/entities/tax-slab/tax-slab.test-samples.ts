import { ITaxSlab, NewTaxSlab } from './tax-slab.model';

export const sampleWithRequiredData: ITaxSlab = {
  id: 26881,
  name: 'monthly woot',
  rate: 9662.85,
};

export const sampleWithPartialData: ITaxSlab = {
  id: 28486,
  name: 'overconfidently rationali',
  rate: 5580.9,
};

export const sampleWithFullData: ITaxSlab = {
  id: 25631,
  name: 'before certainly',
  rate: 26856.09,
};

export const sampleWithNewData: NewTaxSlab = {
  name: 'along lest',
  rate: 24443.96,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
