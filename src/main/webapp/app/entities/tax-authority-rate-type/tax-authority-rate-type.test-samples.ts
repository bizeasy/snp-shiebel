import { ITaxAuthorityRateType, NewTaxAuthorityRateType } from './tax-authority-rate-type.model';

export const sampleWithRequiredData: ITaxAuthorityRateType = {
  id: 10193,
  name: 'handy',
};

export const sampleWithPartialData: ITaxAuthorityRateType = {
  id: 14805,
  name: 'briefly or till',
};

export const sampleWithFullData: ITaxAuthorityRateType = {
  id: 13652,
  name: 'emphasise upon gentle',
  description: 'entwine other',
};

export const sampleWithNewData: NewTaxAuthorityRateType = {
  name: 'excepting yowza gad',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
