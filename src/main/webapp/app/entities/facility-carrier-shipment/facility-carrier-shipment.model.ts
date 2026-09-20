import { IFacility } from 'app/entities/facility/facility.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';

export interface IFacilityCarrierShipment {
  id: number;
  facility?: IFacility | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
  shipmentMethodType?: IShipmentMethodType | null;
}

export type NewFacilityCarrierShipment = Omit<IFacilityCarrierShipment, 'id'> & { id: null };
