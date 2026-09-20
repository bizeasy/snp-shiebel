import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';

export interface ICarrierShipmentMethod {
  id: number;
  sequenceNo?: number | null;
  carrierServiceCode?: string | null;
  shipmentMethodType?: IShipmentMethodType | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewCarrierShipmentMethod = Omit<ICarrierShipmentMethod, 'id'> & { id: null };
