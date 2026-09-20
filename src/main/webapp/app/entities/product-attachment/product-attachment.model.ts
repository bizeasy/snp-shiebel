import { IAttachment } from 'app/entities/attachment/attachment.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductAttachment {
  id: number;
  name?: string | null;
  product?: IProduct | null;
  attachment?: IAttachment | null;
}

export type NewProductAttachment = Omit<IProductAttachment, 'id'> & { id: null };
