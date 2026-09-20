import dayjs from 'dayjs/esm';

import { IProduct } from 'app/entities/product/product.model';
import { IProductAssocType } from 'app/entities/product-assoc-type/product-assoc-type.model';

export interface IProductAssoc {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  quantity?: number | null;
  product?: IProduct | null;
  productTo?: IProduct | null;
  productAssocType?: IProductAssocType | null;
}

export type NewProductAssoc = Omit<IProductAssoc, 'id'> & { id: null };
