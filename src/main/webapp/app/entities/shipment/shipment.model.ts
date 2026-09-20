import dayjs from 'dayjs/esm';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IFacility } from 'app/entities/facility/facility.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { IShipmentType } from 'app/entities/shipment-type/shipment-type.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IShipment {
  id: number;
  estimatedShipDate?: dayjs.Dayjs | null;
  estimatedReadyDate?: dayjs.Dayjs | null;
  estimatedArrivalDate?: dayjs.Dayjs | null;
  estimatedShipCost?: number | null;
  shipmentType?: IShipmentType | null;
  shipmentMethodType?: IShipmentMethodType | null;
  status?: IStatus | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
  originFacility?: IFacility | null;
  destinationFacility?: IFacility | null;
  fromParty?: IParty | null;
  toParty?: IParty | null;
  carrierParty?: IParty | null;
  contactMech?: IContactMech | null;
}

export type NewShipment = Omit<IShipment, 'id'> & { id: null };
