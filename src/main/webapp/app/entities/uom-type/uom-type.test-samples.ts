import { IUomType, NewUomType } from './uom-type.model';

export const sampleWithRequiredData: IUomType = {
  id: 25128,
  name: 'until',
};

export const sampleWithPartialData: IUomType = {
  id: 7320,
  name: 'waft',
  description: 'despite deliberately valiantly',
};

export const sampleWithFullData: IUomType = {
  id: 16099,
  name: 'indeed radiant because',
  description: 'scruple velvety sediment',
};

export const sampleWithNewData: NewUomType = {
  name: 'brand',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
