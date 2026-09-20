import { IShoppingListType, NewShoppingListType } from './shopping-list-type.model';

export const sampleWithRequiredData: IShoppingListType = {
  id: 13061,
  name: 'huzzah over',
};

export const sampleWithPartialData: IShoppingListType = {
  id: 3075,
  name: 'limply',
};

export const sampleWithFullData: IShoppingListType = {
  id: 19140,
  name: 'segregate',
  description: 'overplay',
};

export const sampleWithNewData: NewShoppingListType = {
  name: 'oof youthfully better',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
