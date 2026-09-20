import { IProductStore, NewProductStore } from './product-store.model';

export const sampleWithRequiredData: IProductStore = {
  id: 30779,
  name: 'irritably',
};

export const sampleWithPartialData: IProductStore = {
  id: 19658,
  name: 'indeed firsthand',
  title: 'essence lest',
  postalAddress: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IProductStore = {
  id: 1909,
  name: 'championship',
  title: 'oh strategy',
  postalAddress: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewProductStore = {
  name: 'aha forenenst republican',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
