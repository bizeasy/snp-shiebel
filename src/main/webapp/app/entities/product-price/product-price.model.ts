import dayjs from 'dayjs/esm';

import { IProduct } from 'app/entities/product/product.model';
import { IProductPricePurpose } from 'app/entities/product-price-purpose/product-price-purpose.model';
import { IProductPriceType } from 'app/entities/product-price-type/product-price-type.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IProductPrice {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  price?: number | null;
  cgst?: number | null;
  igst?: number | null;
  sgst?: number | null;
  totalPrice?: number | null;
  product?: IProduct | null;
  productPriceType?: IProductPriceType | null;
  productPricePurpose?: IProductPricePurpose | null;
  currencyUom?: IUom | null;
}

export type NewProductPrice = Omit<IProductPrice, 'id'> & { id: null };
