import { IAttachment } from 'app/entities/attachment/attachment.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderAttachment {
  id: number;
  name?: string | null;
  order?: IOrder | null;
  attachment?: IAttachment | null;
}

export type NewOrderAttachment = Omit<IOrderAttachment, 'id'> & { id: null };
