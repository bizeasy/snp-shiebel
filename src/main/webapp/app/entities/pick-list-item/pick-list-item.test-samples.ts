import { IPickListItem, NewPickListItem } from './pick-list-item.model';

export const sampleWithRequiredData: IPickListItem = {
  id: 19736,
  quantity: 31661.93,
};

export const sampleWithPartialData: IPickListItem = {
  id: 573,
  quantity: 956.21,
};

export const sampleWithFullData: IPickListItem = {
  id: 19977,
  quantity: 1063.75,
};

export const sampleWithNewData: NewPickListItem = {
  quantity: 4332.61,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
