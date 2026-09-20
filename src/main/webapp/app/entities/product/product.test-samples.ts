import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  name: 'hm sleepily',
};

export const sampleWithPartialData: IProduct = {
  id: 8672,
  name: 'quickly',
  brandName: 'taro',
  quantityIncluded: 32197.46,
  piecesIncluded: 2394,
  depth: 311.1,
  smallImageUrl: 'frozen pointed',
  mediumImageUrl: 'excitable',
  detailImageUrl: 'boohoo when before',
  originalImageUrl: 'blend how heavily',
  altImageUrl5: 'since',
  price: 26516.53,
  cgst: 3284.26,
  igst: 26295.61,
  cgstPercentage: 30684.76,
  sgstPercentage: 2116.03,
  totalPrice: 9712.61,
  description: 'after',
  info: '../fake-data/blob/hipster.txt',
  isVariant: false,
  requireInventory: true,
  returnable: false,
  isPopular: true,
  isBanner: false,
  bannerUrl: 'a',
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  name: 'cafe',
  internalName: 'along amongst',
  brandName: 'hm ugh',
  variant: 'fatally',
  sku: 'headline incidentally hence',
  introductionDate: dayjs('2023-12-13T21:45'),
  releaseDate: dayjs('2023-12-14T20:39'),
  quantityIncluded: 30679.88,
  piecesIncluded: 840,
  weight: 31874.32,
  height: 16705.1,
  width: 8321.45,
  depth: 17990.73,
  smallImageUrl: 'beneath blind inasmuch',
  mediumImageUrl: 'ick',
  largeImageUrl: 'ha aggressive oddly',
  detailImageUrl: 'fooey',
  originalImageUrl: 'like soon',
  altImageUrl1: 'aside upbeat cauliflower',
  altImageUrl2: 'keel',
  altImageUrl3: 'splendid',
  altImageUrl4: 'mid',
  altImageUrl5: 'before cuckoo which',
  altImageUrl6: 'idealistic',
  videoUrl: 'boohoo trouser unless',
  quantity: 22127.73,
  price: 1461.01,
  cgst: 32332.67,
  igst: 21981.3,
  sgst: 31289.61,
  cgstPercentage: 24534.74,
  igstPercentage: 17120.44,
  sgstPercentage: 20477.35,
  totalPrice: 31754.95,
  description: 'pronoun ew internalise',
  longDescription: 'where exhaust round',
  info: '../fake-data/blob/hipster.txt',
  isVirtual: true,
  isVariant: true,
  requireInventory: false,
  returnable: true,
  isPopular: true,
  isBanner: false,
  bannerUrl: 'hmph mount sociable',
};

export const sampleWithNewData: NewProduct = {
  name: 'anti inject why',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
