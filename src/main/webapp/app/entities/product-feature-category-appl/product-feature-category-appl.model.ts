import dayjs from 'dayjs/esm';

import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { IProductFeatureCategory } from 'app/entities/product-feature-category/product-feature-category.model';

export interface IProductFeatureCategoryAppl {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  productCategory?: IProductCategory | null;
  productFeatureCategory?: IProductFeatureCategory | null;
}

export type NewProductFeatureCategoryAppl = Omit<IProductFeatureCategoryAppl, 'id'> & { id: null };
