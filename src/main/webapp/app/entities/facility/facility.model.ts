import { IFacilityGroup } from 'app/entities/facility-group/facility-group.model';
import { IFacilityType } from 'app/entities/facility-type/facility-type.model';
import { IGeoPoint } from 'app/entities/geo-point/geo-point.model';
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { IParty } from 'app/entities/party/party.model';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IFacility {
  id: number;
  name?: string | null;
  description?: string | null;
  facilitySize?: number | null;
  facilityType?: IFacilityType | null;
  parentFacility?: IFacility | null;
  ownerParty?: IParty | null;
  facilityGroup?: IFacilityGroup | null;
  sizeUom?: IUom | null;
  geoPoint?: IGeoPoint | null;
  inventoryItemType?: IInventoryItemType | null;
  productStore?: IProductStore | null;
}

export type NewFacility = Omit<IFacility, 'id'> & { id: null };
