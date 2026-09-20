import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IProduct } from 'app/entities/product/product.model';
import { IReturnHeader } from 'app/entities/return-header/return-header.model';
import { IReturnHeaderType } from 'app/entities/return-header-type/return-header-type.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IReturnItem {
  id: number;
  sequenceNo?: number | null;
  description?: string | null;
  returnQuantity?: number | null;
  receivedQuantity?: number | null;
  returnPrice?: number | null;
  returnItemPurpose?: string | null;
  returnHeader?: IReturnHeader | null;
  returnType?: IReturnHeaderType | null;
  product?: IProduct | null;
  orderItem?: IOrderItem | null;
  status?: IStatus | null;
}

export type NewReturnItem = Omit<IReturnItem, 'id'> & { id: null };
