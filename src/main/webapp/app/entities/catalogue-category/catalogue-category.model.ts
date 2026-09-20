import { ICatalogue } from 'app/entities/catalogue/catalogue.model';
import { ICatalogueCategoryType } from 'app/entities/catalogue-category-type/catalogue-category-type.model';
import { IProductCategory } from 'app/entities/product-category/product-category.model';

export interface ICatalogueCategory {
  id: number;
  sequenceNo?: number | null;
  catalogue?: ICatalogue | null;
  productCategory?: IProductCategory | null;
  catalogueCategoryType?: ICatalogueCategoryType | null;
}

export type NewCatalogueCategory = Omit<ICatalogueCategory, 'id'> & { id: null };
