import dayjs from 'dayjs/esm';

import { IShoppingListItem, NewShoppingListItem } from './shopping-list-item.model';

export const sampleWithRequiredData: IShoppingListItem = {
  id: 21582,
  quantity: 8592.68,
};

export const sampleWithPartialData: IShoppingListItem = {
  id: 26859,
  quantity: 26129.25,
  requiredByDate: dayjs('2023-12-14T04:28'),
  selectedAmount: 17320.26,
};

export const sampleWithFullData: IShoppingListItem = {
  id: 31784,
  sequenceNo: 17567,
  quantity: 27645.53,
  requiredByDate: dayjs('2023-12-13T22:38'),
  selectedAmount: 25625.3,
  maxAmount: 11305.36,
  description: 'uselessly',
  story: 'from minister',
};

export const sampleWithNewData: NewShoppingListItem = {
  quantity: 1087.32,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
