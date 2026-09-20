import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IOrderRole {
  id: number;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewOrderRole = Omit<IOrderRole, 'id'> & { id: null };
