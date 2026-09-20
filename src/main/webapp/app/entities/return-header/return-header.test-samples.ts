import { IReturnHeader, NewReturnHeader } from './return-header.model';

export const sampleWithRequiredData: IReturnHeader = {
  id: 11805,
};

export const sampleWithPartialData: IReturnHeader = {
  id: 30603,
  supplierRmaId: 'about',
};

export const sampleWithFullData: IReturnHeader = {
  id: 7699,
  needsInventoryReceive: false,
  supplierRmaId: 'forecast',
};

export const sampleWithNewData: NewReturnHeader = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
