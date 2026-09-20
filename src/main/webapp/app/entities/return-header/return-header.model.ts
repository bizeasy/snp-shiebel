import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IFacility } from 'app/entities/facility/facility.model';
import { IParty } from 'app/entities/party/party.model';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { IReturnHeaderType } from 'app/entities/return-header-type/return-header-type.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IReturnHeader {
  id: number;
  needsInventoryReceive?: boolean | null;
  supplierRmaId?: string | null;
  returnType?: IReturnHeaderType | null;
  status?: IStatus | null;
  fromParty?: IParty | null;
  toParty?: IParty | null;
  paymentMethod?: IPaymentMethod | null;
  originContactMech?: IContactMech | null;
  destinationFacility?: IFacility | null;
  currencyUom?: IUom | null;
}

export type NewReturnHeader = Omit<IReturnHeader, 'id'> & { id: null };
