import dayjs from 'dayjs/esm';

import { IOrder } from 'app/entities/order/order.model';
import { IOrderAdjustmentType } from 'app/entities/order-adjustment-type/order-adjustment-type.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IProduct } from 'app/entities/product/product.model';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';

export interface IOrderAdjustment {
  id: number;
  description?: string | null;
  comments?: string | null;
  amount?: number | null;
  sourcePercentage?: number | null;
  sourceRefId?: string | null;
  exemptAmount?: number | null;
  includeInTax?: boolean | null;
  includeInShipping?: boolean | null;
  includedInPrice?: boolean | null;
  entryDate?: dayjs.Dayjs | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  adjustmentType?: IOrderAdjustmentType | null;
  product?: IProduct | null;
  productFeature?: IProductFeature | null;
  taxAuthority?: ITaxAuthority | null;
}

export type NewOrderAdjustment = Omit<IOrderAdjustment, 'id'> & { id: null };
