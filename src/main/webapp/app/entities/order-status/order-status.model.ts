import dayjs from 'dayjs/esm';

import { IOrder } from 'app/entities/order/order.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IOrderStatus {
  id: number;
  statusDateTime?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  order?: IOrder | null;
  status?: IStatus | null;
}

export type NewOrderStatus = Omit<IOrderStatus, 'id'> & { id: null };
