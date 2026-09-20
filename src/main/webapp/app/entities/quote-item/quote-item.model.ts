import dayjs from 'dayjs/esm';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { IProduct } from 'app/entities/product/product.model';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IQuote } from 'app/entities/quote/quote.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IQuoteItem {
  id: number;
  sequenceNo?: number | null;
  quantity?: number | null;
  selectedAmount?: number | null;
  quoteUnitPrice?: number | null;
  totalAmount?: number | null;
  estimatedDeliveryDate?: dayjs.Dayjs | null;
  leadTimeDays?: number | null;
  comments?: string | null;
  quote?: IQuote | null;
  product?: IProduct | null;
  productFeature?: IProductFeature | null;
  uom?: IUom | null;
  status?: IStatus | null;
  custRequest?: ICustRequest | null;
  custRequestItem?: ICustRequestItem | null;
}

export type NewQuoteItem = Omit<IQuoteItem, 'id'> & { id: null };
