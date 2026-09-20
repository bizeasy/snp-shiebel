import dayjs from 'dayjs/esm';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';

export interface IItemIssuance {
  id: number;
  message?: string | null;
  issuedDate?: dayjs.Dayjs | null;
  issuedBy?: string | null;
  quantity?: number | null;
  cancelQuantity?: number | null;
  inventoryItem?: IInventoryItem | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  shipment?: IShipment | null;
  shipmentItem?: IShipmentItem | null;
  issuedByParty?: IParty | null;
}

export type NewItemIssuance = Omit<IItemIssuance, 'id'> & { id: null };
