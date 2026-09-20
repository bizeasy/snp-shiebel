import { ICustRequestType, NewCustRequestType } from './cust-request-type.model';

export const sampleWithRequiredData: ICustRequestType = {
  id: 3709,
  name: 'because',
};

export const sampleWithPartialData: ICustRequestType = {
  id: 15653,
  name: 'onto',
};

export const sampleWithFullData: ICustRequestType = {
  id: 23687,
  name: 'cruelty pertinent',
  description: 'instead wherever',
};

export const sampleWithNewData: NewCustRequestType = {
  name: 'failing acidly a',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
