import dayjs from 'dayjs/esm';

import { IInventoryItemDelegate, NewInventoryItemDelegate } from './inventory-item-delegate.model';

export const sampleWithRequiredData: IInventoryItemDelegate = {
  id: 14749,
  effectiveDate: dayjs('2023-12-13T22:30'),
};

export const sampleWithPartialData: IInventoryItemDelegate = {
  id: 8525,
  sequenceNo: 4412,
  effectiveDate: dayjs('2023-12-13T23:08'),
};

export const sampleWithFullData: IInventoryItemDelegate = {
  id: 26165,
  sequenceNo: 2987,
  effectiveDate: dayjs('2023-12-14T13:36'),
  quantityOnHandDiff: 20632.25,
  availableToPromiseDiff: 17517.46,
  accountingQuantityDiff: 1610.95,
  unitCost: 19091.78,
  remarks: 'fully supposing',
};

export const sampleWithNewData: NewInventoryItemDelegate = {
  effectiveDate: dayjs('2023-12-13T23:24'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
