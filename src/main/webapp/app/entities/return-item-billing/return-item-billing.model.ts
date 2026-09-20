import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IReturnItem } from 'app/entities/return-item/return-item.model';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';

export interface IReturnItemBilling {
  id: number;
  quantity?: number | null;
  amount?: number | null;
  returnItem?: IReturnItem | null;
  orderItem?: IOrderItem | null;
  invoiceItem?: IInvoiceItem | null;
  shipmentReceipt?: IShipmentReceipt | null;
}

export type NewReturnItemBilling = Omit<IReturnItemBilling, 'id'> & { id: null };
