import dayjs from 'dayjs/esm';

import { IProductReview, NewProductReview } from './product-review.model';

export const sampleWithRequiredData: IProductReview = {
  id: 21277,
};

export const sampleWithPartialData: IProductReview = {
  id: 31902,
  ratingInfo: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IProductReview = {
  id: 31072,
  rating: 8,
  ratingInfo: '../fake-data/blob/hipster.txt',
  message: 'superior',
  reviewDate: dayjs('2023-12-14T06:42'),
};

export const sampleWithNewData: NewProductReview = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
