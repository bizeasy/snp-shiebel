import dayjs from 'dayjs/esm';

import { IInventoryItem, NewInventoryItem } from './inventory-item.model';

export const sampleWithRequiredData: IInventoryItem = {
  id: 19345,
};

export const sampleWithPartialData: IInventoryItem = {
  id: 13944,
  receivedDate: dayjs('2023-12-14T10:11'),
  retestDate: dayjs('2023-12-14T08:25'),
  mfgBatchNo: 'lest explode',
  binNumber: 'ill-fated',
  quantityOnHandTotal: 27216.32,
  availableToPromiseTotal: 2228.22,
  accountingQuantityTotal: 27444.81,
  softIdentifier: 'hidden blah',
  activationNumber: 'blah scuffle',
  activationValidThru: dayjs('2023-12-14T10:14'),
};

export const sampleWithFullData: IInventoryItem = {
  id: 20465,
  receivedDate: dayjs('2023-12-14T10:15'),
  manufacturedDate: dayjs('2023-12-14T08:26'),
  expiryDate: dayjs('2023-12-14T02:03'),
  retestDate: dayjs('2023-12-14T11:49'),
  containerId: 'moral jittery',
  batchNo: 'till optimistic',
  mfgBatchNo: 'lavish inspection now',
  lotNoStr: 'inwardly gleefully given',
  binNumber: 'apologise round nearly',
  comments: 'frozen fooey kiddingly',
  quantityOnHandTotal: 27836.04,
  availableToPromiseTotal: 3255.31,
  accountingQuantityTotal: 28311.61,
  oldQuantityOnHand: 28263.37,
  oldAvailableToPromise: 26986.48,
  serialNumber: 'wrong or',
  softIdentifier: 'quash pish likewise',
  activationNumber: 'and give instruction',
  activationValidThru: dayjs('2023-12-14T12:45'),
  unitCost: 10375.09,
};

export const sampleWithNewData: NewInventoryItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
