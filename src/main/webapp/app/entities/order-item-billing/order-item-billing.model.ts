import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';

export interface IOrderItemBilling {
  id: number;
  quantity?: number | null;
  amount?: number | null;
  orderItem?: IOrderItem | null;
  invoiceItem?: IInvoiceItem | null;
  shipmentReceipt?: IShipmentReceipt | null;
  itemIssuance?: IItemIssuance | null;
}

export type NewOrderItemBilling = Omit<IOrderItemBilling, 'id'> & { id: null };
