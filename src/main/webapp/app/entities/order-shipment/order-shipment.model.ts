import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';

export interface IOrderShipment {
  id: number;
  quantity?: number | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  shipment?: IShipment | null;
  shipmentItem?: IShipmentItem | null;
}

export type NewOrderShipment = Omit<IOrderShipment, 'id'> & { id: null };
