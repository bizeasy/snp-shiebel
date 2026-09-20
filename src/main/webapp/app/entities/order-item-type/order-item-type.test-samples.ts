import { IOrderItemType, NewOrderItemType } from './order-item-type.model';

export const sampleWithRequiredData: IOrderItemType = {
  id: 6865,
  name: 'overvalue oof',
};

export const sampleWithPartialData: IOrderItemType = {
  id: 25933,
  name: 'regular',
  description: 'abnormally quicker',
};

export const sampleWithFullData: IOrderItemType = {
  id: 5349,
  name: 'whereas firsthand',
  description: 'vista',
};

export const sampleWithNewData: NewOrderItemType = {
  name: 'judgementally smoggy',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
