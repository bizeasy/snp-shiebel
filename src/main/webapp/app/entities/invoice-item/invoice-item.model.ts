import dayjs from 'dayjs/esm';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IInvoiceItemType } from 'app/entities/invoice-item-type/invoice-item-type.model';
import { IProduct } from 'app/entities/product/product.model';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ITaxAuthorityRateType } from 'app/entities/tax-authority-rate-type/tax-authority-rate-type.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IInvoiceItem {
  id: number;
  sequenceNo?: number | null;
  quantity?: number | null;
  amount?: number | null;
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
  invoice?: IInvoice | null;
  invoiceItemType?: IInvoiceItemType | null;
  product?: IProduct | null;
  uom?: IUom | null;
  productFeature?: IProductFeature | null;
  productFacility?: IProductFacility | null;
  fromInventoryItem?: IInventoryItem | null;
  taxAuthRate?: ITaxAuthorityRateType | null;
  parentInvoice?: IInvoice | null;
  parentInvoiceItem?: IInvoiceItem | null;
}

export type NewInvoiceItem = Omit<IInvoiceItem, 'id'> & { id: null };
