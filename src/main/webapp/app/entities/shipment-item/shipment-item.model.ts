import { IProduct } from 'app/entities/product/product.model';
import { IShipment } from 'app/entities/shipment/shipment.model';

export interface IShipmentItem {
  id: number;
  sequenceNo?: number | null;
  quantity?: number | null;
  message?: string | null;
  shipment?: IShipment | null;
  product?: IProduct | null;
}

export type NewShipmentItem = Omit<IShipmentItem, 'id'> & { id: null };
