import { IAttachment } from 'app/entities/attachment/attachment.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';

export interface IOrderItemAttachment {
  id: number;
  name?: string | null;
  orderItem?: IOrderItem | null;
  attachment?: IAttachment | null;
}

export type NewOrderItemAttachment = Omit<IOrderItemAttachment, 'id'> & { id: null };
