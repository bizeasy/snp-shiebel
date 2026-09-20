import dayjs from 'dayjs/esm';

import { IFacility } from 'app/entities/facility/facility.model';
import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { ILot } from 'app/entities/lot/lot.model';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IInventoryItem {
  id: number;
  receivedDate?: dayjs.Dayjs | null;
  manufacturedDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  retestDate?: dayjs.Dayjs | null;
  containerId?: string | null;
  batchNo?: string | null;
  mfgBatchNo?: string | null;
  lotNoStr?: string | null;
  binNumber?: string | null;
  comments?: string | null;
  quantityOnHandTotal?: number | null;
  availableToPromiseTotal?: number | null;
  accountingQuantityTotal?: number | null;
  oldQuantityOnHand?: number | null;
  oldAvailableToPromise?: number | null;
  serialNumber?: string | null;
  softIdentifier?: string | null;
  activationNumber?: string | null;
  activationValidThru?: dayjs.Dayjs | null;
  unitCost?: number | null;
  inventoryItemType?: IInventoryItemType | null;
  product?: IProduct | null;
  supplier?: IParty | null;
  ownerParty?: IParty | null;
  status?: IStatus | null;
  facility?: IFacility | null;
  facilityLocation?: IFacilityLocation | null;
  uom?: IUom | null;
  currencyUom?: IUom | null;
  lot?: ILot | null;
}

export type NewInventoryItem = Omit<IInventoryItem, 'id'> & { id: null };
