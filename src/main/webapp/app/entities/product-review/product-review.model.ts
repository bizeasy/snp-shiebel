import dayjs from 'dayjs/esm';

import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductReview {
  id: number;
  rating?: number | null;
  ratingInfo?: string | null;
  message?: string | null;
  reviewDate?: dayjs.Dayjs | null;
  product?: IProduct | null;
  party?: IParty | null;
  orderItem?: IOrderItem | null;
}

export type NewProductReview = Omit<IProductReview, 'id'> & { id: null };
