import dayjs from 'dayjs/esm';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';

export interface IShipmentReceipt {
  id: number;
  sequenceNo?: number | null;
  quantity?: number | null;
  quantityAccepted?: number | null;
  quantityRejected?: number | null;
  receivedDate?: dayjs.Dayjs | null;
  message?: string | null;
  shipment?: IShipment | null;
  shipmentItem?: IShipmentItem | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  inventoryItem?: IInventoryItem | null;
  product?: IProduct | null;
  receivedBy?: IParty | null;
}

export type NewShipmentReceipt = Omit<IShipmentReceipt, 'id'> & { id: null };
