import { IInventoryItemType, NewInventoryItemType } from './inventory-item-type.model';

export const sampleWithRequiredData: IInventoryItemType = {
  id: 29641,
  name: 'gadzooks',
};

export const sampleWithPartialData: IInventoryItemType = {
  id: 4354,
  name: 'for yummy besmirch',
};

export const sampleWithFullData: IInventoryItemType = {
  id: 24359,
  name: 'for measly',
  description: 'worse',
};

export const sampleWithNewData: NewInventoryItemType = {
  name: 'during pine',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
