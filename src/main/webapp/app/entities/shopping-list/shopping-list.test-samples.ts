import { IShoppingList, NewShoppingList } from './shopping-list.model';

export const sampleWithRequiredData: IShoppingList = {
  id: 5357,
  name: 'newsletter through far-flung',
};

export const sampleWithPartialData: IShoppingList = {
  id: 29307,
  name: 'a throughout too',
};

export const sampleWithFullData: IShoppingList = {
  id: 10423,
  name: 'calculating pine',
  isActive: true,
};

export const sampleWithNewData: NewShoppingList = {
  name: 'obediently',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
