import dayjs from 'dayjs/esm';

import { IShipment } from 'app/entities/shipment/shipment.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IShipmentStatus {
  id: number;
  statusDateTime?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  shipment?: IShipment | null;
  status?: IStatus | null;
}

export type NewShipmentStatus = Omit<IShipmentStatus, 'id'> & { id: null };
