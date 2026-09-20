import dayjs from 'dayjs/esm';

import { IOrderAdjustmentType } from 'app/entities/order-adjustment-type/order-adjustment-type.model';
import { IProduct } from 'app/entities/product/product.model';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IQuote } from 'app/entities/quote/quote.model';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';

export interface IQuoteAdjustment {
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
  quote?: IQuote | null;
  quoteItem?: IQuoteItem | null;
  adjustmentType?: IOrderAdjustmentType | null;
  product?: IProduct | null;
  productFeature?: IProductFeature | null;
  taxAuthority?: ITaxAuthority | null;
}

export type NewQuoteAdjustment = Omit<IQuoteAdjustment, 'id'> & { id: null };
