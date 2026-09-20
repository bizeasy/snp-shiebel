import dayjs from 'dayjs/esm';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItemType } from 'app/entities/order-item-type/order-item-type.model';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { IShoppingListItem } from 'app/entities/shopping-list-item/shopping-list-item.model';
import { IStatus } from 'app/entities/status/status.model';
import { ISupplierProduct } from 'app/entities/supplier-product/supplier-product.model';
import { ITaxAuthorityRateType } from 'app/entities/tax-authority-rate-type/tax-authority-rate-type.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IOrderItem {
  id: number;
  sequenceNo?: number | null;
  quantity?: number | null;
  cancelQuantity?: number | null;
  selectedAmount?: number | null;
  unitPrice?: number | null;
  unitListPrice?: number | null;
  isModifiedPrice?: boolean | null;
  estimatedShipDate?: dayjs.Dayjs | null;
  estimatedDeliveryDate?: dayjs.Dayjs | null;
  shipBeforeDate?: dayjs.Dayjs | null;
  shipAfterDate?: dayjs.Dayjs | null;
  entryDate?: dayjs.Dayjs | null;
  attr1?: string | null;
  attr2?: string | null;
  attr3?: string | null;
  order?: IOrder | null;
  orderItemType?: IOrderItemType | null;
  product?: IProduct | null;
  uom?: IUom | null;
  supplierProduct?: ISupplierProduct | null;
  supplier?: IParty | null;
  productFeature?: IProductFeature | null;
  productFacility?: IProductFacility | null;
  fromInventoryItem?: IInventoryItem | null;
  shoppingListItem?: IShoppingListItem | null;
  quoteItem?: IQuoteItem | null;
  status?: IStatus | null;
  taxAuthRate?: ITaxAuthorityRateType | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };
