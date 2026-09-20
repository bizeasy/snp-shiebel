import dayjs from 'dayjs/esm';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { IShipmentReceipt } from 'app/entities/shipment-receipt/shipment-receipt.model';

export interface IInventoryItemDelegate {
  id: number;
  sequenceNo?: number | null;
  effectiveDate?: dayjs.Dayjs | null;
  quantityOnHandDiff?: number | null;
  availableToPromiseDiff?: number | null;
  accountingQuantityDiff?: number | null;
  unitCost?: number | null;
  remarks?: string | null;
  inventoryItem?: IInventoryItem | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  invoice?: IInvoice | null;
  invoiceItem?: IInvoiceItem | null;
  shipment?: IShipment | null;
  shipmentItem?: IShipmentItem | null;
  itemIssuance?: IItemIssuance | null;
  receipt?: IShipmentReceipt | null;
}

export type NewInventoryItemDelegate = Omit<IInventoryItemDelegate, 'id'> & { id: null };
