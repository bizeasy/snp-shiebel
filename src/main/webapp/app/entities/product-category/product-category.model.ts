import { IProductCategoryType } from 'app/entities/product-category-type/product-category-type.model';

export interface IProductCategory {
  id: number;
  name?: string | null;
  description?: string | null;
  longDescription?: string | null;
  attribute?: string | null;
  sequenceNo?: number | null;
  imageUrl?: string | null;
  altImageUrl?: string | null;
  bannerUrl?: string | null;
  largeImageUrl?: string | null;
  altLargeImageUrl?: string | null;
  info?: string | null;
  isBanner?: boolean | null;
  productCategoryType?: IProductCategoryType | null;
  parent?: IProductCategory | null;
}

export type NewProductCategory = Omit<IProductCategory, 'id'> & { id: null };
