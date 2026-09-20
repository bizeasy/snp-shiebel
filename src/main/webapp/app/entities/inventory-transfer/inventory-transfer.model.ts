import dayjs from 'dayjs/esm';

import { IFacility } from 'app/entities/facility/facility.model';
import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IInventoryTransfer {
  id: number;
  sentDate?: dayjs.Dayjs | null;
  receivedDate?: dayjs.Dayjs | null;
  comments?: string | null;
  inventoryItem?: IInventoryItem | null;
  status?: IStatus | null;
  facility?: IFacility | null;
  facilityLocation?: IFacilityLocation | null;
  facilityTo?: IFacility | null;
  facilityLocationTo?: IFacilityLocation | null;
  issuance?: IItemIssuance | null;
}

export type NewInventoryTransfer = Omit<IInventoryTransfer, 'id'> & { id: null };
