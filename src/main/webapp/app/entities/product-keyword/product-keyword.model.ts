import { IKeywordType } from 'app/entities/keyword-type/keyword-type.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductKeyword {
  id: number;
  keyword?: string | null;
  relevancyWeight?: number | null;
  product?: IProduct | null;
  keywordType?: IKeywordType | null;
}

export type NewProductKeyword = Omit<IProductKeyword, 'id'> & { id: null };
