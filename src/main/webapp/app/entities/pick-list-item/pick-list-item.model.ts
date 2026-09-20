import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IPickList } from 'app/entities/pick-list/pick-list.model';
import { IProduct } from 'app/entities/product/product.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IPickListItem {
  id: number;
  quantity?: number | null;
  pickList?: IPickList | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  inventoryItem?: IInventoryItem | null;
  product?: IProduct | null;
  facilityLocation?: IFacilityLocation | null;
  status?: IStatus | null;
}

export type NewPickListItem = Omit<IPickListItem, 'id'> & { id: null };
