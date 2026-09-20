import { IProductCategory, NewProductCategory } from './product-category.model';

export const sampleWithRequiredData: IProductCategory = {
  id: 14928,
  name: 'jam-packed retract',
};

export const sampleWithPartialData: IProductCategory = {
  id: 21276,
  name: 'simple',
  altLargeImageUrl: 'bustling',
  info: '../fake-data/blob/hipster.txt',
  isBanner: true,
};

export const sampleWithFullData: IProductCategory = {
  id: 13570,
  name: 'happy possession widow',
  description: 'pro unexpectedly forenenst',
  longDescription: 'uh-huh hence vet',
  attribute: 'oh',
  sequenceNo: 511,
  imageUrl: 'circa righteously pepper',
  altImageUrl: 'wrongly allocation ha',
  bannerUrl: 'oh but geez',
  largeImageUrl: 'bug',
  altLargeImageUrl: 'creak',
  info: '../fake-data/blob/hipster.txt',
  isBanner: false,
};

export const sampleWithNewData: NewProductCategory = {
  name: 'suspiciously',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
