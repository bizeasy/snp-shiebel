import { IProductFeatureCategory } from 'app/entities/product-feature-category/product-feature-category.model';
import { IProductFeatureType } from 'app/entities/product-feature-type/product-feature-type.model';

export interface IProductFeature {
  id: number;
  name?: string | null;
  description?: string | null;
  amount?: number | null;
  sequenceNo?: number | null;
  abbrev?: string | null;
  productFeatureType?: IProductFeatureType | null;
  productFeatureCategory?: IProductFeatureCategory | null;
}

export type NewProductFeature = Omit<IProductFeature, 'id'> & { id: null };
