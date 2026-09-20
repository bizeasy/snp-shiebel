import dayjs from 'dayjs/esm';

import { IProduct } from 'app/entities/product/product.model';
import { IProductCategory } from 'app/entities/product-category/product-category.model';

export interface IProductCategoryMember {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  product?: IProduct | null;
  productCategory?: IProductCategory | null;
}

export type NewProductCategoryMember = Omit<IProductCategoryMember, 'id'> & { id: null };
