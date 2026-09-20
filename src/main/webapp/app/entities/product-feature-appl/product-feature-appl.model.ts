import dayjs from 'dayjs/esm';

import { IProduct } from 'app/entities/product/product.model';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IProductFeatureApplType } from 'app/entities/product-feature-appl-type/product-feature-appl-type.model';

export interface IProductFeatureAppl {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  amount?: number | null;
  product?: IProduct | null;
  productFeature?: IProductFeature | null;
  featureApplType?: IProductFeatureApplType | null;
}

export type NewProductFeatureAppl = Omit<IProductFeatureAppl, 'id'> & { id: null };
