import dayjs from 'dayjs/esm';

import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { IProductFeatureGroup } from 'app/entities/product-feature-group/product-feature-group.model';

export interface IProductFeatureGroupAppl {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  productFeatureGroup?: IProductFeatureGroup | null;
  productFeature?: IProductFeature | null;
}

export type NewProductFeatureGroupAppl = Omit<IProductFeatureGroupAppl, 'id'> & { id: null };
