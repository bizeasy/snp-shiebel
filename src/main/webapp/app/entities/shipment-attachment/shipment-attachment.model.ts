import { IAttachment } from 'app/entities/attachment/attachment.model';
import { IShipment } from 'app/entities/shipment/shipment.model';

export interface IShipmentAttachment {
  id: number;
  name?: string | null;
  shipment?: IShipment | null;
  attachment?: IAttachment | null;
}

export type NewShipmentAttachment = Omit<IShipmentAttachment, 'id'> & { id: null };
