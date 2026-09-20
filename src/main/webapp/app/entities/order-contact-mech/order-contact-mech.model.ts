import dayjs from 'dayjs/esm';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderContactMech {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  order?: IOrder | null;
  contactMech?: IContactMech | null;
  contactMechPurpose?: IContactMechPurpose | null;
}

export type NewOrderContactMech = Omit<IOrderContactMech, 'id'> & { id: null };
